package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.rxbus;

import javax.inject.Singleton;

import dagger.Component;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactsms.ContactSmsFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data.ContactStatsFragment;

@Singleton
@Component(modules = RxBusModule.class)
public interface RxBusComponent {
    RxBus getRxBus();

    void inject(ContactStatsFragment fragment);

    void inject(ContactSmsFragment fragment);
}
