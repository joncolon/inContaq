package nyc.c4q.jonathancolon.studentcouncilapp;


import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;

import nyc.c4q.jonathancolon.studentcouncilapp.contactlist.Contact;
import nyc.c4q.jonathancolon.studentcouncilapp.sqlite.ContactDatabaseHelper;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactNoteEditor extends Fragment {
    String TAG = "SET TEXT REQUEST: ";
    private TextView contactName;
    private Button saveButton;
    private static int RESULT_LOAD_IMG = 1;
    private ImageView contactImageTV, backgroundImageTV;
    String imgDecodableString;
    Contact contact;

    private SQLiteDatabase db;


    public ContactNoteEditor() {
        //required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View NotepadLayoutFragment = inflater.inflate(R.layout.fragment_contact_note_editor, container, false);
        //get widget references from layout
        saveButton = (Button) NotepadLayoutFragment.findViewById(R.id.save_button);
        contactName = (TextView) NotepadLayoutFragment.findViewById(R.id.editor_contact_name);
        contactImageTV = (ImageView) NotepadLayoutFragment.findViewById(R.id.contact_img);
        backgroundImageTV = (ImageView) NotepadLayoutFragment.findViewById(R.id.background_image);


        //inflates the Note Editor Fragment
        Intent intent = getActivity().getIntent();

        //populate widgets with data
        Bundle extras = intent.getExtras();

        extras.getBundle("Parcelled Contact");

        contact = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("Parcelled Contact"));





        String nameValue = contact.getFirstName();
        Integer contactImage = contact.getContactImage();
        Integer backgroundImage = contact.getBackgroundImage();
        contactImageTV.setImageResource(contactImage);
        backgroundImageTV.setImageResource(backgroundImage);
        contactName.setText(nameValue);
        Log.e(TAG, "TEXT ADDED SUCCESSFULLY");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Create intent to Open Image applications like Gallery, Google Photos

                // Assume thisActivity is the current activity
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        return NotepadLayoutFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) getView().findViewById(R.id.contact_img);

                // Set the Image in ImageView after decoding the String
                Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
                BitmapDrawable replacementImage = new BitmapDrawable(getResources(), bitmap);

                imgView.setImageDrawable(replacementImage);

                ContactDatabaseHelper dbHelper = ContactDatabaseHelper.getInstance(getActivity());
                db = dbHelper.getWritableDatabase();




            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "onActivityResult: " + e.toString());

            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void insertImg(int id , Bitmap img ) {


        byte[] data = getBitmapAsByteArray(img); // this is a function

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
