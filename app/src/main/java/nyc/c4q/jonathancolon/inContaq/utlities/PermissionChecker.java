package nyc.c4q.jonathancolon.inContaq.utlities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionChecker {
    private final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS};
    private Activity activity;
    private Context context;

    public PermissionChecker(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, 3);
        }
    }
}
