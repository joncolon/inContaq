package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import nyc.c4q.jonathancolon.inContaq.R;


public class SplashScreen extends AppCompatActivity {

    private TextView inContaq;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Log.d("jjjjj", "onCreate: " + this.getClass().getSimpleName());

        inContaq = (TextView) findViewById(R.id.app_name);
        Typeface robotoreg = Typeface.createFromAsset(inContaq.getContext().getApplicationContext().getAssets(), "fonts/WalkwaySemiBold.ttf");
        inContaq.setTypeface(robotoreg);
    }
}
