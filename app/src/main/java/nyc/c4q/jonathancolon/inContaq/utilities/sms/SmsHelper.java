package nyc.c4q.jonathancolon.inContaq.utilities.sms;

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

import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;

/**
 * Created by jonathancolon on 1/5/17.
 */

public class SmsHelper {

    public SmsHelper() {
    }

    public static String smsDateFormat(long timeInMilli) {

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

        String formattedDate = month + "/" + dayOfMonth + "/" + year + " ";
        formattedDate += hour + ":" + minutes + " " + amPm;
        return formattedDate;
    }

    public static void getLastContactedDate(Context context) {

        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://sms/sent"), null, null, null, null);
        cursor.moveToFirst();
        String date = cursor.getString(cursor.getColumnIndex("date"));
        Long timestamp = Long.parseLong(date);

        Log.d(Contact.class.getName(), smsDateFormat(timestamp));


        Toast.makeText(context, "Last Contacted: " + smsDateFormat(timestamp),
                Toast.LENGTH_LONG).show();
    }

    public static ArrayList<Sms> getAllSms(Context context, Contact contact) {
        StringBuilder smsBuilder = new StringBuilder();
        final String SMS_URI_ALL = "content://sms/";

        ArrayList lstSms = new ArrayList<Sms>();
        Sms objSms;

        if (contact.getCellPhoneNumber() != null){
        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor c = context.getApplicationContext().getContentResolver().query(uri, projection,
                    "address='" + contact.getCellPhoneNumber() + "'", null, "date desc");

            if (c.moveToFirst()) {

                int totalSMS = c.getCount();

                for (int i = 0; i < totalSMS; i++) {
                    objSms = new Sms();
                    objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                    objSms.setAddress(c.getString(c.getColumnIndexOrThrow("address")).replaceAll("\\s+", ""));
                    objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                    objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                    objSms.setType(c.getString(c.getColumnIndexOrThrow("type")));

                    if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                        objSms.setFolderName("inbox");
                    } else {
                        objSms.setFolderName("sent");
                    }
                    lstSms.add(objSms);
                    c.moveToNext();
                }
                if (!c.isClosed()) {
                    c.close();
                }
            } else {
                smsBuilder.append("no result!");
            }
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
        } else {
            try {
                Uri uri = Uri.parse(SMS_URI_ALL);

                String[] projection = new String[]{"_id", "address", "person", "body",
                        "date", "type"};

                Cursor c = context.getApplicationContext().getContentResolver().query(uri,
                        projection, null, null, "date desc");

                if (c.moveToFirst()) {

                    int totalSMS = c.getCount();

                    for (int i = 0; i < totalSMS; i++) {
                        objSms = new Sms();
                        objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                        objSms.setAddress(c.getString(c.getColumnIndexOrThrow("address")).replaceAll("\\s+", ""));
                        objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                        objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                        objSms.setType(c.getString(c.getColumnIndexOrThrow("type")));

                        if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                            objSms.setFolderName("inbox");
                        } else {
                            objSms.setFolderName("sent");
                        }
                        lstSms.add(objSms);
                        c.moveToNext();
                    }
                    if (!c.isClosed()) {
                        c.close();
                    }
                } else {
                    smsBuilder.append("no result!");
                }
            } catch (SQLiteException ex) {
                Log.d("SQLiteException", ex.getMessage());
            }
        }
        lstSms.size();
        return lstSms;

    }
}

