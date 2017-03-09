package nyc.c4q.jonathancolon.inContaq.utlities.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactSmsFragment;

//
//public class SmsReceiver extends BroadcastReceiver {
//
//    public static final String SMS_BUNDLE = "pdus";
//
//    public void onReceive(Context context, Intent intent) {
//        Bundle intentExtras = intent.getExtras();
//
//        if (intentExtras != null) {
//            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
//            String smsMessageStr = "";
//            for (int i = 0; i < sms.length; ++i) {
//                String format = intentExtras.getString("format");
//                SmsMessage smsMessage = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                    smsMessage = SmsMessage.createFromPdu((byte[]) sms[i], format);
//                }
//
//                String smsBody = smsMessage.getMessageBody().toString();
//                String address = smsMessage.getOriginatingAddress();
//
//                smsMessageStr += "SMS From: " + address + "\n";
//                smsMessageStr += smsBody + "\n";
//            }
//
//            ContactSmsFragment inst = ContactSmsFragment.newInstance();
//            inst.onSaveInstanceState(intentExtras);
////            inst.updateInbox(smsMessageStr);
//        }
//    }
//}




public class SmsReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;
        String strMessage = "";

        if (myBundle != null) {
            Object[] pdus = (Object[]) myBundle.get("pdus");

            messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String format = myBundle.getString("format");
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                strMessage += "SMS From: " + messages[i].getOriginatingAddress();
                strMessage += " : ";
                strMessage += messages[i].getMessageBody();
                strMessage += "\n";
            }

            Log.v("SMS", strMessage);
            Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
