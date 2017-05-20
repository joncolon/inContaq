package nyc.c4q.jonathancolon.inContaq.utlities;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.model.Sms;


public class SmsHelper {

    private static final String URI_SENT = "content://sms/sent";
    private static final String ADDRESS = "address";
    private static final String DATE = "date";

    public SmsHelper() {
    }

    public static long getLastContactedDate(ContentResolver contentResolver, Contact contact) {
        Cursor cursor = contentResolver.query(Uri.parse(URI_SENT), null, ADDRESS + "='" + contact.getMobileNumber() + "'", null, null);
        if (cursor != null) {
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

    public static StringBuilder smsDateFormat(long timeInMilli) {
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
    public static ArrayList<Sms> parseSentSms(ArrayList<Sms> smsList) {
        ArrayList<Sms> sentSms = new ArrayList<>();

        for (int i = 0; i < smsList.size(); i++) {
            if (smsList.get(i).getType().equals("2")) {
                sentSms.add(smsList.get(i));
            }
        }
        return sentSms;
    }

    @NonNull
    public static ArrayList<Sms> parseReceivedSms(ArrayList<Sms> smsList) {
        ArrayList<Sms> receivedSms = new ArrayList<>();
        for (int i = 0; i < smsList.size(); i++) {
            if (smsList.get(i).getType().equals("1")) {
                receivedSms.add(smsList.get(i));
            }
        }
        return receivedSms;
    }
}

