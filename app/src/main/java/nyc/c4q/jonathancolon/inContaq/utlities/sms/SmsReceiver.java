package nyc.c4q.jonathancolon.inContaq.utlities.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactSmsFragment;

public class SmsReceiver extends BroadcastReceiver  {

    //todo create listener to refresh sms fragment upon receiving text
    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            for (int i = 0; i < sms.length; ++i) {

                String format = intentExtras.getString("format");
            }

            ContactSmsFragment inst = ContactSmsFragment.instance();

            inst.populateSmsList();
            inst.refreshRecyclerView();

        }
    }
}
