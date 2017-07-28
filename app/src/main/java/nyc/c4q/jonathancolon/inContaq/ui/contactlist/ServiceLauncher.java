package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.notifications.ContactNotificationService;
import nyc.c4q.jonathancolon.inContaq.notifications.MyAlarmReceiver;

/**
 * Created by jonathancolon on 6/9/17.
 */

public class ServiceLauncher {

    private Context context;
    private final static String TAG = ServiceLauncher.class.getSimpleName();

    @Inject
    public ServiceLauncher(Context context) {
        this.context = context;
    }

    void checkServiceCreated() {

        if (!ContactNotificationService.hasStarted) {
            System.out.println("Starting service...");
            ContactNotificationService.hasStarted = true;
            Log.e(TAG, String.valueOf(ContactNotificationService.hasStarted ));
            startService();
        }
    }

    private void startService() {
        IntentFilter filter = new IntentFilter(MyAlarmReceiver.ACTION);
        MyAlarmReceiver receiver = new MyAlarmReceiver();
        context.registerReceiver(receiver, filter);
        Intent intent = new Intent(MyAlarmReceiver.ACTION);
        context.sendBroadcast(intent);
    }
}
