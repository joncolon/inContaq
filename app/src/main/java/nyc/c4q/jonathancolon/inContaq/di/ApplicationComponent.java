package nyc.c4q.jonathancolon.inContaq.di;

import javax.inject.Singleton;

import dagger.Component;
import nyc.c4q.jonathancolon.inContaq.application.AppController;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.ContactDetailsActivity;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactinfo.ContactInfoFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.ContactSmsFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.data.GetAllSms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.ContactStatsFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.db.RealmService;
import nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListPresenter;

/**
 * Created by jonathancolon on 6/9/17.
 */

@Singleton
@Component(modules = {ApplicationContextModule.class, RealmModule.class, MainModule.class})
public interface ApplicationComponent {

    void inject(AppController application);

    void inject(ContactListActivity contactListActivity);

    void inject(ContactListPresenter contactListPresenter);

    void inject(RealmService realmService);

    void inject(ContactSmsFragment smsFragment);

    void inject(ContactStatsFragment statsFragment);

    void inject(ContactInfoFragment infoFragment);

    void inject(ContactDetailsActivity viewPagerActivity);

    void inject(GetAllSms getAllSms);
}
