package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.smsreminder.ContactNotificationService;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.ContactViewPagerActivity;
import nyc.c4q.jonathancolon.inContaq.utlities.DeviceUtils;
import nyc.c4q.jonathancolon.inContaq.utlities.NameSplitter;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmDbHelper;

import static android.provider.ContactsContract.CommonDataKinds.Phone;
import static android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
import static android.provider.ContactsContract.Contacts.CONTENT_URI;
import static android.provider.ContactsContract.Contacts.DISPLAY_NAME;
import static android.provider.ContactsContract.Contacts._ID;

public class ContactListActivity extends AppCompatActivity implements AlertDialogCallback<String>,
        ContactListAdapter.Listener {

    public static final String CONTACT_ID = "Contact";
    private static final String TAG = ContactListActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    private RecyclerView recyclerView;
    private AlertDialog InputContactDialogObject;
    private RealmResults<Contact> contactList;
    private String name = "";
    private Context context;
    private Realm realm;


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
        buildEnterContactDialog(this);
    }

    public void checkServiceCreated() {
        if (!ContactNotificationService.hasStarted) {
            System.out.println("Starting service...");
            Intent intent = new Intent(getApplicationContext(), ContactNotificationService.class);
            intent.putExtra("hasStarted", true);
            startService(intent);
        }
    }

    private void initViews() {
        TextView importContactsTV = (TextView) findViewById(R.id.import_contacts);
        FloatingActionButton addContactFab = (FloatingActionButton) findViewById(R.id.fab_add_contact);

        importContactsTV.setOnClickListener(v -> {
            ImportContacts importContacts = new ImportContacts(context);
            importContacts.getContactsFromContentProvider();
            try {
                ContactListActivity.this.refreshRecyclerView();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        addContactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
            }
        });
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

    private void buildEnterContactDialog(final AlertDialogCallback<String> callback) {
        final EditText input = new EditText(ContactListActivity.this);

        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
        confirmBuilder.setTitle(R.string.add_contact);
        confirmBuilder.setMessage(R.string.enter_name);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        confirmBuilder.setView(input);


        confirmBuilder.setPositiveButton(R.string.positive_button, (dialog, which) -> {
            name = input.getText().toString();
            try {
                callback.alertDialogCallback(name);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        confirmBuilder.setNegativeButton(R.string.negative_button, (dialog, which) -> {
            //do nothing
        });
        InputContactDialogObject = confirmBuilder.create();
    }

    private void refreshRecyclerView() throws ExecutionException, InterruptedException {
        Log.e("fetching contacts", "Fetching contacts...");
        fetchAllContacts();
        ContactListAdapter adapter = (ContactListAdapter) recyclerView.getAdapter();
//        sortByName();
        adapter.setData(contactList);

    }

    private void fetchAllContacts() {
        if (realm == null) {
            realm = RealmDbHelper.getInstance();
        }
        contactList = realm.where(Contact.class).findAll().sort("firstName", Sort.ASCENDING);
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

        // querying contact data store
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

        String contactNumber = null;

        retrieveContactID();

        Log.d(TAG, "Contact ID: " + contactID);
        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(Phone.CONTENT_URI,
                new String[]{NUMBER},
                Phone.CONTACT_ID + " = ? AND " + TYPE + " = " + TYPE_MOBILE,
                new String[]{contactID},
                null);

        if (cursorPhone != null && cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(NUMBER));
            cursorPhone.close();
            Log.d(TAG, "Contact Phone Number: " + contactNumber);
            contact.setMobileNumber(contactNumber);
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
        }

        if (emailCursor != null) {
            emailCursor.close();
        }
    }

    private String retrieveContactID() {
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{_ID},
                null, null, null);

        if (cursorID != null && cursorID.moveToFirst()) {


            contactID = cursorID.getString(cursorID.getColumnIndex(_ID));
        }

        if (cursorID != null) {
            cursorID.close();
        }

        return contactID;
    }

    private void openEditor() {
        InputContactDialogObject.show();
    }

    private void sortByName() {
        List<Contact> contacts = contactList;
        Collections.sort(contacts, (o1, o2) -> o1.getFirstName().compareToIgnoreCase(o2.getFirstName()));
    }

    @Override
    public void alertDialogCallback(String userInput) throws ExecutionException, InterruptedException {
        name = userInput;
        if (!isEmptyString(name)) {
            String[] splitName = NameSplitter.splitFirstAndLastName(name);

            Contact contact = new Contact();
            contact.setFirstName(splitName[0]);
            contact.setLastName(splitName[1]);
            // TODO: 5/6/17 add contact to realm
            refreshRecyclerView();
        } else {
            Toast.makeText(ContactListActivity.this, R.string.enter_name,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmptyString(String string) {
        return string.trim().length() < 0;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) { // guard against weird low-budget phones
            realm.close();
        } // Remember to close Realm when done.
    }
}


