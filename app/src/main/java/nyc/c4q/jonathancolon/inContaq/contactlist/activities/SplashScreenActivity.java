package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.SplashScreenFragment;

public class SplashScreenActivity extends AppCompatActivity {

    private TextView inContaq;
    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.splash_screen_layout, new SplashScreenFragment())
                        .commit();
                Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
                startActivity(intent);

//                finish();
            }
        }, 4000);
//
    }
}
