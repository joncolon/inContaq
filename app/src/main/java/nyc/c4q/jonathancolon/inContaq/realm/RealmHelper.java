package nyc.c4q.jonathancolon.inContaq.realm;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;
import nyc.c4q.jonathancolon.inContaq.sms.model.Sms;

/**
 * Created by jonathancolon on 5/4/17.
 */

public class RealmHelper implements io.realm.RealmModel {
    private static final String TAG = RealmHelper.class.getSimpleName();
    public static Realm getInstance() {
        Log.e(TAG, "opening realm");
        return Realm.getDefaultInstance();
    }

    public static Contact getByRealmID(Realm realm, long realmID) {
        Log.e(TAG, "getByRealmID");
        return realm.where(Contact.class).equalTo("realmID", realmID).findFirst();
    }

    public static RealmResults<Sms> getByMobileNumber(Realm realm, String mobileNumber) {
        Log.e(TAG, "getByMobileNumber");
        return realm.where(Sms.class).equalTo("address", mobileNumber).findAll().sort("time", Sort.ASCENDING);
    }

    public static void closeRealm(Realm realm) {
        if (realm != null) {
            Log.e("RealmHelper: ", "closing realm");
            realm.close();
        }

    }

    public static void clearSmsRecords(Realm realm){
        realm.executeTransaction(realm1 -> realm1.delete(Sms.class));
    }
}
