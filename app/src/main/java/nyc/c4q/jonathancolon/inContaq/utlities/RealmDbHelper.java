package nyc.c4q.jonathancolon.inContaq.utlities;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.model.Contact;


public class RealmDbHelper implements io.realm.RealmModel {
    private static final String TAG = RealmDbHelper.class.getSimpleName();

    public static Realm getInstance() {
        Log.e(TAG, "opening realm");
        return Realm.getDefaultInstance();
    }

    public static Contact getByRealmID(Realm realm, long realmID) {
        Log.e(TAG, "getByRealmID");
        return realm.where(Contact.class).equalTo("realmID", realmID).findFirst();
    }

    public static RealmResults<Contact> findByReminderEnabled(Realm realm) {
        Log.e(TAG, "getByRealmID");
        return realm.where(Contact.class).equalTo("reminderEnabled", true).findAll();
    }

    public static void closeRealm(Realm realm) {
        if (realm != null) {
            Log.e("RealmDbHelper: ", "closing realm");
            realm.close();
        }
    }

    public static void addContactToRealmDB(Realm realm, final Contact newContact) {
        Log.i("Realm: ",
                "Adding to Contact database...");

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

    public static void enableReminder(Realm realm, Contact contact) {
        realm.executeTransaction(realm1 -> contact.setReminderEnabled(true));
    }

    public static void disableReminder(Realm realm, Contact contact) {
        realm.executeTransaction(realm1 -> contact.setReminderEnabled(false));
    }
}
