package nyc.c4q.jonathancolon.studentcouncilapp.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.qbusict.cupboard.QueryResultIterable;
import nyc.c4q.jonathancolon.studentcouncilapp.contactlist.Contact;

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

    public static Contact getRandomContact(SQLiteDatabase db) {
        Random rand = new Random();
        Contact contact = cupboard().withDatabase(db).get(Contact.class, rand.nextInt(20));
        return contact;
    }
}
