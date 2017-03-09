package nyc.c4q.jonathancolon.inContaq.notifications;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;

import static android.app.Notification.PRIORITY_HIGH;

/**
 * Created by Hyun on 3/8/17.
 */

public class ContactNotification extends IntentService {

    private NotificationCompat.Builder mBuilder;
    private NotificationManager notificationManager;
    private Context mContext;

    private static final long WEEK_IN_MILLISECONDS = 604800000;
    private static final int ONE_MINUTE_IN_MILLIS = 60000;
    public static boolean hasStarted = false;


    public ContactNotification() {
        super("ContactNotificationService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        WakefulBroadcastReceiver.completeWakefulIntent(intent);

        System.out.println("Called IntentService...");

        if (!hasStarted) { // Needed or else it'll keep scheduling new alarms and you'll be swarmed with notifications
            System.out.println("Setting alarm for service...");
            scheduleAlarm();
            hasStarted = true;
        }
    }


    public void startNotification(Contact contact) {

        if (hasWeekPassed(contact)){

            int NOTIFICATION_ID = 555;

            Intent intent = new Intent(mContext, ContactListActivity.class);

            int requestID = (int) System.currentTimeMillis();
            int flags = PendingIntent.FLAG_CANCEL_CURRENT; // Cancel old intent and create new one
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, requestID, intent, flags);

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            mBuilder = new NotificationCompat.Builder(mContext)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.heartnotification_icon)
                    .setPriority(PRIORITY_HIGH)
                    .setFullScreenIntent(pendingIntent, true)
                    .setContentTitle("It's been a week since you've texted " + contact.getFirstName() + " " + contact.getLastName())
                    .setContentText("Say hello inContaq!")
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.heartnotification_icon))
                    .setSound(notification);

            notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        }

    }

    private boolean hasWeekPassed(Contact contact) {
        return System.currentTimeMillis() - contact.getTimeLastContacted() > WEEK_IN_MILLISECONDS;
    }

    private void scheduleAlarm() {
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, ContactNotification.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ONE_MINUTE_IN_MILLIS, pi);
    }

    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }
}
