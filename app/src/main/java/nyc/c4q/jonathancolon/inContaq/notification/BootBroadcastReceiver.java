package nyc.c4q.jonathancolon.inContaq.notification;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class BootBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, ContactNotificationService.class);
        startWakefulService(context, startServiceIntent);
    }
}
