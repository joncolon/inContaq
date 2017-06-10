package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.rxbus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
