package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import javax.inject.Inject;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import nyc.c4q.jonathancolon.inContaq.model.ContactModel;
import nyc.c4q.jonathancolon.inContaq.utlities.NameFormatter;
import nyc.c4q.jonathancolon.inContaq.utlities.PhoneNumberFormatter;

import static android.provider.ContactsContract.CommonDataKinds.Email;
import static android.provider.ContactsContract.CommonDataKinds.Email.CONTACT_ID;
import static android.provider.ContactsContract.CommonDataKinds.Email.CONTENT_URI;
import static android.provider.ContactsContract.CommonDataKinds.Phone;
import static android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
import static android.provider.ContactsContract.Contacts.DISPLAY_NAME;
import static android.provider.ContactsContract.Contacts._ID;


public class ContactReader {

    private static final String TAG = ContactReader.class.getSimpleName();
    private static final int FIRST_NAME = 0;
    private static final int LAST_NAME = 1;
    private ContentResolver contentResolver;
    private Context context;
    private String contactID;

    @Inject
    ContactReader(ContentResolver contentResolver, Context context) {
        this.contentResolver = contentResolver;
        this.context = context;
    }

    ContactModel retrieveContact(Uri uri) {
        ContactModel contactModel = new ContactModel();
        retrieveContactName(contactModel, uri);
        retrieveContactNumber(contactModel, uri);
        retrieveContactEmail(contactModel);
        return contactModel;
    }

    private void retrieveContactName(ContactModel contactModel, Uri uri) {
        String contactName = null;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
            cursor.close();
        }

        Log.d(TAG, "ContactModel Name: " + contactName);

        contactModel.setFirstName(NameFormatter.splitFirstAndLastName(contactName)[FIRST_NAME]);
        contactModel.setLastName(NameFormatter.splitFirstAndLastName(contactName)[LAST_NAME]);
    }

    private void retrieveContactNumber(ContactModel contactModel, Uri uri) {
        String contactNumber;
        retrieveContactID(uri);

        Log.d(TAG, "ContactModel ID: " + contactID);
        Cursor cursorPhone = contentResolver.query(Phone.CONTENT_URI,
                new String[]{NUMBER},
                Phone.CONTACT_ID + " = ? AND " + TYPE + " = " + TYPE_MOBILE,
                new String[]{contactID},
                null);

        try {
            if (cursorPhone != null && cursorPhone.moveToFirst()) {
                contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(NUMBER));
                cursorPhone.close();

                Log.d(TAG, "ContactModel Phone Number: " + contactNumber);
                PhoneNumberFormatter formatter = new PhoneNumberFormatter(
                        PhoneNumberUtil.createInstance(context));
                String mobileNumber = formatter.formatPhoneNumber(contactNumber);
                contactModel.setMobileNumber(mobileNumber);
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
        } finally {
            if (cursorPhone != null) {
                cursorPhone.close();
            }
        }
    }

    private void retrieveContactEmail(ContactModel contactModel) {
        String DATA = Email.DATA;
        String email;

        Cursor emailCursor = contentResolver.query(CONTENT_URI, null,
                CONTACT_ID + " = ?", new String[]{contactID}, null);
        if (emailCursor != null) {
            while (emailCursor.moveToNext()) {
                email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                Log.d(TAG, "ContactModel Email: " + email);
                contactModel.setEmail(email);
            }
            emailCursor.close();
        }
    }

    private String retrieveContactID(Uri uri) {
        try (Cursor cursorID = contentResolver.query(uri,
                new String[]{_ID},
                null, null, null)) {

            if (cursorID != null && cursorID.moveToFirst()) {
                contactID = cursorID.getString(cursorID.getColumnIndex(_ID));
                cursorID.close();
            }
        }
        return contactID;
    }
}
