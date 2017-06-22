package nyc.c4q.jonathancolon.inContaq.database;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import nyc.c4q.jonathancolon.inContaq.model.Contact;

import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isNull;


public class RealmService {

    private Context context;

    @Inject
    public RealmService(Context context) {
        this.context = context;
    }

    private static final String TAG = RealmService.class.getSimpleName();

    private RealmConfiguration realmConfiguration;

    public void setup() {
        Realm.init(context);
        if (isNull(realmConfiguration)) {
            realmConfiguration = new RealmConfiguration.Builder().build();
            Realm.setDefaultConfiguration(realmConfiguration);
        } else {
            throw new IllegalStateException("database already configured");
        }
    }

    public Realm getInstance() {
        Log.e(TAG, "opening realm");
        return Realm.getDefaultInstance();
    }

    public Contact getByRealmID(long realmID) {
        Log.e(TAG, "getByRealmID");
        return getInstance().where(Contact.class).equalTo("realmID", realmID).findFirst();
    }

    private Realm getRealmInstance() {
        return Realm.getDefaultInstance();
    }

    public RealmResults<Contact> fetchAllContacts() {
        return getRealmInstance().where(Contact.class).findAll().sort("firstName", Sort.ASCENDING);
    }

    public RealmResults<Contact> findByReminderEnabled() {
        Log.e(TAG, "getByRealmID");
        return getInstance().where(Contact.class).equalTo("reminderEnabled", true).findAll();
    }

    public void closeRealm() {
        if (isNull(getRealmInstance())) {
            Log.e("RealmService: ", "closing realm");
            getInstance().close();
        }
    }

    public void addContactToRealmDB(final Contact newContact) {
        Log.i("Realm: ",
                "Adding to Contact database...");

        getInstance().executeTransaction(realm1 -> {
            long realmID = 1;
            if (realm1.where(Contact.class).count() > 0) {
                realmID = realm1.where(Contact.class).max("realmID").longValue() + 1; // auto-increment id
            }
            newContact.setRealmID(realmID);
            Log.d("ADDING ID: ", newContact.getFirstName() + " " + newContact.getRealmID());
            Contact contact = realm1.copyToRealmOrUpdate(newContact);

        });
    }

    public void toggleReminder(Contact contact, boolean isEnabled) {
        getInstance().executeTransaction(realm1 -> contact.setReminderEnabled(isEnabled));
    }
}
