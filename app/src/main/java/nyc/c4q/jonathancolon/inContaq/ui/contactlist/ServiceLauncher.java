package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.di.Injector;
import nyc.c4q.jonathancolon.inContaq.smsreminder.ContactNotificationService;
import nyc.c4q.jonathancolon.inContaq.smsreminder.MyAlarmReceiver;

/**
 * Created by jonathancolon on 6/9/17.
 */

public class ServiceLauncher {

    @Inject
    Context context;

    ServiceLauncher() {
        Injector.getApplicationComponent().inject(this);
    }

    void checkServiceCreated() {
        if (!ContactNotificationService.hasStarted) {
            System.out.println("Starting service...");
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
