package nyc.c4q.jonathancolon.inContaq.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlarmReceiver extends BroadcastReceiver {

    public static final String ACTION = "some.action.lol";

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Called AlarmReceiver...");
        Intent i = new Intent(context, ContactNotificationService.class);
        context.startService(i);
    }
}