package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactinfo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.List;


public class MessengerAppLauncher {

    //this is to do stuff
    private Context context;
    private final String FACEBOOK_MESSENGER = "com.facebook.orca";
    private final String SLACK = "com.Slack";
    private final String WHATSAPP = "com.whatsapp";
    private final String SNAPCHAT = "com.snapchat.android";
    private final String SKYPE = "com.skype.raider";
    private final String GOOGLE_HANGOUTS = "com.google.android.talk";
    private final String LINKEDIN = "com.linkedin.android";
    private String phoneNumber;

    MessengerAppLauncher(Context context, String phoneNumber) {
        this.context = context;
        this.phoneNumber = phoneNumber;
    }



    private boolean isPackageInstalled(String targetPackage) {
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage))
                return true;
        }
        notInstalledToast();
        return false;
    }

    void openFacebookMessenger() {
        if (isPackageInstalled(FACEBOOK_MESSENGER)){
            Intent intent = context
                    .getPackageManager()
                    .getLaunchIntentForPackage(FACEBOOK_MESSENGER);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Log.e("error", ex.toString());
            }
        }
    }

    private void notInstalledToast() {
        Toast.makeText(context, "This app is not installed", Toast.LENGTH_SHORT).show();
    }

    void openSlack() {
        if (isPackageInstalled(SLACK)){
            Intent intent = context
                    .getPackageManager()
                    .getLaunchIntentForPackage(SLACK);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Log.e("error", ex.toString());
            }
        }
    }

    void openSkype() {
        if (isPackageInstalled(SKYPE)){
            Intent intent = context
                    .getPackageManager()
                    .getLaunchIntentForPackage(SKYPE);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Log.e("error", ex.toString());
            }
        }
    }

    void openHangouts() {
        if (isPackageInstalled(GOOGLE_HANGOUTS)){
            Intent intent = context
                    .getPackageManager()
                    .getLaunchIntentForPackage(GOOGLE_HANGOUTS);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Log.e("error", ex.toString());
            }
        }
    }

    void openSnapChat() {
        if (isPackageInstalled(SNAPCHAT)){
            Intent intent = context
                    .getPackageManager()
                    .getLaunchIntentForPackage(SNAPCHAT);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Log.e("error", ex.toString());
            }
        }
    }

    void openDefaultSms() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + Uri.encode(phoneNumber)));

        if (phoneNumber != null){
            try{
                context.startActivity(intent);
            } catch (Exception e){
                Log.e("DEFAULT SMS: ", "Unable to launch sms app" + e.getMessage() );
            }
        }
    }


    void openWhatsApp() {
        if (isPackageInstalled(WHATSAPP)){
            Intent intent = context
                    .getPackageManager()
                    .getLaunchIntentForPackage(WHATSAPP);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Log.e("error", ex.toString());
            }
        }
    }

    void openLinkedIn() {
        if (isPackageInstalled(LINKEDIN)){
            Intent intent = context
                    .getPackageManager()
                    .getLaunchIntentForPackage(LINKEDIN);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Log.e("error", ex.toString());
            }
        }
    }
}
