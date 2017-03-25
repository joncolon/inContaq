package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;
import nyc.c4q.jonathancolon.inContaq.utlities.NameSplitter;
import nyc.c4q.jonathancolon.inContaq.utlities.sqlite.ContactDatabaseHelper;

import static android.provider.ContactsContract.CommonDataKinds.Email;
import static android.provider.ContactsContract.CommonDataKinds.Phone;
import static android.provider.ContactsContract.Contacts;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;


class ImportContacts {

    private Context context;

    ImportContacts(Context context) {
        this.context = context;
    }

    void getContactsFromContentProvider() {
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

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        // Loop for every contact in the phone
        if (((cursor != null) ? cursor.getCount() : 0) > 0) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String contact_id = getId(_ID, cursor);
                    String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                    if (hasPhoneNumber > 0) {
                        Contact contact = new Contact();
                        String[] splitName = NameSplitter.splitFirstAndLastName(name);
                        contact.setFirstName(splitName[0]);
                        contact.setLastName(splitName[1]);

                        Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,
                                Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                        if (phoneCursor != null) {

                            while (phoneCursor.moveToNext()) {
                                phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                                String type = phoneCursor.getString(phoneCursor.getColumnIndex(TYPE));
                                String simplifiedPhoneNumber = simplifyPhoneNumber(phoneNumber);

                                if (type.equals(context.getString(R.string.type_mobile))) {
                                    contact.setCellPhoneNumber(simplifiedPhoneNumber);
                                }
                            }
                        }

                        if (phoneCursor != null) {
                            phoneCursor.close();
                        }

                        Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null,
                                EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);

                        if (emailCursor != null) {
                            while (emailCursor.moveToNext()) {
                                email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                                contact.setEmail(email);
                            }
                        }

                        if (emailCursor != null) {
                            emailCursor.close();
                        }

                        ContactDatabaseHelper dbHelper = ContactDatabaseHelper.getInstance(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        cupboard().withDatabase(db).put(contact);

                    }
                }
            }
        }
    }

    private String getId(String _ID, Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(_ID));
    }

    private String simplifyPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[()\\s-]+", "");
    }
}
