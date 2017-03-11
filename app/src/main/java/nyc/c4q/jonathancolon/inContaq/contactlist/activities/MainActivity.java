package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.jonathancolon.inContaq.NameSplitter;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.utlities.sqlite.ContactDatabaseHelper;

import static android.provider.ContactsContract.CommonDataKinds.Email;
import static android.provider.ContactsContract.CommonDataKinds.Phone;
import static android.provider.ContactsContract.Contacts;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends Activity {
    private final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS};
    public TextView outputText;
    private SQLiteDatabase db;
    private ImageView profileImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_contacts);

        initViews();
        checkPermissions();
        fetchContacts();
    }

    private void initViews() {
        outputText = (TextView) findViewById(R.id.textView1);
        profileImage = (ImageView) findViewById(R.id.profile_image);
    }

    synchronized private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 3);
        }
    }

    synchronized public void fetchContacts() {
        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = Contacts.CONTENT_URI;
        String _ID = Contacts._ID;
        String DISPLAY_NAME = Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = Phone.CONTENT_URI;
        String Phone_CONTACT_ID = Phone.CONTACT_ID;
        String NUMBER = Phone.NUMBER;
        String TYPE = Phone.TYPE;

        Uri EmailCONTENT_URI = Email.CONTENT_URI;
        String EmailCONTACT_ID = Email.CONTACT_ID;
        String DATA = Email.DATA;

        StringBuffer output = new StringBuffer();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contact_id = getContactId(_ID, cursor);
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {

                    Contact contact = new Contact();
                    NameSplitter nameSplitter = new NameSplitter();
                    String[] splitName = nameSplitter.splitFirstAndLastName(name);
                    contact.setFirstName(splitName[0]);
                    contact.setLastName(splitName[1]);
                    output.append("\nName: " + contact.getFirstName() + contact.getLastName());

                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        String type = phoneCursor.getString(phoneCursor.getColumnIndex(TYPE));
                        String simplifiedPhoneNumber = simplifyPhoneNumber(phoneNumber);

                        if (type.equals(getString(R.string.type_mobile))) {
                            contact.setCellPhoneNumber(simplifiedPhoneNumber);
                            output.append("\nType:" + type + "\nPhone number:" + simplifiedPhoneNumber);
                        }
                    }
                    phoneCursor.close();
                    // Query and loop for every email of the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null,
                            EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (emailCursor.moveToNext()) {
                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                        output.append("\nEmail:" + email);
                        contact.setEmail(email);
                    }
                    ContactDatabaseHelper dbHelper = ContactDatabaseHelper.getInstance(this);
                    db = dbHelper.getWritableDatabase();
                    cupboard().withDatabase(db).put(contact);

                    emailCursor.close();
                }
                output.append("\n");
            }
            outputText.setText(output);
        }
    }

    private String getContactId(String _ID, Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(_ID));
    }

    private String simplifyPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[()\\s-]+", "");
    }

    private List<ContactItems> displayContacts() {

        List<ContactItems> contactItemsList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phoneNo = "";

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(Phone.CONTENT_URI, null,
                            Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        phoneNo += pCur.getString(pCur.getColumnIndex(Phone.NUMBER)) + (pCur.isLast() ? "" : "\n");
                    }

                    contactItemsList.add(new ContactItems(id, name, phoneNo));
                    pCur.close();
                }
            }
        }
        return contactItemsList;
    }
}
