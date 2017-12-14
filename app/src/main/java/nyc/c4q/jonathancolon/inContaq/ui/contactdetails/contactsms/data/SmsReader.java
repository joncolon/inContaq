package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.model.Sms;
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
    private ArrayList<Sms> smsList;
    private ContentResolver contentResolver;


    @Inject
    public SmsReader(@NonNull ContentResolver contentResolver,
                     RealmService realmService, PhoneNumberFormatter phoneNumberFormatter) {
        this.contentResolver = contentResolver;
        this.realmService = realmService;
        this.phoneNumberFormatter = phoneNumberFormatter;
    }

    public ArrayList<Sms> retrieveSmsList(long realmID) throws NumberParseException {
        Contact contact = realmService.getByRealmID(realmID);
        smsList = new ArrayList<>(EMPTY);

        // The contact should NEVER be null.
        if (isNull(contact)) {
            return smsList;
        }

        if (isNull(contact.getMobileNumber())) {
            return smsList;
        }

        String formattedNumber = getFormattedNumber(contact.getMobileNumber());
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
                int totalSMS = cursor.getCount();
                for (int i = 0; i < totalSMS; i++) {
                    smsList.add(getSms(cursor));
                    cursor.moveToNext();
                }
                if (!cursor.isClosed()) {
                    cursor.close();
                }
            }
        }
    }

    @NonNull
    private Sms getSms(Cursor cursor) throws NumberParseException {
        Sms smsModel = new Sms();
        String phoneNumber = getFormattedNumber(
                cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS)));

        smsModel.setPhoneNumber(phoneNumber);
        smsModel.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(BODY)));
        smsModel.setTimeStamp(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
        smsModel.setType(cursor.getString(cursor.getColumnIndexOrThrow(TYPE)));

        return smsModel;
    }
}
