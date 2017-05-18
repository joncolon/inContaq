package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import io.realm.Realm;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.utlities.NameSplitter;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmDbHelper;

import static android.provider.ContactsContract.CommonDataKinds.Email;
import static android.provider.ContactsContract.CommonDataKinds.Phone;
import static android.provider.ContactsContract.Contacts;


class ImportContacts {
    private final ContentResolver contentResolver;
    private final String TAG = ImportContacts.class.getSimpleName();
    private String firstName;
    private String lastName;
    private String mobileNumber = null;
    private String email = null;
    private String contactID = null;

    ImportContacts(Context context) {
        contentResolver = context.getContentResolver();
    }

    void getContactsFromContentProvider() {


        Cursor cursor = getCursor();

        Realm realm = RealmDbHelper.getInstance();
        // Loop for every contact in the phone
        if (((cursor != null) ? cursor.getCount() : 0) > 0) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Log.e(TAG, "Searching Content Provider");

                    contactID = getId(cursor);
                    String name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));
                    if (hasPhoneNumber(cursor)) {
                        Contact contact = new Contact();
                        String[] splitName = NameSplitter.splitFirstAndLastName(name);
                        firstName = (splitName[0]);
                        lastName = (splitName[1]);
                        Log.e(TAG, "first name: " + firstName);

                        email = retrieveEmailAddress();
                        mobileNumber = retrieveMobileNumber();
                        Log.e(TAG, "mobile number: " + mobileNumber);

                        contact.setFirstName(firstName);
                        contact.setLastName(lastName);
                        contact.setMobileNumber(mobileNumber);
                        contact.setEmail(email);

                        addContactToRealmDB(realm, contact);


                    }
                }
            }
        }
        RealmDbHelper.closeRealm(realm);
        Log.e(TAG, "Exiting Content Provider");
    }

    private Cursor getCursor() {
        return contentResolver.query(Contacts.CONTENT_URI, null, null, null, null);
    }

    private String getId(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(Contacts._ID));
    }

    private boolean hasPhoneNumber(Cursor cursor) {
        int hasPhoneChecker = Integer.parseInt(cursor.getString(cursor.
                getColumnIndex(Contacts.HAS_PHONE_NUMBER)));
        return hasPhoneChecker > 0;
    }

    private String retrieveEmailAddress() {
        Cursor emailCursor = contentResolver.query(Email.CONTENT_URI, null,
                Email.CONTACT_ID + " = ?", new String[]{contactID}, null);

        if (emailCursor != null) {
            while (emailCursor.moveToNext()) {
                email = emailCursor.getString(emailCursor.getColumnIndex(Email.DATA));
            }
        }

        if (emailCursor != null) {
            emailCursor.close();
        }
        return email;
    }

    private String retrieveMobileNumber() {
        Cursor phoneCursor = contentResolver.query(Phone.CONTENT_URI, null,
                Phone.CONTACT_ID + " = ?", new String[]{contactID}, null);
        String phoneNumber = null;
        if (phoneCursor != null) {

            while (phoneCursor.moveToNext()) {
                phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NUMBER));
                String type = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.TYPE));
                String mobileNumber = simplifyPhoneNumber(phoneNumber);

                if (isMobilePhone(type)) {
                    return mobileNumber;
                }
            }
            phoneCursor.close();
            return simplifyPhoneNumber(phoneNumber);
        }
        return null;
    }

    private void addContactToRealmDB(Realm realm, final Contact newContact) {
        Log.i("Realm: ",
                "Adding to Contact database...");


        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.executeTransaction(realm1 -> {
            long realmID = 1;
            if (realm1.where(Contact.class).count() > 0) {
                realmID = realm1.where(Contact.class).max("realmID").longValue() + 1; // auto-increment id
            }
            newContact.setRealmID(realmID);
            Log.d("ADDING ID: ", newContact.getFirstName() + " " + newContact.getRealmID());
            Contact contact = realm1.copyToRealmOrUpdate(newContact);

        });
    }

    private String simplifyPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[()\\s-]+", "");
    }

    private boolean isMobilePhone(String type) {
        return type.equals("2");
    }
}
