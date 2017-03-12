package nyc.c4q.jonathancolon.inContaq.notifications;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class BootBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Launch the specified service when this message is received
        System.out.println("Called WakefulBroadcastReceiver...");
        Intent startServiceIntent = new Intent(context, ContactNotification.class);
        startWakefulService(context, startServiceIntent);
    }
}
