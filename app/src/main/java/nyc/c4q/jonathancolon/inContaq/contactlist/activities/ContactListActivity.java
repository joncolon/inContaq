package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.AlertDialogCallback;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.adapters.ContactListAdapter;
import nyc.c4q.jonathancolon.inContaq.utlities.sqlite.ContactDatabaseHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.sqlite.SqlHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class ContactListActivity extends AppCompatActivity implements AlertDialogCallback<String>,
        ContactListAdapter.Listener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String PARCELLED_CONTACT = "Parcelled Contact";
    private RecyclerView recyclerView;
    private AlertDialog InputContactDialogObject;
    private List<Contact> contactList;
    private SQLiteDatabase db;
    private String mText = "";
    private final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Stetho.initializeWithDefaults(this);

        FloatingActionButton addContactFab = (FloatingActionButton) findViewById(R.id.fab_add_contact);
        addContactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactListActivity.this.openEditor();
            }
        });

        setupRecyclerView();
        refreshRecyclerView();
        checkPermissions();
        buildInputContactDialog(this);
    }

    // POP UP for Entering a new Contact
    private void buildInputContactDialog(final AlertDialogCallback<String> callback) {

        final EditText input = new EditText(ContactListActivity.this);

        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
        confirmBuilder.setTitle(R.string.add_contact);
        confirmBuilder.setMessage(R.string.enter_name);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        confirmBuilder.setView(input);

        confirmBuilder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mText = input.getText().toString();
                callback.alertDialogCallback(mText);
            }
        });

        confirmBuilder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        InputContactDialogObject = confirmBuilder.create();
    }

    @Override
    public void alertDialogCallback(String dialogFragmentText) {
        mText = dialogFragmentText;
        splitFirstandLastName();
    }

    private void splitFirstandLastName() {
        if (mText.trim().length() > 0) {
            String name = mText;
            String lastName = "";
            String firstName;
            if (name.split("\\w+").length > 1) {
                lastName = name.substring(name.lastIndexOf(" ") + 1);
                firstName = name.substring(0, name.lastIndexOf(' '));
            } else {
                firstName = name;
            }
            Contact contact = new Contact(firstName, lastName);
            addContact(firstName, lastName);
            cupboard().withDatabase(db).put(contact);
            refreshRecyclerView();
        } else {
            Toast.makeText(ContactListActivity.this, R.string.enter_name,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void openEditor() {
        InputContactDialogObject.show();
    }

    private void addContact(String firstName, String lastName) {
        Contact contactToAdd = new Contact(firstName, lastName);
        ContactListAdapter adapter = (ContactListAdapter) recyclerView.getAdapter();
        adapter.addItem(contactToAdd);
        adapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ContactListAdapter(this, this));
    }

    @Override
    public void onContactClicked(Contact contact) {
        Intent intent = new Intent(this, ContactViewPagerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PARCELLED_CONTACT, Parcels.wrap(contact));

        this.startActivity(intent);
    }

    private void refreshRecyclerView() {
        ContactDatabaseHelper dbHelper = ContactDatabaseHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();
        contactList = SqlHelper.selectAllContacts(db);
        ContactListAdapter adapter = (ContactListAdapter) recyclerView.getAdapter();
        sortContacts();
        adapter.setData(contactList);
    }

    @Override
    public void onContactLongClicked(Contact contact) {
        cupboard().withDatabase(db).delete(contact);
        refreshRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshRecyclerView();
    }

    private void sortContacts() {
        List<Contact> contacts = contactList;
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
            }
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 3);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}


