package nyc.c4q.jonathancolon.inContaq.sms;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactSmsFragment;

public class SmsReceiver extends BroadcastReceiver  {

    //todo create listener to refresh sms fragment upon receiving text

    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            Object[] pdusObj = (Object[]) bundle.get("pdus");

            SmsMessage[] messages = new SmsMessage[pdusObj.length];

            for (int i = 0; i < messages.length; i++) {
                String format = bundle.getString("format");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i], format);
                } else {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                }
            }
            for (SmsMessage msg : messages) {
                Log.i("log", "display msg body  : " + msg.getDisplayMessageBody() + "originating address : " + msg.getDisplayOriginatingAddress() + " get message body : " + msg.getMessageBody());
                ContentValues values = new ContentValues();
                values.put(Telephony.Sms.ADDRESS, msg.getDisplayOriginatingAddress());
                values.put(Telephony.Sms.BODY, msg.getMessageBody());
                values.put(Telephony.Sms.DATE, msg.getTimestampMillis());
                values.put(Telephony.Sms.STATUS, msg.getStatus());

                if (msg.getOriginatingAddress().equals("9175019362")){
                    values.put(Telephony.Sms.TYPE, 2);
                } else
                    values.put(Telephony.Sms.TYPE, 1);
                context.getApplicationContext().getContentResolver().insert(Telephony.Sms.Sent.CONTENT_URI, values);
            }
        }
    ContactSmsFragment inst = ContactSmsFragment.instance();
        inst.updateSms();
    }
}

