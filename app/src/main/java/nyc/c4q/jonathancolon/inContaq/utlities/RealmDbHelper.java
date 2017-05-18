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
}
