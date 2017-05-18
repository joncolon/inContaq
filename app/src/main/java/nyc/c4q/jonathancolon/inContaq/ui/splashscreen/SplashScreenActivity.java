package nyc.c4q.jonathancolon.inContaq.ui.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity;

public class SplashScreenActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler = new Handler();

        handler.postDelayed(() -> {
            FragmentManager fragmentManager = SplashScreenActivity.this.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.splash_screen_layout, new SplashScreenFragment())
                    .commit();
            Intent intent = new Intent(SplashScreenActivity.this.getApplicationContext(), ContactListActivity.class);
            SplashScreenActivity.this.startActivity(intent);
        }, 4000);
    }
}
