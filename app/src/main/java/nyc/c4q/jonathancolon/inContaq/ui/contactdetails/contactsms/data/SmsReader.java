package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
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
    public static final String TAG = SmsReader.class.getSimpleName();
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

    public Single<ArrayList<Sms>> retrieveSmsList(long realmID) {
        Contact contact = realmService.getByRealmID(realmID);
        smsList = new ArrayList<>(EMPTY);

        String formattedNumber = getFormattedNumber(contact.getMobileNumber());

        return Single.fromCallable(() -> readContentProvider(formattedNumber))
                .subscribeOn(Schedulers.io());
    }

    private String getFormattedNumber(String mobileNumber) {
        try {
            return phoneNumberFormatter.formatPhoneNumber(mobileNumber);
        } catch (NumberParseException e) {
            Log.e(TAG, "getFormattedNumber: " + Arrays.toString(e.getStackTrace()));
        }
        return mobileNumber;
    }

    private ArrayList<Sms> readContentProvider(String formattedNumber) throws NumberParseException {
        Uri uri = Uri.parse(URI_ALL);
        String[] projection = new String[]{ADDRESS, BODY, DATE, TYPE};
        Cursor cursor = contentResolver.query(uri,
                projection, getSelection(formattedNumber), null, DATE_DESC);

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

        return smsList;
    }

    @NonNull
    private String getSelection(String formattedNumber) {
        return new StringBuilder()
                .append(ADDRESS)
                .append("='")
                .append(formattedNumber)
                .append("'")
                .toString();
    }

    @NonNull
    private Sms createSmsModel(Cursor cursor) throws NumberParseException {
        Sms sms = new Sms();
        String phoneNumber = getFormattedNumber(
                cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS)));

        sms.setPhoneNumber(phoneNumber);
        sms.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(BODY)));
        sms.setTimeStamp(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
        sms.setType(cursor.getString(cursor.getColumnIndexOrThrow(TYPE)));

        return sms;
    }
}
