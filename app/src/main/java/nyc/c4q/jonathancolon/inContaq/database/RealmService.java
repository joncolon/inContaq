package nyc.c4q.jonathancolon.inContaq.database;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import nyc.c4q.jonathancolon.inContaq.model.ContactModel;

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

    public synchronized ContactModel getByRealmID(long realmID) {
        Log.e(TAG, "getByRealmID");
        ContactModel contactModel = getInstance()
                .where(ContactModel.class)
                .equalTo(REALM_ID, realmID).findFirst();
        closeRealm();
        return contactModel;
    }

    public Realm getInstance() {
        return Realm.getDefaultInstance();
    }

    public void closeRealm() {
        if (!isNull(getInstance())) {
            getInstance().close();
        }
    }

    public RealmResults<ContactModel> fetchAllContacts() {
        RealmResults<ContactModel> contactModelList = getInstance()
                .where(ContactModel.class)
                .findAll()
                .sort(FIRST_NAME, Sort.ASCENDING);
        closeRealm();
        return contactModelList;
    }

    public RealmResults<ContactModel> findByReminderEnabled() {
        Log.e(TAG, "findByReminderEnabled");
        RealmResults<ContactModel> reminderEnabledList = getInstance()
                .where(ContactModel.class)
                .equalTo(REMINDER_ENABLED, true)
                .findAll()
                .sort(FIRST_NAME, Sort.ASCENDING);
        closeRealm();
        return reminderEnabledList;
    }

    public synchronized void deleteContactFromRealmDB(final ContactModel contactModel) {
        Log.i("Realm: ",
                "deleting " + contactModel.getFullName()+ " from database...");

        getInstance().executeTransaction(realm -> {
            getInstance().where(ContactModel.class)
                    .equalTo(REALM_ID, contactModel.getRealmID())
                    .findFirst()
                    .deleteFromRealm();
        });
        closeRealm();
    }

    public synchronized void addContactToRealmDB(final ContactModel newContactModel) {
        Log.i("Realm: ",
                "Adding to ContactModel database...");

        getInstance().executeTransaction(realm -> {
            long realmID = 1;

            if (realm.where(ContactModel.class).count() > 0) {
                // auto-increment id
                realmID = realm.where(ContactModel.class).max(REALM_ID).longValue() + 1;
            }
            newContactModel.setRealmID(realmID);
            Log.d("ADDING ID: ", newContactModel.getFirstName() + " " + newContactModel.getRealmID());
            Log.d("MOBILE NUMBER: ", newContactModel.getMobileNumber());
            ContactModel contactModel = realm.copyToRealmOrUpdate(newContactModel);
        });
        closeRealm();
    }

    public void toggleReminder(ContactModel contactModel, boolean isEnabled) {
        getInstance().executeTransaction(realm1 -> contactModel.setReminderEnabled(isEnabled));
        closeRealm();
    }
}
