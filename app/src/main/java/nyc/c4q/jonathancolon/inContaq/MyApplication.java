package nyc.c4q.jonathancolon.inContaq;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by jonathancolon on 5/4/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
    }
}
