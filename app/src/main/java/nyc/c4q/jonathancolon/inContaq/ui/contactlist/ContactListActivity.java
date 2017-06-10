package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.smsreminder.MyAlarmReceiver;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.ContactViewPagerActivity;
import nyc.c4q.jonathancolon.inContaq.utlities.DeviceUtils;
import nyc.c4q.jonathancolon.inContaq.utlities.Injector;
import nyc.c4q.jonathancolon.inContaq.utlities.MaterialTapHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmService;

import static android.provider.ContactsContract.Contacts.CONTENT_URI;

public class ContactListActivity extends AppCompatActivity implements
        ContactListAdapter.Listener, IContactListView {

    public static final String CONTACT_ID = "Contact";
    private static final String TAG = ContactListActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    ServiceLauncher serviceLauncher;
    private RecyclerView recyclerView;
    private RealmResults<Contact> contactList;
    private MyAlarmReceiver receiver;
    private ContactListPresenter presenter;

    @Inject
    RealmService realmService;
    
    @Inject
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        Activity activity = this;
        presenter = new ContactListPresenter(this);

        Injector.getApplicationComponent().inject(this);

        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("request_permissions", true) &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startActivity(new Intent(this, PermissionActivity.class));
            finish();
            return;
        }

        MaterialTapHelper tapHelper = new MaterialTapHelper(context, activity,
                getPreferences(MODE_PRIVATE), contactList);

        serviceLauncher = new ServiceLauncher(context);
        serviceLauncher.checkServiceCreated();
        initViews();
        setupRecyclerView();
        preloadContactListImages();
        tapHelper.addFirstContactPrompt();
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        FloatingActionButton addContactFab = (FloatingActionButton)
                findViewById(R.id.fab_add_contact);

        addContactFab.setOnClickListener(v -> startActivityForResult(new Intent(Intent.ACTION_PICK,
                CONTENT_URI), REQUEST_CODE_PICK_CONTACTS));
    }

    private void setupRecyclerView() {
        PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setExtraLayoutSpace(DeviceUtils.getScreenHeight(context));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ContactListAdapter(this, this));
        recyclerView.setItemViewCacheSize(30);
        recyclerView.setDrawingCacheEnabled(true);
    }

    private void preloadContactListImages() {
        PicassoHelper pHelper = new PicassoHelper(context);

        if (contactList != null) {
            for (int i = 0; i < contactList.size(); i++) {
                if (contactList.get(i).getBackgroundImage() != null) {
                    pHelper.preloadImages(contactList.get(i).getBackgroundImage());
                }
                if (contactList.get(i).getContactImage() != null) {
                    pHelper.preloadImages(contactList.get(i).getContactImage());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            Uri contactUri = data.getData();

            RetrieveSingleContact retrieveSingleContact = new RetrieveSingleContact(context,
                    contactUri, getContentResolver());
            Contact contact = retrieveSingleContact.createContact();
            // TODO: 6/9/17 make not null
            realmService.addContactToRealmDB(contact);
        }
    }

    @Override
    public void onContactClicked(Contact contact) {
        Intent intent = new Intent(this, ContactViewPagerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(CONTACT_ID, contact.getRealmID());
        this.startActivity(intent);
    }

    @Override
    public void onContactLongClicked(Contact contact) {
        //// TODO: 5/22/17 add functionality
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            presenter.retrieveContacts();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmService.closeRealm();
        if (receiver != null) {
            unregisterReceiver(receiver);
            Log.d(TAG, "onDestroy: unregisted receiver");
        }
    }

    @Override
    public void onContactsLoadedSuccess(RealmResults<Contact> list) throws ExecutionException,
            InterruptedException {
        refreshRecyclerView(list);
    }

    private void refreshRecyclerView(RealmResults<Contact> list) throws ExecutionException,
            InterruptedException {
        ContactListAdapter adapter = (ContactListAdapter) recyclerView.getAdapter();
        adapter.setData(list);
    }

    @Override
    public void onContactsLoadedFailure(String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    //todo migrate intent to this method
    @Override
    public void launchActivity(Contact contact) {
    }


}