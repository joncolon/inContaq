package nyc.c4q.jonathancolon.inContaq.data.asynctasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.params.GetAllSmsTaskParams;
import nyc.c4q.jonathancolon.inContaq.sms.model.Sms;

public class GetAllSmsWorkerTask extends AsyncTask<GetAllSmsTaskParams, Void, ArrayList<Sms>> {

    private static final String URI_ALL = "content://sms/";
    private static final String ID = "_id";
    private static final String ADDRESS = "address";
    private static final String PERSON = "person";
    private static final String BODY = "body";
    private static final String DATE = "date";
    private static final String TYPE = "type";
    private static final String DATE_DESC = "date desc";
    private static final String INBOX = "inbox";
    private static final String SENT = "sent";


    @Override
    protected ArrayList<Sms> doInBackground(GetAllSmsTaskParams... params) {
        ArrayList<Sms> smsList = getAllSms(params[0].context, params[0].contact);
        return smsList;
    }

    synchronized public static ArrayList<Sms> getAllSms(Context context, Contact contact) {
        StringBuilder smsBuilder;
        smsBuilder = new StringBuilder();
        final String SMS_URI_ALL = URI_ALL;

        ArrayList<Sms> smsList = new ArrayList<Sms>();
        Sms objSms;

        if (contact.getCellPhoneNumber() != null){

            try {
                Uri uri = Uri.parse(SMS_URI_ALL);
                String[] projection = new String[]{ID, ADDRESS, PERSON, BODY, DATE, TYPE};
                Cursor cursor = context.getApplicationContext().getContentResolver().query(uri, projection,
                        ADDRESS + "='" + contact.getCellPhoneNumber() + "'", null, DATE_DESC);

                if (cursor != null) {
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

                if (cursor != null) {
                    if (cursor.moveToFirst()) {

                        int totalSMS = cursor.getCount();

                        for (int i = 0; i < totalSMS; i++) {
                            objSms = new Sms();
                            objSms.setId(cursor.getString(cursor.getColumnIndexOrThrow(ID)));
                            //todo why does this cause an error?
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
                }
            } catch (SQLiteException ex) {
                Log.d("SQLiteException", ex.getMessage());
            }
        }
        smsList.size();
        return smsList;
    }
}


