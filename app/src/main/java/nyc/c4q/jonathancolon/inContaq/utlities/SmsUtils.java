package nyc.c4q.jonathancolon.inContaq.utlities;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.model.Sms;

import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.*;


public class SmsUtils {

    private static final String URI_ALL = "content://sms/";
    private static final String ADDRESS = "address";
    private static final String DATE = "date";
    private ContentResolver contentResolver;

    @Inject
    public SmsUtils(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public long getLastContactedDate(Contact contact) {
        Cursor cursor = contentResolver.query(Uri.parse(URI_ALL), null,
                useMobileNumberAsSelection(contact),null, null);

        if (!isNull(cursor)) {
            if (cursor.moveToFirst()) {
                cursor.getCount();
                String date = cursor.getString(cursor.getColumnIndex(DATE));
                Long timestamp = Long.parseLong(date);
                cursor.close();

                Log.d(Contact.class.getName(), String.valueOf(smsDateFormat(timestamp)));
                return timestamp;
            }
        }
        return -1;
    }

    @NonNull
    private String useMobileNumberAsSelection(Contact contact) {
        return new StringBuilder()
                .append(ADDRESS)
                .append("='")
                .append(contact.getMobileNumber())
                .append("'")
                .toString();
    }

    public StringBuilder smsDateFormat(long timeInMilli) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timeInMilli);
        int month = (calendar.get(Calendar.MONTH)) + 1;
        int year = (calendar.get(Calendar.YEAR));
        int dayOfMonth = (calendar.get(Calendar.DAY_OF_MONTH));
        int hourOfDay = (calendar.get(Calendar.HOUR));
        int minute = (calendar.get(Calendar.MINUTE));
        int isAMorPM = (calendar.get(Calendar.AM_PM));

        String hour = hourOfDay == 0 ? "12" : String.valueOf(hourOfDay);

        String minutes;
        switch (minute) {
            case 0:
                minutes = "00";
                break;
            case 1:
                minutes = "01";
                break;
            case 2:
                minutes = "02";
                break;
            case 3:
                minutes = "03";
                break;
            case 4:
                minutes = "04";
                break;
            case 5:
                minutes = "05";
                break;
            case 6:
                minutes = "06";
                break;
            case 7:
                minutes = "07";
                break;
            case 8:
                minutes = "08";
                break;
            case 9:
                minutes = "09";
                break;
            default:
                minutes = String.valueOf(minute);
        }
        String amPm = isAMorPM == 0 ? "AM" : "PM";

        StringBuilder formattedDate = new StringBuilder()
                .append(month)
                .append("/")
                .append(dayOfMonth)
                .append("/")
                .append(year)
                .append(" ")
                .append(hour)
                .append(":")
                .append(minutes)
                .append(" ")
                .append(amPm);

        return formattedDate;
    }

    @NonNull
    public ArrayList<Sms> parseSentSms(ArrayList<Sms> smsList) {
        ArrayList<Sms> sentSms = new ArrayList<>();

        for (int i = 0; i < smsList.size(); i++) {
            if (smsList.get(i).getType().equals("2")) {
                sentSms.add(smsList.get(i));
            }
        }
        return sentSms;
    }

    @NonNull
    public ArrayList<Sms> parseReceivedSms(ArrayList<Sms> smsList) {
        ArrayList<Sms> receivedSms = new ArrayList<>();
        for (int i = 0; i < smsList.size(); i++) {
            if (smsList.get(i).getType().equals("1")) {
                receivedSms.add(smsList.get(i));
            }
        }
        return receivedSms;
    }
}

