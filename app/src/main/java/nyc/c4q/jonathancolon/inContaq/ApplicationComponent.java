package nyc.c4q.jonathancolon.inContaq;

import javax.inject.Singleton;

import dagger.Component;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.ContactViewPagerActivity;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactinfo.ContactInfoFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.ContactSmsFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.data.GetAllSms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.ContactStatsFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListInteractor;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmService;

/**
 * Created by jonathancolon on 6/9/17.
 */

@Singleton
@Component(modules = {ApplicationContextModule.class, RepositoryModule.class})
public interface ApplicationComponent {

    void inject(MyApplication application);

    void inject(ContactListActivity contactListActivity);

    void inject(RealmService realmService);

    void inject(ContactListInteractor contactListInteractor);

    void inject(ContactSmsFragment smsFragment);

    void inject(ContactStatsFragment statsFragment);

    void inject(ContactInfoFragment infoFragment);

    void inject(ContactViewPagerActivity viewPagerActivity);

    void inject(GetAllSms getAllSms);
}
