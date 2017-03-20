package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import nyc.c4q.jonathancolon.inContaq.R;

public class SplashScreenActivity extends AppCompatActivity {

    private TextView inContaq;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }
}
