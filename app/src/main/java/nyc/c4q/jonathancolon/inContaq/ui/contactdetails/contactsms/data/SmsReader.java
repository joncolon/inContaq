package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import javax.inject.Inject;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.model.ContactModel;
import nyc.c4q.jonathancolon.inContaq.model.SmsModel;
import nyc.c4q.jonathancolon.inContaq.utlities.PhoneNumberFormatter;

import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isNull;


public class SmsReader {

    private static final String URI_ALL = "content://sms/";
    private static final String ADDRESS = "address";
    private static final String BODY = "body";
    private static final String DATE = "date";
    private static final String TYPE = "type";
    private static final String DATE_DESC = "date desc";
    private static final int EMPTY = 0;
    private final RealmService realmService;
    private PhoneNumberFormatter phoneNumberFormatter;
    private ArrayList<SmsModel> smsList;
    private ContentResolver contentResolver;


    @Inject
    public SmsReader(@NonNull ContentResolver contentResolver,
                     RealmService realmService, PhoneNumberFormatter phoneNumberFormatter) {
        this.contentResolver = contentResolver;
        this.realmService = realmService;
        this.phoneNumberFormatter = phoneNumberFormatter;
    }

    public ArrayList<SmsModel> retrieveSmsList(long realmID) throws Exception {
        ContactModel contactModel = realmService.getByRealmID(realmID);
        smsList = new ArrayList<>(EMPTY);

        // The contactModel should NEVER be null.
        if (isNull(contactModel)) {
            throw new Exception("ContactModel is Null");
        }

        if (isNull(contactModel.getMobileNumber())) {
            throw new Exception("ContactModel does not have a Mobile Number");
        }

        String formattedNumber = getFormattedNumber(contactModel.getMobileNumber());
        readContentProvider(formattedNumber);

        return smsList;
    }

    private String getFormattedNumber(String mobileNumber) throws NumberParseException {
        return phoneNumberFormatter.formatPhoneNumber(mobileNumber);
    }

    private void readContentProvider(String formattedNumber) throws NumberParseException {
        Uri uri = Uri.parse(URI_ALL);
        String[] projection = new String[]{ADDRESS, BODY, DATE, TYPE};
        Cursor cursor = contentResolver.query(uri,
                projection, ADDRESS + "='" + formattedNumber + "'", null, DATE_DESC);

        if (!isNull(cursor)) {
            if (cursor.moveToFirst()) {
                int totalSms = cursor.getCount();
                for (int i = 0; i < totalSms; i++) {
                    smsList.add(createSmsModel(cursor));
                    cursor.moveToNext();
                }
                if (!cursor.isClosed()) {
                    cursor.close();
                }
            }
        }
    }

    @NonNull
    private SmsModel createSmsModel(Cursor cursor) throws NumberParseException {
        SmsModel sms = new SmsModel();
        String phoneNumber = getFormattedNumber(
                cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS)));

        sms.setPhoneNumber(phoneNumber);
        sms.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(BODY)));
        sms.setTimeStamp(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
        sms.setType(cursor.getString(cursor.getColumnIndexOrThrow(TYPE)));

        return sms;
    }
}
