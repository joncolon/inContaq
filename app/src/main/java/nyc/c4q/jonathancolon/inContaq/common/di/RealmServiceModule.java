package nyc.c4q.jonathancolon.inContaq.common.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;


@Module
public class RealmServiceModule {

    @Provides
    @Singleton
    RealmService provideRealmService(Context context) {
        return new RealmService(context);
    }
}
