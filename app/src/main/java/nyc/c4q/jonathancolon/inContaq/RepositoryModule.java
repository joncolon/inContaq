package nyc.c4q.jonathancolon.inContaq;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmService;

/**
 * Created by jonathancolon on 6/9/17.
 */

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public RealmService provideDatabaseRealm() {
        return new RealmService();
    }
}
