package nyc.c4q.jonathancolon.inContaq;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jonathancolon on 6/9/17.
 */

@Module
public class ApplicationContextModule {

    private final MyApplication application;

    public ApplicationContextModule(MyApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public MyApplication application() {
        return application;
    }

    @Provides
    @Singleton
    public Context applicationContext() {
        return application.getApplicationContext();
    }

}
