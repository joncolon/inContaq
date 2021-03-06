package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import javax.inject.Inject;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
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

    Contact retrieveContact(Uri uri) {
        Contact contact = new Contact();
        retrieveContactName(contact, uri);
        retrieveContactNumber(contact, uri);
        retrieveContactEmail(contact);
        return contact;
    }

    private void retrieveContactName(Contact contact, Uri uri) {
        String contactName = null;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
            cursor.close();
        }

        Log.d(TAG, "Contact Name: " + contactName);

        contact.setFirstName(NameFormatter.splitFirstAndLastName(contactName)[FIRST_NAME]);
        contact.setLastName(NameFormatter.splitFirstAndLastName(contactName)[LAST_NAME]);
    }

    private void retrieveContactNumber(Contact contact, Uri uri) {
        String contactNumber;
        retrieveContactID(uri);

        Log.d(TAG, "Contact ID: " + contactID);
        Cursor cursorPhone = contentResolver.query(Phone.CONTENT_URI,
                new String[]{NUMBER},
                Phone.CONTACT_ID + " = ? AND " + TYPE + " = " + TYPE_MOBILE,
                new String[]{contactID},
                null);

        try {
            if (cursorPhone != null && cursorPhone.moveToFirst()) {
                contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(NUMBER));
                cursorPhone.close();

                Log.d(TAG, "Contact Phone Number: " + contactNumber);
                PhoneNumberFormatter formatter = new PhoneNumberFormatter(
                        PhoneNumberUtil.createInstance(context));
                String mobileNumber = formatter.formatPhoneNumber(contactNumber);
                contact.setMobileNumber(mobileNumber);
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
        } finally {
            if (cursorPhone != null) {
                cursorPhone.close();
            }
        }
    }

    private void retrieveContactEmail(Contact contact) {
        String DATA = Email.DATA;
        String email;

        Cursor emailCursor = contentResolver.query(CONTENT_URI, null,
                CONTACT_ID + " = ?", new String[]{contactID}, null);
        if (emailCursor != null) {
            while (emailCursor.moveToNext()) {
                email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                Log.d(TAG, "Contact Email: " + email);
                contact.setEmail(email);
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
