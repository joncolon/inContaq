package nyc.c4q.jonathancolon.studentcouncilapp.contactlist;


import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import nyc.c4q.jonathancolon.studentcouncilapp.R;
import nyc.c4q.jonathancolon.studentcouncilapp.sqlite.ContactDatabaseHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditContactFragment extends Fragment {
    private static final int RESULT_LOAD_BACKGROUND_IMG = 2;
    String TAG = "SET TEXT REQUEST: ";
    private static TextView contactName, smsList;
    private static int RESULT_LOAD_CONTACT_IMG = 1;
    private static ImageView contactImageIV, backgroundImageIV;
    String imgDecodableString;
    private static Contact contact;


    private SQLiteDatabase db;


    public EditContactFragment() {
        //required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View NotepadLayoutFragment = inflater.inflate(R.layout.fragment_edit_contact, container, false);

        //get widget references from layout
        contactName = (TextView) NotepadLayoutFragment.findViewById(R.id.editor_contact_name);
        contactImageIV = (ImageView) NotepadLayoutFragment.findViewById(R.id.contact_img);
        backgroundImageIV = (ImageView) NotepadLayoutFragment.findViewById(R.id.background_image);
        smsList = (TextView) NotepadLayoutFragment.findViewById(R.id.sms);

        contact = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("Parcelled Contact"));

        displayContactInfo(contact);

        getLastSmsDate();



        contactImageIV.setOnClickListener(new View.OnClickListener() {
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
                startActivityForResult(galleryIntent, RESULT_LOAD_CONTACT_IMG);
            }
        });

        backgroundImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_BACKGROUND_IMG);
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
            if (resultCode == Activity.RESULT_OK) {

                switch (requestCode) {
                    case 1:
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
                        Bitmap bitmap = decodeSampledBitmapFromFilePath(imgDecodableString, 275, 275);

                        imgView.setImageBitmap(bitmap);

                        //SAVE TO DATABASE
                        ContactDatabaseHelper dbHelper = ContactDatabaseHelper.getInstance(getActivity());
                        db = dbHelper.getWritableDatabase();

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte imageInByte[] = stream.toByteArray();
                        contact.setContactImage(imageInByte);
                        cupboard().withDatabase(db).put(contact);

                        break;

                    case 2:
                        // Get the Image from data
                        Uri selectedBackgroundImage = data.getData();
                        String[] filePathColumnBG = {MediaStore.Images.Media.DATA};


                        // Get the cursor
                        Cursor cursorBG = getActivity().getApplicationContext().getContentResolver().query(selectedBackgroundImage,
                                filePathColumnBG, null, null, null);

                        // Move to first row
                        cursorBG.moveToFirst();

                        int columnIndexBG = cursorBG.getColumnIndex(filePathColumnBG[0]);
                        imgDecodableString = cursorBG.getString(columnIndexBG);
                        cursorBG.close();
                        ImageView imgViewBG = (ImageView) getView().findViewById(R.id.background_image);


                        Bitmap bitmapBG = decodeSampledBitmapFromFilePath(imgDecodableString, 275, 275);

                        imgViewBG.setImageBitmap(bitmapBG);

                        //SAVE TO DATABASE
                        ByteArrayOutputStream streamBG = new ByteArrayOutputStream();
                        bitmapBG.compress(Bitmap.CompressFormat.JPEG, 100, streamBG);
                        byte imageInByteBG[] = streamBG.toByteArray();


                        ContactDatabaseHelper dbHelperBG = ContactDatabaseHelper.getInstance(getActivity());
                        db = dbHelperBG.getWritableDatabase();
                        contact.setBackgroundImage(imageInByteBG);
                        cupboard().withDatabase(db).put(contact);

                        break;
                }
            } else {
                Toast.makeText(getActivity(), "Photo not selected", Toast.LENGTH_LONG)
                        .show();
            }
        } catch (Exception e) {
            Log.e(TAG, "onActivityResult: " + e.toString());

            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private static void displayContactInfo(Contact contact) {
        String nameValue = contact.getFirstName() + " " + contact.getLastName();

        if (contact.getBackgroundImage() != null) {
            byte[] backgroundImage = contact.getBackgroundImage();
            ByteArrayInputStream imageStream = new ByteArrayInputStream(backgroundImage);
            Bitmap decodedImage = BitmapFactory.decodeStream(imageStream);

            backgroundImageIV.setImageBitmap(decodedImage);
        }

        if (contact.getContactImage() != null) {
            byte[] contactImage = contact.getContactImage();
            ByteArrayInputStream imageStream = new ByteArrayInputStream(contactImage);
            Bitmap decodedImage = BitmapFactory.decodeStream(imageStream);

            contactImageIV.setImageBitmap(decodedImage);
        }

        contactName.setText(nameValue);

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight, String filepath) {
        options = new BitmapFactory.Options();

        // Raw height and width of image
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        float height = options.outHeight;
        float width = options.outWidth;
        double inSampleSize = 1D;

        if (height > reqHeight || width > reqWidth) {
            int halfHeight = (int) (height / 2);
            int halfWidth = (int) (width / 2);

            // Calculate a inSampleSize that is a power of 2 - the decoder will use a value that is a power of two anyway.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return (int) inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFilePath(String filepath,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight, filepath);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }

    public void getLastSmsDate() {

        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        cursor.moveToFirst();
        String date = cursor.getString(cursor.getColumnIndex("date"));
        Long timestamp = Long.parseLong(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Date finaldate = calendar.getTime();
        String smsDate = finaldate.toString();
        Log.d(Contact.class.getName(), smsDate);

        getAllSmsFromSender();


        Toast.makeText(getActivity(), smsDate, Toast.LENGTH_SHORT).show();
    }

    public void getAllSmsFromSender() {
        StringBuilder smsBuilder = new StringBuilder();
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
            Cursor cur = getActivity().getApplicationContext().getContentResolver().query(uri, projection, "address='9172707921'", null, "date desc");
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int int_Type = cur.getInt(index_Type);

                    smsBuilder.append(strAddress + "\n");
                    smsBuilder.append(strbody + "\n");
                    smsBuilder.append(smsDateFormat(longDate));
                    smsBuilder.append("\n\n");

                    smsList.setText(smsBuilder);

                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                }
            } else {
                smsBuilder.append("no result!");
            }
        } catch (SQLiteException ex) {
        Log.d("SQLiteException", ex.getMessage());
        }
    }

    public String smsDateFormat(long timeInMilli){
        String amPm;
        String minutes;
        String hour;

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timeInMilli);
        int month = (calendar.get(Calendar.MONTH)) + 1;
        int year = (calendar.get(Calendar.YEAR));
        int dayOfMonth = (calendar.get(Calendar.DAY_OF_MONTH));
        int hourOfDay = (calendar.get(Calendar.HOUR));
        int minute = (calendar.get(Calendar.MINUTE));
        int isAMorPM = (calendar.get(Calendar.AM_PM));

        hour = hourOfDay == 0 ? "12" : String.valueOf(hourOfDay);

        switch (minute) {
            case 0: minutes = "00";
                break;
            case 1: minutes = "01";
                break;
            case 2: minutes = "02";
                break;
            case 3: minutes = "03";
                break;
            case 4: minutes = "04";
                break;
            case 5: minutes = "05";
                break;
            case 6: minutes = "06";
                break;
            case 7: minutes = "07";
                break;
            case 8: minutes = "08";
                break;
            case 9: minutes = "09";
                break;
            default: minutes = String.valueOf(minute);
        }
        amPm = isAMorPM == 0 ? "AM" : "PM";

        String formattedDate = month + "/" + dayOfMonth + "/" + year + " " + hour + ":" + minutes + " " + amPm;
        return formattedDate;
    }

}
