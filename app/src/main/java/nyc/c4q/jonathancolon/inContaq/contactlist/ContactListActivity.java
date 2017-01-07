package nyc.c4q.jonathancolon.inContaq.contactlist;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import nyc.c4q.jonathancolon.inContaq.sqlite.ContactDatabaseHelper;
import nyc.c4q.jonathancolon.inContaq.sqlite.SqlHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class ContactListActivity extends AppCompatActivity implements AlertDialogCallback<String>, ContactListAdapter.Listener {
    private static final String TAG = "DB DELETE : ";
    private RecyclerView recyclerView;
    private AlertDialog InputContactDialogObject;

    public static Activity activity;

    private SQLiteDatabase db;

    private String mText = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        this.activity = this;
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        Stetho.initializeWithDefaults(this);


        // Initializing views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Set the adapter for RecyclerView
        setupRecyclerView();
        refreshRecyclerView();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, permissions, 2);
        }

        buildInputContactDialog(this, this);
    }


    //Opens an AlertDialog to enter a new contact name
    public void buildInputContactDialog(Context context, final AlertDialogCallback<String> callback) {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
        confirmBuilder.setTitle("Add a contact");
        confirmBuilder.setMessage("Enter contact's name");

        final EditText input = new EditText(ContactListActivity.this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        confirmBuilder.setView(input);


        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Called when add button is clicked.

                Toast.makeText(ContactListActivity.this, input.getText(), Toast.LENGTH_SHORT).show();
                mText = input.getText().toString();

                callback.alertDialogCallback(mText);
            }
        });

        confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        InputContactDialogObject = confirmBuilder.create();
    }

    public void openEditor(View view) {
        InputContactDialogObject.show();
    }

    public void addContact(String firstName, String lastName) {

        // Add data locally to the list
        Contact contactToAdd = new Contact(firstName, lastName);
        //mData.add(dataToAdd);
        ContactListAdapter adapter = (ContactListAdapter) recyclerView.getAdapter();
        // Update adapter
        adapter.addItem(contactToAdd);
        adapter.notifyDataSetChanged();


    }

    @Override
    public void alertDialogCallback(String dialogFragmentText) {
        mText = dialogFragmentText;

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
            Toast.makeText(ContactListActivity.this, "Enter a valid name", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ContactListAdapter(this));
    }

    @Override
    public void onContactClicked(Contact contact) {
        Intent intent = new Intent(this, ContactViewPagerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms","ContactSmsFragment, Instance 4");


        intent.putExtra("Parcelled Contact", Parcels.wrap(contact));

        this.startActivity(intent);

    }

    public void refreshRecyclerView() {
        ContactDatabaseHelper dbHelper = ContactDatabaseHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();
        List<Contact> contacts = SqlHelper.selectAllContacts(db);
        ContactListAdapter adapter = (ContactListAdapter) recyclerView.getAdapter();

        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
            }
        });
        adapter.setData(contacts);
        Log.d(TAG, "RefreshRV : " + contacts.size());
        ;
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
}


