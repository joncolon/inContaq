package nyc.c4q.jonathancolon.inContaq.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.db.RealmService;

/**
 * Created by jonathancolon on 6/9/17.
 */

@Module
public class RealmModule {

    @Provides
    @Singleton
    public RealmService provideRealmService() {
        return new RealmService();
    }
}
