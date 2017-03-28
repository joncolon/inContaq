package nyc.c4q.jonathancolon.inContaq.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactSmsFragment;

public class SmsReceiver extends BroadcastReceiver  {

    //todo create listener to refresh sms fragment upon receiving text
    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if (intentExtras != null) {
            Toast.makeText(context, "Message Received!", Toast.LENGTH_SHORT).show();
            ContactSmsFragment inst = ContactSmsFragment.instance();
            inst.refreshRecyclerView();
        }
    }
}
