package nyc.c4q.jonathancolon.inContaq.common.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.common.application.App;


@Module
public class ApplicationContextModule {

    private final App application;

    ApplicationContextModule(App application) {
        this.application = application;
    }

    @Provides
    @Singleton
    App providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return application.getApplicationContext();
    }
}
