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

    private static final String TAG = RealmService.class.getSimpleName();
    private static final String REALM_ID = "realmID";
    private static final String REMINDER_ENABLED = "reminderEnabled";
    private static final String FIRST_NAME = "firstName";
    private Context context;
    private RealmConfiguration realmConfiguration;

    @Inject
    public RealmService(Context context) {
        this.context = context;
    }

    public void setup() {
        Realm.init(context);
        if (isNull(realmConfiguration)) {
            realmConfiguration = new RealmConfiguration.Builder().build();
            Realm.setDefaultConfiguration(realmConfiguration);
        } else {
            throw new IllegalStateException("database already configured");
        }
    }

    public synchronized Contact getByRealmID(long realmID) {
        Log.e(TAG, "getByRealmID");
        Contact contact = getInstance()
                .where(Contact.class)
                .equalTo(REALM_ID, realmID).findFirst();
        closeRealm();
        return contact;
    }

    public Realm getInstance() {
        return Realm.getDefaultInstance();
    }

    public void closeRealm() {
        if (!isNull(getInstance())) {
            getInstance().close();
        }
    }

    public RealmResults<Contact> fetchAllContacts() {
        RealmResults<Contact> contactList = getInstance()
                .where(Contact.class)
                .findAll()
                .sort(FIRST_NAME, Sort.ASCENDING);
        closeRealm();
        return contactList;
    }

    public RealmResults<Contact> findByReminderEnabled() {
        Log.e(TAG, "findByReminderEnabled");
        RealmResults<Contact> reminderEnabledList = getInstance()
                .where(Contact.class)
                .equalTo(REMINDER_ENABLED, true)
                .findAll()
                .sort(FIRST_NAME, Sort.ASCENDING);
        closeRealm();
        return reminderEnabledList;
    }

    public synchronized void deleteContactFromRealmDB(final Contact contact) {
        Log.i("Realm: ",
                "deleting " + contact.getFullName()+ " from database...");

        getInstance().executeTransaction(realm -> {
            getInstance().where(Contact.class)
                    .equalTo(REALM_ID, contact.getRealmID())
                    .findFirst()
                    .deleteFromRealm();
        });
        closeRealm();
    }

    public synchronized void addContactToRealmDB(final Contact newContact) {
        Log.i("Realm: ",
                "Adding to Contact database...");

        getInstance().executeTransaction(realm -> {
            long realmID = 1;

            if (realm.where(Contact.class).count() > 0) {
                // auto-increment id
                realmID = realm.where(Contact.class).max(REALM_ID).longValue() + 1;
            }
            newContact.setRealmID(realmID);
            Log.d("ADDING ID: ", newContact.getFirstName() + " " + newContact.getRealmID());
            Log.d("MOBILE NUMBER: ", newContact.getMobileNumber());
            Contact contact = realm.copyToRealmOrUpdate(newContact);
        });
        closeRealm();
    }

    public void toggleReminder(Contact contact, boolean isEnabled) {
        getInstance().executeTransaction(realm1 -> contact.setReminderEnabled(isEnabled));
        closeRealm();
    }
}
