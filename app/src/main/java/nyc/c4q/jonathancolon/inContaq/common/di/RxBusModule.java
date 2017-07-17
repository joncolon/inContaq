package nyc.c4q.jonathancolon.inContaq.common.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.utlities.RxBus;

/**
 * Created by jonathancolon on 5/13/17.
 */

@Module
class RxBusModule {

    @Provides
    @Singleton
    RxBus rxBus() {
        return new RxBus();
    }
}
