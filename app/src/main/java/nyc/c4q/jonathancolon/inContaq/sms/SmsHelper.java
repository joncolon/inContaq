package nyc.c4q.jonathancolon.inContaq.sms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.params.GetAllSmsTaskParams;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.GetAllSmsWorkerTask;
import nyc.c4q.jonathancolon.inContaq.sms.model.Sms;


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

    public static void getLastContactedDate(Context context, Contact contact) {

        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse(URI_SENT), null, ADDRESS  + "='" + contact.getCellPhoneNumber() + "'", null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String date = cursor.getString(cursor.getColumnIndex(DATE));
                Long timestamp = Long.parseLong(date);
                cursor.close();

                Log.d(Contact.class.getName(), String.valueOf(smsDateFormat(timestamp)));

                Toast.makeText(context, "Last Contacted: " + smsDateFormat(timestamp),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    synchronized public static ArrayList<Sms> getAllSms(Context context, Contact contact) {
        GetAllSmsTaskParams getAllSmsTaskParams = new GetAllSmsTaskParams(contact, context);
        GetAllSmsWorkerTask getAllSmsWorkerTask = new GetAllSmsWorkerTask();
        ArrayList<Sms> smsList;

        try {
            smsList = getAllSmsWorkerTask.execute(getAllSmsTaskParams).get();
            return smsList;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        throw new NullPointerException();
    }

    synchronized public static ArrayList<Sms> getSentSms(Context context, Contact contact) {
        GetAllSmsTaskParams getAllSmsTaskParams = new GetAllSmsTaskParams(contact, context);
        GetAllSmsWorkerTask getAllSmsWorkerTask = new GetAllSmsWorkerTask();
        ArrayList<Sms> smsList;

        try {
            smsList = getAllSmsWorkerTask.execute(getAllSmsTaskParams).get();
            return parseSentSms(smsList);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        throw new NullPointerException();
    }

    synchronized public static ArrayList<Sms> getReceivedSms(Context context, Contact contact) {
        GetAllSmsTaskParams getAllSmsTaskParams = new GetAllSmsTaskParams(contact, context);
        GetAllSmsWorkerTask getAllSmsWorkerTask = new GetAllSmsWorkerTask();
        ArrayList<Sms> smsList;

        try {
            smsList = getAllSmsWorkerTask.execute(getAllSmsTaskParams).get();
            return parseReceivedSms(smsList);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        throw new NullPointerException();
    }

    @NonNull
    public static ArrayList<Sms> parseSentSms(ArrayList<Sms> smsList) {
        ArrayList<Sms> sentSms = new ArrayList<Sms>();

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

