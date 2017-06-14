package nyc.c4q.jonathancolon.inContaq.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by jonathancolon on 6/10/17.
 */

public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }



    public static Context getContext() {
        return context;
    }


}
