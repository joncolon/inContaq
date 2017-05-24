package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.rxbus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jonathancolon on 5/13/17.
 */

@Module
public class RxBusModule {

    @Provides
    @Singleton
    public RxBus rxBus() {
        return new RxBus();
    }
}
