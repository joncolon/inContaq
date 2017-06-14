package nyc.c4q.jonathancolon.inContaq.application;

import android.app.Application;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.di.Injector;
import nyc.c4q.jonathancolon.inContaq.db.RealmService;

public class AppController extends Application {

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
