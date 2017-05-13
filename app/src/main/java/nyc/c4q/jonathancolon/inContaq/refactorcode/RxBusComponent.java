package nyc.c4q.jonathancolon.inContaq.refactorcode;

import javax.inject.Singleton;

import dagger.Component;
import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactSmsFragment;
import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactStatsFragment;

@Singleton
@Component(modules = RxBusModule.class)
public interface RxBusComponent {
    RxBus getRxBus();
    void inject(ContactStatsFragment fragment);
    void inject(ContactSmsFragment fragment);
}
