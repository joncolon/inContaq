package nyc.c4q.jonathancolon.inContaq.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.application.AppController;

/**
 * Created by jonathancolon on 6/9/17.
 */

@Module
public class ApplicationContextModule {

    private final AppController application;

    ApplicationContextModule(AppController application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public AppController application() {
        return application;
    }

    @Provides
    @Singleton
    Context applicationContext() {
        return application.getApplicationContext();
    }

}
