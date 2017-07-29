package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

public class PermissionActivity extends Activity {

    private static final String PERMISSIONS_KEY = "request_permissions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions(new String[]{
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
        }, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean(PERMISSIONS_KEY, false)
                .apply();

        startActivity(new Intent(this, ContactListActivity.class));
        finish();
    }

}
