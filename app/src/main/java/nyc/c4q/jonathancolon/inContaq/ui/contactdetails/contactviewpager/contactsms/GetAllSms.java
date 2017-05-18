package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactsms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

import io.realm.Realm;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmDbHelper;


public class GetAllSms {

    private static final String URI_ALL = "content://sms/";
    private static final String ADDRESS = "address";
    private static final String BODY = "body";
    private static final String DATE = "date";
    private static final String TYPE = "type";
    private static final String DATE_DESC = "date desc";
    private final Context context;
    private long realmID;
    private ArrayList<Sms> smsList;
    private ContentResolver contentResolver;

    public GetAllSms(Context context, long realmID) {
        this.context = context;
        this.realmID = realmID;
    }

    public ArrayList<Sms> getAllSms() {
        Sms smsObject;

        Realm realm = RealmDbHelper.getInstance();
        Contact contact = RealmDbHelper.getByRealmID(realm, realmID);

        if (contact.getMobileNumber() != null) {
            smsList = new ArrayList<>();

            if (context != null) {
                Log.e("RXJAVA", "getAllSms: ATTEMPTING TO GET SMS");

                contentResolver = context.getApplicationContext().getContentResolver();
                Uri uri = Uri.parse(URI_ALL);
                String[] projection = new String[]{ADDRESS, BODY, DATE, TYPE};
                Cursor cursor = contentResolver.query(uri,
                        projection, ADDRESS + "='" + contact.getMobileNumber() + "'", null, DATE_DESC);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        int totalSMS = cursor.getCount();
                        for (int i = 0; i < totalSMS; i++) {
                            smsObject = new Sms();
                            smsObject.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS)).replaceAll("\\s+", ""));
                            smsObject.setMsg(cursor.getString(cursor.getColumnIndexOrThrow(BODY)));
                            smsObject.setTime(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                            smsObject.setType(cursor.getString(cursor.getColumnIndexOrThrow(TYPE)));
//                            addSmsToRealmDB(realm, smsObject);
                            smsList.add(smsObject);
                            cursor.moveToNext();
                        }
                        if (!cursor.isClosed()) {
                            cursor.close();
                        }
                    }
                }
            }
            RealmDbHelper.closeRealm(realm);
        }
        return smsList;
    }

//    private static void addSmsToRealmDB(Realm realm, final Sms smsObject) {
//        realm.executeTransaction(realm1 -> {
//            long realmId = 1;
//            // TODO: 5/8/17 CHECK TO SEE IF SMS ID EXISTS IN DATABASE
//            if (realm1.where(Sms.class).count() > 0) {
//                realmId = realm1.where(Sms.class).max("realmId").longValue() + 1; // auto-increment id
//            }
//            Log.e("RXJAVA", "getAllSms: " + smsObject.getMsg());
//            smsObject.setRealmId(realmId);
//            realm1.copyToRealmOrUpdate(smsObject);
//        });
//    }
}
