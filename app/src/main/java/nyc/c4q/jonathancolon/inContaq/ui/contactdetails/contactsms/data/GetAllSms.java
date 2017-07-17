package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.PhoneNumberHelper;

import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isNull;


public class GetAllSms {

    private static final String URI_ALL = "content://sms/";
    private static final String ADDRESS = "address";
    private static final String BODY = "body";
    private static final String DATE = "date";
    private static final String TYPE = "type";
    private static final String DATE_DESC = "date desc";
    private final RealmService realmService;
    private PhoneNumberHelper phoneNumberHelper;
    private ArrayList<Sms> smsList;
    private ContentResolver contentResolver;



    @Inject
    public GetAllSms(ContentResolver contentResolver,
                     RealmService realmService, PhoneNumberHelper phoneNumberHelper) {
        this.contentResolver = contentResolver;
        this.realmService = realmService;
        this.phoneNumberHelper = phoneNumberHelper;
    }

    public ArrayList<Sms> retrieveSmsList(long realmID) throws NumberParseException {
        Sms smsObject;

        Contact contact = realmService.getByRealmID(realmID);

        if (contact.getMobileNumber() != null) {
            smsList = new ArrayList<>();

            if (!isNull(contentResolver)) {
                Log.e("RXJAVA", "retrieveSmsListInBackground: ATTEMPTING TO GET SMS");

                String formattedNumber = phoneNumberHelper.formatPhoneNumber(contact.getMobileNumber());

                Uri uri = Uri.parse(URI_ALL);
                String[] projection = new String[]{ADDRESS, BODY, DATE, TYPE};
                Cursor cursor = contentResolver.query(uri,
                        projection, ADDRESS + "='" + formattedNumber + "'", null, DATE_DESC);

                if (!isNull(cursor)) {
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
            realmService.closeRealm();
        }

        if (!isNull(smsList)) {
            return smsList;
        }
        return smsList = new ArrayList<>(0);
    }
}
