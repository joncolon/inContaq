package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.ContactViewPagerActivity;
import nyc.c4q.jonathancolon.inContaq.utlities.DeviceUtils;
import nyc.c4q.jonathancolon.inContaq.utlities.NameSplitter;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmDbHelper;

public class ContactListActivity extends AppCompatActivity implements AlertDialogCallback<String>,
        ContactListAdapter.Listener {

    public static final String CONTACT_ID = "Contact";
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

//        checkServiceCreated();

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

        addContactFab.setOnClickListener(v -> ContactListActivity.this.openEditor());
    }

//    public void checkServiceCreated() {
//        if (!ContactNotificationService.hasStarted) {
//            System.out.println("Starting service...");
//            Intent intent = new Intent(getApplicationContext(), ContactNotificationService.class);
//            startService(intent);
//        }
//    }

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

    private void openEditor() {
        InputContactDialogObject.show();
    }

    private void fetchAllContacts() {
        if (realm == null) {
            realm = RealmDbHelper.getInstance();
        }
        contactList = realm.where(Contact.class).findAll().sort("firstName", Sort.ASCENDING);
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


