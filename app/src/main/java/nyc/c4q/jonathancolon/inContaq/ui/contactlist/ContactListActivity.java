package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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
import nyc.c4q.jonathancolon.inContaq.utlities.MaterialTapHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoHelper;

import static android.content.Intent.ACTION_PICK;
import static android.os.Build.VERSION.SDK_INT;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.provider.ContactsContract.Contacts.CONTENT_URI;
import static nyc.c4q.jonathancolon.inContaq.common.di.Injector.getApplicationComponent;
import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isEmptyList;
import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isNull;

public class ContactListActivity extends BaseActivity implements
        ContactListAdapter.Listener, ContactListContract.View, AlertDialogCallback<Integer> {

    public static final String CONTACT_KEY = "Contact";
    private static final String PERMISSION_KEY = "request_permissions";
    private static final String TAG = ContactListActivity.class.getSimpleName();
    private static final int REQUEST_CODE_SELECT_CONTACT = 1;

    @Inject
    RealmService realmService;
    @Inject
    ContactListPresenter presenter;
    @Inject
    ServiceLauncher serviceLauncher;
    @Inject
    RetrieveSingleContact retrieveSingleContact;
    @Inject
    PicassoHelper pUtils;

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
            startActivityForResult(new Intent(ACTION_PICK,
                    CONTENT_URI), REQUEST_CODE_SELECT_CONTACT);
    }

    @Override
    public void initializeMaterialTapPrompt(RealmResults<Contact> contactList) {
        MaterialTapHelper tapHelper = new MaterialTapHelper(this, ContactListActivity.this,
                getPreferences(MODE_PRIVATE), contactList);
        tapHelper.addFirstContactPrompt();
    }

    private void buildSaveEditDialog(Contact contact) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Delete this contact: " + contact.getFullName() + "?");
        alertDialog.setMessage(R.string.are_you_sure);

        alertDialog.setPositiveButton(R.string.positive_button, (dialog, which) -> {
            selection = 1;
            ContactListActivity.this.alertDialogCallback(selection, contact);

        }).setNegativeButton(R.string.negative_button, (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    @Override
    public void alertDialogCallback(Integer ret, Contact contact) {
        ret = selection;
        if (ret == 1) {
            realmService.deleteContactFromRealmDB(contact);
            contactList = presenter.retrieveContacts();
            refreshContactList(contactList);
        }
    }

    @Override
    public void checkService() {
        Log.e(TAG, String.valueOf(ContactNotificationService.hasStarted ));
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
        recyclerView.setAdapter(new ContactListAdapter(this, this, pUtils));
        recyclerView.setItemViewCacheSize(30);
        recyclerView.setDrawingCacheEnabled(true);
    }

    @Override
    public void preLoadContactListImages() {
        if (!isEmptyList(contactList)) {
            for (int i = 0; i < contactList.size(); i++) {
                if (!isNull(contactList.get(i).getBackgroundImage())) {
                    pUtils.preloadImages(contactList.get(i).getBackgroundImage());
                }
                if (!isNull(contactList.get(i).getContactImage())) {
                    pUtils.preloadImages(contactList.get(i).getContactImage());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_CONTACT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Contact contact = retrieveSingleContact.createContact(uri);
            realmService.addContactToRealmDB(contact);
        }
    }

    @Override
    public void onContactClicked(Contact contact) {
        if (!isNull(contact.getMobileNumber())) {
            Intent intent = new Intent(this, ContactDetailsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(CONTACT_KEY, contact.getRealmID());
            this.startActivity(intent);
        } else {
            Toast.makeText(this, "Contact does not have a mobile number",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onContactLongClicked(Contact contact) {
        buildSaveEditDialog(contact);
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