package nyc.c4q.jonathancolon.inContaq.notification;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import io.realm.Realm;
import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmDbHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsHelper;

import static android.app.Notification.PRIORITY_HIGH;
import static android.media.RingtoneManager.TYPE_NOTIFICATION;
import static android.media.RingtoneManager.getDefaultUri;


public class ContactNotificationService extends IntentService {

    private static final long ONE_WEEK = 604800000;
    private static final long TWO_WEEKS = 1209600000;
    private static final long THREE_WEEKS = 1814400000;
    private static final int TWELVE_HOURS = 43200000;
    private static final int ONE_MIN = 600000;
    public static boolean hasStarted = false;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager notificationManager;
    private SQLiteDatabase db;
    private Context context;
    private Realm realm;
    private ContentResolver contentResolver;

    public ContactNotificationService() {
        super("ContactNotificationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        realm = RealmDbHelper.getInstance();
        contentResolver = context.getContentResolver();
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
        checkInspectionTime();
    }

    private void scheduleAlarm() {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, ContactNotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, ONE_MIN, TWELVE_HOURS,
                pendingIntent);
    }

    private void checkInspectionTime() {
        RealmResults<Contact> contactList = RealmDbHelper.findByReminderEnabled(realm);

        boolean notificationFired = false;
        int i = 0;
        while (i < contactList.size() && !notificationFired) {
            Contact contact = contactList.get(i);
                if (contact.isReminderEnabled()) {
                    switch (contact.getReminderDuration()) {
                        case 1:
                            if (hasWeekPassed(contact)) {
                                startNotification(contact, context, 1);
                                notificationFired = true;
                                break;
                            }
                        case 2:
                            if (hasTwoWeeksPassed(contact)) {
                                startNotification(contact, context, 2);
                                notificationFired = true;
                            }
                            break;
                        case 3:
                            if (hasThreeWeeksPassed(contact)) {
                                startNotification(contact, context, 3);
                                notificationFired = true;
                            }
                            break;
                    }
                    i++;
            }
        }
    }

    private boolean hasWeekPassed(Contact c) {
        return System.currentTimeMillis() -
                SmsHelper.getLastContactedDate(contentResolver, c) > ONE_WEEK;
    }

    public void startNotification(Contact contact, Context context, int numberOfWeeks) {

        int NOTIFICATION_ID = (int) System.currentTimeMillis();
        Intent intent = new Intent(context, ContactListActivity.class);
        int requestID = (int) System.currentTimeMillis();
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; // Cancel old intent and create new one
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestID, intent, flags);

        Uri notification = getDefaultUri(TYPE_NOTIFICATION);

        mBuilder = new NotificationCompat.Builder(context)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.vectorpaint)
                .setPriority(PRIORITY_HIGH)
                .setFullScreenIntent(pendingIntent, true)
                .setContentTitle("Forgetting someone?")
                .setContentText("Check out inContaq")
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.vectorpaint))
                .setSound(notification);

        notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private boolean hasTwoWeeksPassed(Contact c) {
        return System.currentTimeMillis() -
                SmsHelper.getLastContactedDate(contentResolver, c) > TWO_WEEKS;
    }

    private boolean hasThreeWeeksPassed(Contact c) {
        return System.currentTimeMillis() -
                SmsHelper.getLastContactedDate(contentResolver, c) > THREE_WEEKS;
    }

    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RealmDbHelper.closeRealm(realm);
    }
}
