package nyc.c4q.jonathancolon.studentcouncilapp.contactlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import java.util.List;

import nyc.c4q.jonathancolon.studentcouncilapp.FragmentView;
import nyc.c4q.jonathancolon.studentcouncilapp.R;
import nyc.c4q.jonathancolon.studentcouncilapp.sqlite.ContactDatabaseHelper;
import nyc.c4q.jonathancolon.studentcouncilapp.sqlite.SqlHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class ContactListActivity extends AppCompatActivity implements AlertDialogCallback<String>, ContactsAdapter.Listener {
    private static final String TAG = "DB DELETE : ";
    private RecyclerView recyclerView;
    private AlertDialog InputContactDialogObject;
    private FloatingActionButton addContactFab;

    public static Activity activity;

    private SQLiteDatabase db;


    public static final String CONTACT_NAME_EXTRA = "Contact Name";
    public static final String CONTACT_IMAGE_EXTRA = "Contact Image";

    private String mText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activity = this;

        Stetho.initializeWithDefaults(this);



        // Initializing views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Set the adapter for RecyclerView
        setupRecyclerView();
        refreshRecyclerView();

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
        ContactsAdapter adapter = (ContactsAdapter) recyclerView.getAdapter();
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
        recyclerView.setAdapter(new ContactsAdapter(this));
    }

    @Override
    public void onContactClicked(Contact contact) {
        Intent intent = new Intent(this, FragmentView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        intent.putExtra("Parcelled Contact", Parcels.wrap(contact));

        this.startActivity(intent);

    }

    public void refreshRecyclerView() {
        ContactDatabaseHelper dbHelper = ContactDatabaseHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();
        List<Contact> contacts = SqlHelper.selectAllContacts(db);
        ContactsAdapter adapter = (ContactsAdapter) recyclerView.getAdapter();
        adapter.setData(contacts);
        Log.d(TAG, "RefreshRV : " + contacts.size());;
    }

    @Override
    public void onContactLongClicked(Contact contact) {
        cupboard().withDatabase(db).delete(contact);
        refreshRecyclerView();
    }




}


