package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import javax.inject.Inject;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import nyc.c4q.jonathancolon.inContaq.di.Injector;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.utlities.NameSplitter;
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


public class RetrieveSingleContact {

    private static final String TAG = RetrieveSingleContact.class.getSimpleName();
    @Inject
    Context context;
    private Uri contactUri;
    private ContentResolver contentResolver;
    private String contactID;

    RetrieveSingleContact(Uri uri, ContentResolver contentResolver) {
        Injector.getApplicationComponent().inject(this);
        this.contactUri = uri;
        this.contentResolver = contentResolver;
    }

    Contact createContact() {
        Contact contact = new Contact();
        retrieveContactName(contact);
        retrieveContactNumber(contact);
        retrieveContactEmail(contact);
        return contact;
    }

    private void retrieveContactName(Contact contact) {
        String contactName = null;
        Cursor cursor = contentResolver.query(contactUri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
            cursor.close();
        }

        Log.d(TAG, "Contact Name: " + contactName);

        contact.setFirstName(NameSplitter.splitFirstAndLastName(contactName)[0]);
        contact.setLastName(NameSplitter.splitFirstAndLastName(contactName)[1]);
    }

    private void retrieveContactNumber(Contact contact) {
        String contactNumber;
        retrieveContactID();

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
                PhoneNumberFormatter formatter = new PhoneNumberFormatter(context,
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
        String email = null;

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

    private String retrieveContactID() {
        try (Cursor cursorID = contentResolver.query(contactUri,
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
