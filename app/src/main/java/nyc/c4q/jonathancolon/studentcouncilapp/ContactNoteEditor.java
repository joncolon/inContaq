package nyc.c4q.jonathancolon.studentcouncilapp;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactNoteEditor extends Fragment {
    String TAG = "SET TEXT REQUEST: ";
    private EditText contactNote;
    private TextView contactName;
    private Button saveButton;
    private AlertDialog confirmDialogObject;
    private static final String MODIFIED_NOTE = "Modified Note";

    public ContactNoteEditor(){
        //required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View NotepadLayoutFragment = inflater.inflate(R.layout.fragment_contact_note_editor, container, false);
        //get widget references from layout
        saveButton = (Button) NotepadLayoutFragment.findViewById(R.id.save_button);
        contactName = (TextView) NotepadLayoutFragment.findViewById(R.id.editor_contact_name);
        contactNote = (EditText) NotepadLayoutFragment.findViewById(R.id.edit_text_notepad);
        if (savedInstanceState != null) {
            String savedNote = savedInstanceState.getString(MODIFIED_NOTE);
            Log.i("NOTES---START ----- " , savedInstanceState.getString(MODIFIED_NOTE));
            contactNote.setText(savedNote);
        }

        //inflates the Note Editor Fragment
        Intent intent = getActivity().getIntent();

            //populate widgets with data
            Bundle extras = intent.getExtras();

            if (savedInstanceState != null){
                contactNote.setText(savedInstanceState.getString(MODIFIED_NOTE));
                Log.i("NOTES--- " , savedInstanceState.getString(MODIFIED_NOTE));
            }
            else {
                String noteValue = extras.getString(MainActivity.CONTACT_NOTE_EXTRA);
                contactNote.setText(noteValue);
            }
                String nameValue = extras.getString(MainActivity.CONTACT_NAME_EXTRA);

        contactName.setText(nameValue);
                Log.e(TAG, "TEXT ADDED SUCCESSFULLY");

        buildConfirmDialog();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogObject.show();
            }
        });

        return NotepadLayoutFragment;
    }

    //This section sets up the confirm dialog box when user hits the save button
    public void buildConfirmDialog() {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());
        confirmBuilder.setTitle("Are you sure?");
        confirmBuilder.setMessage("Are you sure you want to save this note?");

        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Save Note: ", "Contact Note: " + contactNote.getText());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

        confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        confirmDialogObject = confirmBuilder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String savedNote = contactNote.getText().toString();
        outState.putString(MODIFIED_NOTE, savedNote);
        super.onSaveInstanceState(outState);
    }






}
