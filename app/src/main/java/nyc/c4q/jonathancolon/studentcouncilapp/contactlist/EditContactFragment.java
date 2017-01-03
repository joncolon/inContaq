package nyc.c4q.jonathancolon.studentcouncilapp.contactlist;


import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nyc.c4q.jonathancolon.studentcouncilapp.R;
import nyc.c4q.jonathancolon.studentcouncilapp.smsreader.Sms;
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
    Cursor c;

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

        //retrieve intent data
        Intent intent = getActivity().getIntent();
        contact = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("Parcelled Contact"));

        displayContactInfo(contact);


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
                                2);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_BACKGROUND_IMG);
            }
        });

        checkReadSmsPermissions();

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

            if (resultCode == Activity.RESULT_OK){

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
                        Bitmap bitmap = decodeSampledBitmapFromResource(imgDecodableString, 300, 300);

                        imgView.setImageBitmap(bitmap);


                        //SAVE TO DATABASE
                        ContactDatabaseHelper dbHelper = ContactDatabaseHelper.getInstance(getActivity());
                        db = dbHelper.getWritableDatabase();

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte imageInByte[] = stream.toByteArray();

                        contact.setContactImage(imageInByte);
                        cupboard().withDatabase(db).put(contact);



                        Toast.makeText(getActivity(), String.valueOf(Activity.RESULT_OK), Toast.LENGTH_SHORT).show();
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


                        Bitmap bitmapBG = decodeSampledBitmapFromResource(imgDecodableString, 300, 300);

                        imgViewBG.setImageBitmap(bitmapBG);

                        Toast.makeText(getActivity(), bitmapBG.getHeight() + " " + bitmapBG.getWidth() + " " + bitmapBG.getDensity(), Toast.LENGTH_LONG).show();

                        //SAVE TO DATABASE
                        ByteArrayOutputStream streamBG = new ByteArrayOutputStream();
                        bitmapBG.compress(Bitmap.CompressFormat.JPEG, 100, streamBG);
                        byte imageInByteBG[] = streamBG.toByteArray();


                        ContactDatabaseHelper dbHelperBG = ContactDatabaseHelper.getInstance(getActivity());
                        db = dbHelperBG.getWritableDatabase();
                        contact.setBackgroundImage(imageInByteBG);
                        cupboard().withDatabase(db).put(contact);

                        Toast.makeText(getActivity(), String.valueOf(resultCode), Toast.LENGTH_SHORT).show();
                        break;
                }
            } else{
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

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight, String filepath) {
        options = new BitmapFactory.Options();
        // Raw height and width of image

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
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

    public static Bitmap decodeSampledBitmapFromResource(String filepath,
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

    public void checkReadSmsPermissions() {
        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_SMS);

        ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_SMS},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }




        }


    }

    public void getLastSmsDate(){

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


        getAllSms("inbox");

        List<String> messages = new ArrayList<>();

        List<Sms> smsStuff = getAllSms("inbox");

        for (int i = 0; i < smsStuff.size(); i++) {
            messages.add(smsStuff.get(i).getMsg());
        }
        String text = "";
        for (int i = 0; i < messages.size(); i++) {

            text += (messages.get(i) + " \n");
            smsList.setText(text);
        }


        Toast.makeText(getActivity(), smsDate, Toast.LENGTH_SHORT).show();
    }

    public void getAllSmsFromSender() {
        Uri uri = Uri.parse("content://sms/");

        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();

        String phoneNumber = "19172707921";
        String sms = "address='" + phoneNumber + "'";
        Cursor cursor = contentResolver.query(uri, new String[]{"_id", "body"}, sms, null, null);

        if (cursor != null) {
            smsList.setText(String.valueOf(cursor.getCount()));
        }

        while (cursor.moveToNext()) {
            String strbody = cursor.getString(cursor.getColumnIndex("body"));
            smsList.setText(strbody);
        }
    }

        public List<Sms> getAllSms(String folderName) {


            List<Sms> lstSms = new ArrayList<Sms>();
            Sms objSms = new Sms();
            Uri message = Uri.parse("content://sms/"+folderName);
            ContentResolver cr = getActivity().getApplicationContext().getContentResolver();


            c = cr.query(message, null, null, null, null);
            getActivity().startManagingCursor(c);
            int totalSMS = c.getCount();

            if (c.moveToFirst()) {
                for (int i = 0; i < totalSMS; i++) {

                    objSms = new Sms();
                    objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                    objSms.setAddress(c.getString(c
                            .getColumnIndexOrThrow("address")));
                    objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                    objSms.setReadState(c.getString(c.getColumnIndex("read")));
                    objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));

                    lstSms.add(objSms);
                    c.moveToNext();
                }
            }
            if (!c.isClosed()) {
                c.close();
                c = null;
            }

            return lstSms;
        }


    @Override
    public void onPause() {
        super.onPause();
        if (c != null) {
            c.close();
        }
        if (db != null) {
            db.close();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (c != null) {
            c.close();
        }
        if (db != null) {
            db.close();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (c != null) {
            c.close();
        }
        if (db != null) {
            db.close();
        }
    }
    }
