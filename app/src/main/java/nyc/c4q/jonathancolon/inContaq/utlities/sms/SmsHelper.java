package nyc.c4q.jonathancolon.inContaq.utlities.sms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;


public class SmsHelper {

    private static final String URI_ALL = "content://sms/";
    private static final String URI_SENT = "content://sms/sent";
    private static final String ID = "_id";
    private static final String ADDRESS = "address";
    private static final String PERSON = "person";
    private static final String BODY = "body";
    private static final String DATE = "date";
    private static final String TYPE = "type";
    private static final String DATE_DESC = "date desc";
    private static final String INBOX = "inbox";
    private static final String SENT = "sent";

    public SmsHelper() {
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

    public static void getLastContactedDate(Context context) {

        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse(URI_SENT), null, null, null, null);
        cursor.moveToFirst();
        String date = cursor.getString(cursor.getColumnIndex(DATE));
        Long timestamp = Long.parseLong(date);
        cursor.close();

        Log.d(Contact.class.getName(), String.valueOf(smsDateFormat(timestamp)));

        Toast.makeText(context, "Last Contacted: " + smsDateFormat(timestamp),
                Toast.LENGTH_LONG).show();
    }


    //TODO Split into separate methods. 1. getContactSms 2. getAllSms
    synchronized public static ArrayList<Sms> getAllSms(Context context, Contact contact) {
        StringBuilder smsBuilder = new StringBuilder();
        final String SMS_URI_ALL = URI_ALL;

        ArrayList<Sms> smsList = new ArrayList<Sms>();
        Sms objSms;

        if (contact.getCellPhoneNumber() != null){

        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{ID, ADDRESS, PERSON, BODY, DATE, TYPE};
            Cursor cursor = context.getApplicationContext().getContentResolver().query(uri, projection,
                    ADDRESS + "='" + contact.getCellPhoneNumber() + "'", null, DATE_DESC);

            if (cursor.moveToFirst()) {

                int totalSMS = cursor.getCount();

                for (int i = 0; i < totalSMS; i++) {
                    objSms = new Sms();
                    objSms.setId(cursor.getString(cursor.getColumnIndexOrThrow(ID)));
                    objSms.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS)).replaceAll("\\s+", ""));
                    objSms.setMsg(cursor.getString(cursor.getColumnIndexOrThrow(BODY)));
                    objSms.setTime(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                    objSms.setType(cursor.getString(cursor.getColumnIndexOrThrow(TYPE)));

                    if (cursor.getString(cursor.getColumnIndexOrThrow(TYPE)).contains("1")) {
                        objSms.setFolderName(INBOX);
                    } else {
                        objSms.setFolderName(SENT);
                    }
                    smsList.add(objSms);
                    cursor.moveToNext();
                }
                if (!cursor.isClosed()) {
                    cursor.close();
                }
            } else {
                smsBuilder.append(R.string.sms_no_result);
            }
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
        } else {
            try {
                Uri uri = Uri.parse(SMS_URI_ALL);

                String[] projection = new String[]{ID, ADDRESS, PERSON, BODY,
                        DATE, TYPE};

                Cursor cursor = context.getApplicationContext().getContentResolver().query(uri,
                        projection, null, null, DATE_DESC);

                if (cursor.moveToFirst()) {

                    int totalSMS = cursor.getCount();

                    for (int i = 0; i < totalSMS; i++) {
                        objSms = new Sms();
                        objSms.setId(cursor.getString(cursor.getColumnIndexOrThrow(ID)));
                        objSms.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS)).replaceAll("\\s+", ""));
                        objSms.setMsg(cursor.getString(cursor.getColumnIndexOrThrow(BODY)));
                        objSms.setTime(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                        objSms.setType(cursor.getString(cursor.getColumnIndexOrThrow(TYPE)));

                        if (cursor.getString(cursor.getColumnIndexOrThrow(TYPE)).contains("1")) {
                            objSms.setFolderName(INBOX);
                        } else {
                            objSms.setFolderName(SENT);
                        }
                        smsList.add(objSms);
                        cursor.moveToNext();
                    }
                    if (!cursor.isClosed()) {
                        cursor.close();
                    }
                } else {
                    smsBuilder.append(R.string.sms_no_result);
                }
            } catch (SQLiteException ex) {
                Log.d("SQLiteException", ex.getMessage());
            }
        }

        smsList.size();
        return smsList;
    }
}

