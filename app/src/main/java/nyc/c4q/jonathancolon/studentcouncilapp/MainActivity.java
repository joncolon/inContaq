package nyc.c4q.jonathancolon.studentcouncilapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AlertDialogCallback<String> {
    public static List<Contacts> contactsList;
    private RecyclerView recyclerView;
    private Context mContext;
    private ImageButton mButtonAdd;
    private AlertDialog InputContactDialogObject;
    private ContactsAdapter mAdapter;

    public static final String CONTACT_NAME_EXTRA = "Contact Name";
    public static final String CONTACT_NOTE_EXTRA = "Contact Note";
    public static final String CONTACT_IMAGE_EXTRA = "Contact Image";

    private String mText = "";
    private List<Data> mData = new ArrayList<>();
    private SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing views
        mButtonAdd = (ImageButton) findViewById(R.id.btn_add);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mContext = getApplicationContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ContactsAdapter(mData, mContext);

        // Set the adapter for RecyclerView
        recyclerView.setAdapter(mAdapter);
        buildInputContactDialog(this, this);
    }


    //Opens an AlertDialog to enter a new contact name
    public void buildInputContactDialog(Context context, final AlertDialogCallback<String> callback) {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
        confirmBuilder.setTitle("Add a contact");
        confirmBuilder.setMessage("Enter contact's name");

        final EditText input = new EditText(MainActivity.this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        confirmBuilder.setView(input);


        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Called when add button is clicked.

                Toast.makeText(MainActivity.this, input.getText(), Toast.LENGTH_SHORT).show();
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

    public void addContact() {

        // Add data locally to the list
        Data dataToAdd = new Data(mText);
        //mData.add(dataToAdd);

        // Update adapter
        mAdapter.addItem(dataToAdd);
        Toast.makeText(mContext,mData.get(mData.size()-1).toString()+"THUA", Toast.LENGTH_SHORT).show();
        mAdapter.notifyDataSetChanged();

    }

    //FOR SOME REASON IT'S ADDING TWO ITEMS TO MY LIST
    @Override
    public void alertDialogCallback(String ret) {
        mText = ret;
        addContact();
    }
}


