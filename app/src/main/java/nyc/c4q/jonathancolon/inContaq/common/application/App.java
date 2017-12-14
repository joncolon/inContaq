package nyc.c4q.jonathancolon.inContaq.common.application;

import android.app.Application;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.common.dagger.Injector;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;

public class App extends Application {

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
