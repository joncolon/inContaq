package nyc.c4q.jonathancolon.inContaq.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "some.action.lol";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Called AlarmReceiver...");
        Intent i = new Intent(context, ContactNotification.class);
        context.startService(i);
    }
}