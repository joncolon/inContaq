package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.common.base.BaseActivity;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.notifications.MyAlarmReceiver;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.ContactDetailsActivity;
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
        ContactListAdapter.Listener, ContactListContract.View {

    public static final String CONTACT_KEY = "Contact";
    private static final String PERMISSION_KEY = "request_permissions";
    private static final String TAG = ContactListActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;

    @Inject RealmService realmService;
    @Inject ContactListPresenter presenter;
    @Inject ServiceLauncher serviceLauncher;
    @Inject RetrieveSingleContact retrieveSingleContact;
    @Inject PicassoHelper pUtils;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fab_add_contact) FloatingActionButton addContactFab;

    private RealmResults<Contact> contactList;
    private MyAlarmReceiver receiver;

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
        addContactFab.setOnClickListener(v -> {
            startActivityForResult(new Intent(ACTION_PICK,
                    CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
        });
    }

    @Override
    public void initializeMaterialTapPrompt(RealmResults<Contact> contactList) {
        MaterialTapHelper tapHelper = new MaterialTapHelper(this, ContactListActivity.this,
                getPreferences(MODE_PRIVATE), contactList);
        tapHelper.addFirstContactPrompt();
    }

    @Override
    public void checkService() {
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
                if (contactList.get(i).getBackgroundImage() != null) {
                    pUtils.preloadImages(contactList.get(i).getBackgroundImage());
                }
                if (contactList.get(i).getContactImage() != null) {
                    pUtils.preloadImages(contactList.get(i).getContactImage());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            Uri uri = data.getData();

            Contact contact = retrieveSingleContact.createContact(uri);
            realmService.addContactToRealmDB(contact);
        }
    }

    @Override
    public void onContactClicked(Contact contact) {
        Intent intent = new Intent(this, ContactDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(CONTACT_KEY, contact.getRealmID());
        this.startActivity(intent);
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
    protected void onDestroy() {
        super.onDestroy();
        realmService.closeRealm();
        if (!isNull(receiver)) {
            unregisterReceiver(receiver);
            Log.d(TAG, "onDestroy: unregisted receiver");
        }
    }
}