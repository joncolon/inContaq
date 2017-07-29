package nyc.c4q.jonathancolon.inContaq.notifications;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.common.di.Injector;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsHelper;

import static android.app.Notification.PRIORITY_DEFAULT;
import static android.media.RingtoneManager.TYPE_NOTIFICATION;
import static android.media.RingtoneManager.getDefaultUri;


public class ContactNotificationService extends IntentService {

    private final static String GROUP_CONTACTS = "group_contacts";

    private static final long ONE_WEEK = 604800000;
    private static final long TWO_WEEKS = 1209600000;
    private static final long THREE_WEEKS = 1814400000;
    private static final long ONE_DAY = 86400000;
    private static final int ONE_MIN = 60000;
    private static final String TAG = ContactNotificationService.class.getSimpleName();
    public static boolean hasStarted;
    NotificationCompat.InboxStyle inboxStyle;
    private int notificationCount = 0;
    private long lastNotified;

    @Inject RealmService realmService;
    @Inject SmsHelper smsHelper;
    @Inject Context context;


    public ContactNotificationService() {
        super("ContactNotificationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.getApplicationComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        WakefulBroadcastReceiver.completeWakefulIntent(intent);

        System.out.println("Called IntentService...");
        if (!hasStarted) {
            System.out.println("Setting alarm for service...");
            scheduleAlarm();
            hasStarted = true;
        }
        checkInspectionTime();
    }

    private void scheduleAlarm() {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, ContactNotificationService.class);
        i.getExtras();
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, 5000, ONE_DAY,
                pendingIntent);
    }

    private void checkInspectionTime() {
        RealmResults<Contact> contactList = realmService.findByReminderEnabled();
        ArrayList<Contact> contacts = new ArrayList<>(contactList);

        if (lastNotified + ONE_MIN < System.currentTimeMillis()) {
            lastNotified = System.currentTimeMillis();

            if (contacts.size() > 0) {
                int i = 0;

                inboxStyle = new NotificationCompat.InboxStyle();

                while (i < contacts.size()) {
                    Contact contact = contactList.get(i);
                    long daysSince = daysSinceLastMsg(contact);

                    if (contact.isReminderEnabled() && daysSince > 0) {
                        switch (contact.getReminderDuration()) {
                            case 1:
                                if (hasWeekPassed(contact) && contact.getReminderDuration() == 1) {
                                    inboxStyle.addLine(contact.getFirstName() + ": " + " It's been "
                                            + daysSince + " days");
                                    notificationCount++;
                                    break;
                                }
                            case 2:
                                if (hasTwoWeeksPassed(contact) &&
                                        contact.getReminderDuration() == 2) {
                                    inboxStyle.addLine(contact.getFirstName() + ": " + " It's been "
                                            + daysSince + " days");
                                    notificationCount++;
                                    break;
                                }
                            case 3:
                                if (hasThreeWeeksPassed(contact) &&
                                        contact.getReminderDuration() == 3) {
                                    inboxStyle.addLine(contact.getFirstName() + ": " + " It's been "
                                            + daysSince + " days");
                                    notificationCount++;
                                    break;
                                }
                        }
                    }
                    i++;
                }
                if (notificationCount > 0) {
                    startNotification(context);
                }
            }
            realmService.closeRealm();
        }
    }

    private long daysSinceLastMsg(Contact contact) {
        long currentTime = System.currentTimeMillis();
        long lastMsg = smsHelper.getLastContactedDate(contact);
        if (lastMsg > 0) {
            long timeElapsed = currentTime - lastMsg;
            return timeElapsed / ONE_DAY;
        }
        return 0;
    }

    private boolean hasWeekPassed(Contact c) {
        return System.currentTimeMillis() -
                smsHelper.getLastContactedDate(c) > ONE_WEEK;
    }

    private boolean hasTwoWeeksPassed(Contact c) {
        return System.currentTimeMillis() -
                smsHelper.getLastContactedDate(c) > TWO_WEEKS;
    }

    private boolean hasThreeWeeksPassed(Contact c) {
        return System.currentTimeMillis() -
                smsHelper.getLastContactedDate(c) > THREE_WEEKS;
    }

    public void startNotification(Context context) {
        Log.e(TAG, "Starting notification... ");

        int NOTIFICATION_ID = 555;
        Intent intent = new Intent(context, ContactListActivity.class);

        int requestID = (int) System.currentTimeMillis();
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; // Cancel old intent and create new one
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestID, intent, flags);

        Uri notification = getDefaultUri(TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(inboxStyle)
                .setSmallIcon(R.drawable.ic_app_logo)
                .setPriority(PRIORITY_DEFAULT)
                .setFullScreenIntent(pendingIntent, true)
                .setContentTitle("Forgetting someone?")
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_app_logo))
                .setSound(notification)
                .setGroup(GROUP_CONTACTS);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
