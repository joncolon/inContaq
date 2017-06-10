package nyc.c4q.jonathancolon.inContaq;

import android.app.Application;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.utlities.Injector;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmService;

/**
 * Created by jonathancolon on 6/9/17.
 */

public class MyApplication extends Application {

    @Inject
    RealmService realmService;

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
        initRealmConfiguration();
    }


    protected void initDagger() {
        Injector.initializeApplicationComponent(this);
        Injector.getApplicationComponent().inject(this);
    }


    private void initRealmConfiguration() {
        realmService.setup();
    }
}
