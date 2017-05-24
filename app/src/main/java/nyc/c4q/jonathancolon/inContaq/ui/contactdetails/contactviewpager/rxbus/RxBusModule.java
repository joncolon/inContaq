package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.rxbus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
class RxBusModule {

    @Provides
    @Singleton
    public RxBus rxBus() {
        return new RxBus();
    }
}
