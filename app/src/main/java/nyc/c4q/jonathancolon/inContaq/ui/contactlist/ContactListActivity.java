package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.smsreminder.ContactNotificationService;
import nyc.c4q.jonathancolon.inContaq.smsreminder.MyAlarmReceiver;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.ContactViewPagerActivity;
import nyc.c4q.jonathancolon.inContaq.utlities.DeviceUtils;
import nyc.c4q.jonathancolon.inContaq.utlities.NameSplitter;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmDbHelper;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

import static android.provider.ContactsContract.CommonDataKinds.Phone;
import static android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
import static android.provider.ContactsContract.Contacts.CONTENT_URI;
import static android.provider.ContactsContract.Contacts.DISPLAY_NAME;
import static android.provider.ContactsContract.Contacts._ID;

public class ContactListActivity extends AppCompatActivity implements
        ContactListAdapter.Listener {

    public static final String CONTACT_ID = "Contact";
    private static final String TAG = ContactListActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    private RecyclerView recyclerView;
    private RealmResults<Contact> contactList;
    private Context context;
    private Realm realm;
    private SharedPreferences prefs;
    private MyAlarmReceiver receiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        checkServiceCreated();

        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        context = getApplicationContext();

        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("request_permissions", true) &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startActivity(new Intent(this, PermissionActivity.class));
            finish();
            return;
        }

        initViews();
        setupRecyclerView();
        preloadContactListImages();
        prefs = getSharedPreferences("inContaq", MODE_PRIVATE);
        if (prefs.getBoolean("intro", true)) {
            if (contactList == null) {
                new MaterialTapTargetPrompt.Builder(ContactListActivity.this)
                        .setTarget(findViewById(R.id.fab_add_contact))
                        .setPrimaryText("Add your first contact")
                        .setSecondaryText("Tap here to import your first contact")
                        .setBackgroundColour(context.getColor(R.color.charcoal))
                        .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                            @Override
                            public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                prefs.edit().putBoolean("intro", false).apply();
                            }

                            @Override
                            public void onHidePromptComplete() {
                            }
                        })
                        .show();
            }

            }
        }

//    public void checkServiceCreated() {
//        if (!ContactNotificationService.hasStarted) {
//            System.out.println("Starting service...");
//            Intent intent = new Intent(getApplicationContext(), ContactNotificationService.class);
//            intent.putExtra("hasStarted", true);
//            startService(intent);
//        }
//    }

    public void checkServiceCreated() {
        if (!ContactNotificationService.hasStarted) {
            System.out.println("Starting service...");
            startService();
        }
    }

    public void startService() {
        // Sends a broadcast to MyAlarmReceiver that will start MyService
        IntentFilter filter = new IntentFilter(MyAlarmReceiver.ACTION);
        receiver = new MyAlarmReceiver();
        this.registerReceiver(receiver, filter);

        Intent intent = new Intent(MyAlarmReceiver.ACTION);
        sendBroadcast(intent);
    }

    private void initViews() {
        FloatingActionButton addContactFab = (FloatingActionButton)
                findViewById(R.id.fab_add_contact);

        addContactFab.setOnClickListener(v -> startActivityForResult(new Intent(Intent.ACTION_PICK,
                CONTENT_URI), REQUEST_CODE_PICK_CONTACTS));
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
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
            uriContact = data.getData();

            Contact contact = new Contact();
            retrieveContactName(contact);
            retrieveContactNumber(contact);
            retrieveContactEmail(contact);

            RealmDbHelper.addContactToRealmDB(realm, contact);
        }
    }

    private void retrieveContactName(Contact contact) {
        String contactName = null;
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
            cursor.close();
        }
        Log.d(TAG, "Contact Name: " + contactName);
        contact.setFirstName(NameSplitter.splitFirstAndLastName(contactName)[0]);
        contact.setLastName(NameSplitter.splitFirstAndLastName(contactName)[1]);
    }

    private void retrieveContactNumber(Contact contact) {
        String contactNumber;
        retrieveContactID();

        Log.d(TAG, "Contact ID: " + contactID);
        Cursor cursorPhone = getContentResolver().query(Phone.CONTENT_URI,
                new String[]{NUMBER},
                Phone.CONTACT_ID + " = ? AND " + TYPE + " = " + TYPE_MOBILE,
                new String[]{contactID},
                null);
        try {
            if (cursorPhone != null && cursorPhone.moveToFirst()) {
                contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(NUMBER));
                cursorPhone.close();
                Log.d(TAG, "Contact Phone Number: " + contactNumber);
                String mobileNumber = simplifyPhoneNumber(contactNumber);
                contact.setMobileNumber(mobileNumber);
            }
        } finally {
            if (cursorPhone != null) {
                cursorPhone.close();
            }
        }
    }

    private void retrieveContactEmail(Contact contact) {
        Uri EmailCONTENT_URI = Email.CONTENT_URI;
        String EmailCONTACT_ID = Email.CONTACT_ID;
        String DATA = Email.DATA;
        String email = null;

        Cursor emailCursor = getContentResolver().query(EmailCONTENT_URI, null,
                EmailCONTACT_ID + " = ?", new String[]{contactID}, null);
        if (emailCursor != null) {
            while (emailCursor.moveToNext()) {
                email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                Log.d(TAG, "Contact Email: " + email);
                contact.setEmail(email);
            }
            emailCursor.close();
        }
    }

    private String retrieveContactID() {
        try (Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{_ID},
                null, null, null)) {

            if (cursorID != null && cursorID.moveToFirst()) {
                contactID = cursorID.getString(cursorID.getColumnIndex(_ID));
                cursorID.close();
            }
        }
        return contactID;
    }

    private String simplifyPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[()\\s-]+", "");
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
            refreshRecyclerView();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void refreshRecyclerView() throws ExecutionException, InterruptedException {
        Log.e("fetching contacts", "Fetching contacts...");
        fetchAllContacts();
        ContactListAdapter adapter = (ContactListAdapter) recyclerView.getAdapter();
        adapter.setData(contactList);
    }

    private void fetchAllContacts() {
        if (realm == null) {
            realm = RealmDbHelper.getInstance();
        }
        contactList = realm.where(Contact.class).findAll().sort("firstName", Sort.ASCENDING);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RealmDbHelper.closeRealm(realm);
        if (receiver != null){
            unregisterReceiver(receiver);
            Log.d(TAG, "onDestroy: unregisted receiver");
        }
    }
}


