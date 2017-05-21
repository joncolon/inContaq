package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactsms.data;

import android.content.ContentResolver;
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
    private long realmID;
    private ArrayList<Sms> smsList;
    private ContentResolver contentResolver;

    public GetAllSms(ContentResolver contentResolver, long realmID) {
        this.contentResolver = contentResolver;
        this.realmID = realmID;
    }

    public ArrayList<Sms> getAllSms() {
        Sms smsObject;

        Realm realm = RealmDbHelper.getInstance();
        Contact contact = RealmDbHelper.getByRealmID(realm, realmID);

        if (contact.getMobileNumber() != null) {
            smsList = new ArrayList<>();

            if (contentResolver != null) {
                Log.e("RXJAVA", "getAllSms: ATTEMPTING TO GET SMS");

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
}
