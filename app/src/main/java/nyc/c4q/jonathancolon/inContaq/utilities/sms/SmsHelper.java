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
        String amPm;
        String minutes;
        String hour;

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timeInMilli);
        int month = (calendar.get(Calendar.MONTH)) + 1;
        int year = (calendar.get(Calendar.YEAR));
        int dayOfMonth = (calendar.get(Calendar.DAY_OF_MONTH));
        int hourOfDay = (calendar.get(Calendar.HOUR));
        int minute = (calendar.get(Calendar.MINUTE));
        int isAMorPM = (calendar.get(Calendar.AM_PM));

        hour = hourOfDay == 0 ? "12" : String.valueOf(hourOfDay);

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
        amPm = isAMorPM == 0 ? "AM" : "PM";

        String formattedDate = month + "/" + dayOfMonth + "/" + year + " " + hour + ":" + minutes + " " + amPm;
        return formattedDate;
    }

    public static void getLastContactedDate(Context context) {

        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://sms/sent"), null, null, null, null);
        cursor.moveToFirst();
        String date = cursor.getString(cursor.getColumnIndex("date"));
        Long timestamp = Long.parseLong(date);

        Log.d(Contact.class.getName(), smsDateFormat(timestamp));


        Toast.makeText(context, "Last Contacted: " + smsDateFormat(timestamp), Toast.LENGTH_LONG).show();
    }

    public static ArrayList<Sms> getAllSms(Context context) {
        StringBuilder smsBuilder = new StringBuilder();
        final String SMS_URI_ALL = "content://sms/";

        ArrayList lstSms = new ArrayList<Sms>();
        Sms objSms = null;

        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor c = context.getApplicationContext().getContentResolver().query(uri, projection, null, null, "date desc");
            if (c.moveToFirst()) {
                int index_Address = c.getColumnIndex("address");
                int index_Person = c.getColumnIndex("person");
                int index_Body = c.getColumnIndex("body");
                int index_Date = c.getColumnIndex("date");
                int index_Type = c.getColumnIndex("type");

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
        lstSms.size();
        return lstSms;
    }
}


//--------------------------------------------------------------------------------------------------

    //TODO inspect this getAllSmsFromSender() for usability
//    public void getAllSmsFromSender() {
//        StringBuilder smsBuilder = new StringBuilder();
//        final String SMS_URI_INBOX = "content://sms/inbox";
//        final String SMS_URI_ALL = "content://sms/";
//
//        lstSms = new ArrayList<Sms>();
//        Sms objSms = null;
//
//        try {
//            Uri uri = Uri.parse(SMS_URI_INBOX);
//            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
//            Cursor c = getActivity().getApplicationContext().getContentResolver().query(uri, projection, "address='9172707921'", null, "date desc");
//            if (c.moveToFirst()) {
//                int index_Address = c.getColumnIndex("address");
//                int index_Person = c.getColumnIndex("person");
//                int index_Body = c.getColumnIndex("body");
//                int index_Date = c.getColumnIndex("date");
//                int index_Type = c.getColumnIndex("type");
//
//                int totalSMS = c.getCount();
//
//                for (int i = 0; i < totalSMS; i++) {
//                    objSms = new Sms();
//                    objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
//                    objSms.setAddress(c.getString(c.getColumnIndexOrThrow("address")).replaceAll("\\s+", ""));
//                    objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
//                    objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
//                    objSms.setType(c.getString(c.getColumnIndexOrThrow("type")));
//
//
//                    if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
//                        objSms.setFolderName("inbox");
//                    } else {
//                        objSms.setFolderName("sent");
//                    }
//
//                }
//                do {
//                    String strAddress = c.getString(index_Address);
//                    int intPerson = c.getInt(index_Person);
//                    String strbody = c.getString(index_Body);
//                    long longDate = c.getLong(index_Date);
//                    int int_Type = c.getInt(index_Type);
//
//                    smsBuilder.append(strAddress + "\n");
//                    smsBuilder.append(strbody + "\n");
//                    smsBuilder.append(smsDateFormat(longDate));
//                    smsBuilder.append("\n\n");
//
//
//                    smsList.setText(smsBuilder);
//
//                } while (c.moveToNext());
//
//                if (!c.isClosed()) {
//                    c.close();
//                }
//            } else {
//                smsBuilder.append("no result!");
//            }
//        } catch (SQLiteException ex) {
//            Log.d("SQLiteException", ex.getMessage());
//        }
//    }

