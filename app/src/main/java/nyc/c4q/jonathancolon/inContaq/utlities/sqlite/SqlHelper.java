package nyc.c4q.jonathancolon.inContaq.utlities.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;


public class SqlHelper {

    public SqlHelper() {
    }

    public static List<Contact> selectAllContacts(SQLiteDatabase db) {
        List<Contact> contacts = new ArrayList<>();
        try {
            QueryResultIterable<Contact> itr = cupboard().withDatabase(db).query(Contact.class).query();
            for (Contact contact : itr) {
                contacts.add(contact);
            }
            itr.close();
        } catch (Exception e) {
            Log.e("Contact List", "selectAllContacts: ", e);
        }
        return contacts;
    }

    public static void saveToDatabase(Contact contact, Context context){
        ContactDatabaseHelper dbHelper = ContactDatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cupboard().withDatabase(db).put(contact);
        db.close();
    }


}
