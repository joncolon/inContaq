package nyc.c4q.jonathancolon.inContaq.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by Danny on 12/14/2016.
 */

public class ContactDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "contact.db";
    private static final int DATABASE_VERSION = 1;
    private static ContactDatabaseHelper instance;

    public static synchronized ContactDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ContactDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public ContactDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static {
        // register our models
        cupboard().register(Contact.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }
}
