package nyc.c4q.jonathancolon.inContaq.notifications;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.common.dagger.Injector;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.model.ContactModel;
import nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsUtils;

import static android.app.Notification.PRIORITY_DEFAULT;
import static android.media.RingtoneManager.TYPE_NOTIFICATION;
import static android.media.RingtoneManager.getDefaultUri;
import static android.support.v4.app.NotificationCompat.Builder;
import static android.support.v4.app.NotificationCompat.InboxStyle;
import static android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC;
import static nyc.c4q.jonathancolon.inContaq.notifications.DaysPassed.ONE_DAY;


public class ContactNotificationService extends IntentService {

    private final static int FIVE_SECONDS = 5000;
    private static final int ONE_MIN = 60000;

    private static final String TAG = ContactNotificationService.class.getSimpleName();
    private static final String GROUP_CONTACTS = "group_contacts";
    public static boolean hasStarted;
    @Inject
    RealmService realmService;
    @Inject
    SmsUtils smsUtils;
    @Inject
    Context context;
    @Inject
    WeeksPassed weeksPassed;
    @Inject
    DaysPassed daysPassed;
    private int notificationCount = 0;
    private long lastNotified;
    private InboxStyle inboxStyle;


    public ContactNotificationService() {
        super("ContactNotificationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.getApplicationComponent().inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realmService.closeRealm();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        WakefulBroadcastReceiver.completeWakefulIntent(intent);

        System.out.println("Called IntentService...");
        Log.e(TAG, String.valueOf(ContactNotificationService.hasStarted));
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
        am.setRepeating(AlarmManager.RTC_WAKEUP, FIVE_SECONDS, ONE_DAY,
                pendingIntent);
    }

    private void checkInspectionTime() {
        RealmResults<ContactModel> contactModelList = realmService.findByReminderEnabled();
        ArrayList<ContactModel> contactModels = new ArrayList<>(contactModelList);

        if (lastNotified + ONE_MIN < System.currentTimeMillis()) {
            lastNotified = System.currentTimeMillis();

            if (contactModels.size() > 0) {
                int i = 0;

                inboxStyle = new InboxStyle();

                while (i < contactModels.size()) {
                    ContactModel contactModel = contactModelList.get(i);
                    long daysSince = daysPassed.daysSinceLastMsg(contactModel);

                    if (contactModel.isReminderEnabled() && daysSince > 0) {
                        switch (contactModel.getReminderDuration()) {
                            case 1:
                                if (weeksPassed.hasWeekPassed(contactModel) &&
                                        contactModel.getReminderDuration() == 1) {
                                    inboxStyle.addLine(addNotificationMessage(contactModel, daysSince));
                                    notificationCount++;
                                    break;
                                }
                            case 2:
                                if (weeksPassed.hasTwoWeeksPassed(contactModel) &&
                                        contactModel.getReminderDuration() == 2) {
                                    inboxStyle.addLine(addNotificationMessage(contactModel, daysSince));
                                    notificationCount++;
                                    break;
                                }
                            case 3:
                                if (weeksPassed.hasThreeWeeksPassed(contactModel) &&
                                        contactModel.getReminderDuration() == 3) {
                                    inboxStyle.addLine(addNotificationMessage(contactModel, daysSince));
                                    notificationCount++;
                                    break;
                                }
                        }
                    }
                    i++;
                }
            }
            if (notificationCount > 0) {
                startNotification(context);
            }
        }
        realmService.closeRealm();
    }

    @NonNull
    private String addNotificationMessage(ContactModel contactModel, long daysSince) {
        return contactModel.getFirstName() + ": " + " It's been " + daysSince + " days";
    }

    public void startNotification(Context context) {
        Log.e(TAG, "Starting notification... ");

        int NOTIFICATION_ID = 555;
        Intent intent = new Intent(context, ContactListActivity.class);

        int requestID = (int) System.currentTimeMillis();
        int flags = PendingIntent.FLAG_CANCEL_CURRENT;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestID, intent, flags);

        Uri notification = getDefaultUri(TYPE_NOTIFICATION);

        Builder mBuilder = new Builder(context)
                .setVisibility(VISIBILITY_PUBLIC)
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
}
