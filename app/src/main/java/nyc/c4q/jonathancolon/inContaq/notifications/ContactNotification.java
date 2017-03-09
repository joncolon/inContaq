package nyc.c4q.jonathancolon.inContaq.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;

import static android.support.v4.app.NotificationCompat.PRIORITY_HIGH;

/**
 * Created by Hyun on 3/8/17.
 */

public class ContactNotification {

    private NotificationCompat.Builder mBuilder;
    private NotificationManager notificationManager;
    private Context mContext;


    public ContactNotification(Context context) {
        mContext = context;
    }

    public void startNotification() {

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
                .setContentTitle("My notification")
                .setContentText("Hello inContaq!")
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.heartnotification_icon))
                .setSound(notification);

        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
