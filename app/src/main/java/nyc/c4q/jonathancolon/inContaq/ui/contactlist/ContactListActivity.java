package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.common.base.BaseActivity;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.notifications.ContactNotificationService;
import nyc.c4q.jonathancolon.inContaq.notifications.MyAlarmReceiver;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.ContactDetailsActivity;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactinfo.AlertDialogCallback;
import nyc.c4q.jonathancolon.inContaq.utlities.DeviceUtils;
import nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoUtils;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

import static android.content.Intent.ACTION_PICK;
import static android.os.Build.VERSION.SDK_INT;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.provider.ContactsContract.Contacts.CONTENT_URI;
import static nyc.c4q.jonathancolon.inContaq.common.dagger.Injector.getApplicationComponent;
import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isEmptyList;
import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isNull;

public class ContactListActivity extends BaseActivity implements
        ContactListAdapter.Listener, ContactListContract.View, AlertDialogCallback<Integer> {

    public static final String CONTACT_KEY = "Contact";
    private static final String PERMISSION_KEY = "request_permissions";
    private static final String TAG = ContactListActivity.class.getSimpleName();
    private static final int REQUEST_CODE_SELECT_CONTACT = 1;
    public static final int DELETE_CONTACT = 1;

    @Inject
    RealmService realmService;
    @Inject
    ContactListPresenter presenter;
    @Inject
    ServiceLauncher serviceLauncher;
    @Inject
    ContactReader contactReader;
    @Inject
    PicassoUtils picasso;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab_add_contact)
    FloatingActionButton addContactFab;

    private RealmResults<Contact> contactList;
    private MyAlarmReceiver receiver;
    private int selection;

    @Override
    protected void initializeDagger() {
        getApplicationComponent().inject(ContactListActivity.this);
    }

    @Override
    protected void initializePresenter() {
        super.presenter = presenter;
        presenter.setView(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_contact_list;
    }

    @OnClick(R.id.fab_add_contact)
    public void onClick() {
        presenter.onAddContactClicked();
    }

    @Override
    public void selectContact() {
        startActivityForResult(new Intent(ACTION_PICK,
                CONTENT_URI), REQUEST_CODE_SELECT_CONTACT);
    }

    @Override
    public void addFirstContactPrompt() {
        if (ObjectUtils.isEmptyList(contactList)) {
            new MaterialTapTargetPrompt.Builder(this)
                    .setTarget(findViewById(R.id.fab_add_contact))
                    .setPrimaryText(R.string.add_first_contact)
                    .setSecondaryText(R.string.tap_here)
                    .setBackgroundColour(getColor(R.color.charcoal))
                    .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                        @Override
                        public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                            presenter.onaddFirstContactPromptClicked();
                        }

                        @Override
                        public void onHidePromptComplete() {
                            //do nothing
                        }
                    }).show();
        }
    }

    @Override
    public void showDeleteContactDialog(Contact contact) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.delete_this_contact) + contact.getFullName() +
                getString(R.string.question_mark));
        alertDialog.setMessage(R.string.are_you_sure);
        alertDialog.setPositiveButton(R.string.positive_button, (dialog, which) -> {
            selection = DELETE_CONTACT;
            ContactListActivity.this.alertDialogCallback(selection, contact);
        }).setNegativeButton(R.string.negative_button, (dialog, which) -> dialog.cancel());

        alertDialog.show();
    }

    @Override
    public void alertDialogCallback(Integer ret, Contact contact) {
        ret = selection;
        if (ret == DELETE_CONTACT) {
            realmService.deleteContactFromRealmDB(contact);
            contactList = presenter.retrieveContacts();
            refreshContactList(contactList);
        }
    }

    @Override
    public void checkService() {
        Log.e(TAG, String.valueOf(ContactNotificationService.hasStarted));
        serviceLauncher.checkServiceCreated();
    }

    @Override
    public void checkPermissions() {
        if (getDefaultSharedPreferences(this)
                .getBoolean(PERMISSION_KEY, true) &&
                (SDK_INT >= Build.VERSION_CODES.M)) {
            requestPermissions();
        }
    }

    @Override
    public void requestPermissions() {
        startActivity(new Intent(this, PermissionActivity.class));
        finish();
    }

    @Override
    public void initializeRecyclerView() {
        PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setExtraLayoutSpace(DeviceUtils.getScreenHeight(this));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ContactListAdapter(this, this, picasso));
        recyclerView.setItemViewCacheSize(30);
        recyclerView.setDrawingCacheEnabled(true);
    }

    @Override
    public void preLoadContactListImages() {
        if (!isEmptyList(contactList)) {
            for (int i = 0; i < contactList.size(); i++) {
                Contact contact = contactList.get(i);
                if (hasBackgroundImage(contact)) {
                    picasso.preloadImages(contactList.get(i).getBackgroundImage());
                }
                if (hasContactImage(contact)) {
                    picasso.preloadImages(contactList.get(i).getContactImage());
                }
            }
        }
    }

    private boolean hasBackgroundImage(Contact contact) {
        return !isNull(contact.getBackgroundImage());
    }

    private boolean hasContactImage(Contact contact) {
        return !isNull(contact.getContactImage());
    }

    @Override
    public void showNoMobileNumberError() {
        Snackbar.make(recyclerView, getString(R.string.error_no_mobile_found), Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isContactSelected(requestCode, resultCode)) {
            Uri uri = data.getData();
            Contact contact = contactReader.retrieveContact(uri);  //todo extract to presenter
            presenter.addContactToDatabase(contact);
        }
    }

    private boolean isContactSelected(int requestCode, int resultCode) {
        return requestCode == REQUEST_CODE_SELECT_CONTACT && resultCode == RESULT_OK;
    }

    @Override
    public void onContactClicked(Contact contact) {
        presenter.onContactClicked(contact);
    }

    @Override
    public void navigateToContactDetailsActivity(Contact contact) {
        Intent intent = new Intent(this, ContactDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(CONTACT_KEY, contact.getRealmID());
        this.startActivity(intent);
    }

    @Override
    public void onContactLongClicked(Contact contact) {
        presenter.onContactLongClicked(contact);
    }

    @Override
    public void onResume() {
        super.onResume();
        contactList = presenter.retrieveContacts();
        refreshContactList(contactList);
    }

    @Override
    public void refreshContactList(RealmResults<Contact> list) {
        ContactListAdapter adapter = (ContactListAdapter) recyclerView.getAdapter();
        adapter.setData(list);
    }

    @Override
    protected void onPause() {
        super.onPause();
        realmService.closeRealm();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmService.closeRealm();
        if (!isNull(receiver)) {
            unregisterReceiver(receiver);
            Log.d(TAG, "onDestroy: unregisted receiver");
        }
    }
}