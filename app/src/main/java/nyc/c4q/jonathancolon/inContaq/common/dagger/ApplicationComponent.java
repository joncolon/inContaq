package nyc.c4q.jonathancolon.inContaq.common.dagger;

import javax.inject.Singleton;

import dagger.Component;
import nyc.c4q.jonathancolon.inContaq.common.application.App;
import nyc.c4q.jonathancolon.inContaq.notifications.ContactNotificationService;
import nyc.c4q.jonathancolon.inContaq.notifications.DaysPassed;
import nyc.c4q.jonathancolon.inContaq.notifications.WeeksPassed;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.ContactDetailsActivity;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactinfo.ContactInfoFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.ContactSmsFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.ContactStatsFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListPresenter;
import nyc.c4q.jonathancolon.inContaq.utlities.RxBus;


@Singleton
@Component(modules =
        {
                ApplicationContextModule.class,
                RealmServiceModule.class,
                PicassoModule.class,
                ContentResolverModule.class,
                PhoneNumberHelperModule.class,
                PhoneNumberUtilModule.class,
                SmsHelperModule.class,
                RxBusModule.class,
                WeeksPassedModule.class,
                DaysPassedModule.class,
                SharedPrefModule.class
        })

public interface ApplicationComponent {

    RxBus getRxBus();

    void inject(App application);

    void inject(ContactListActivity contactListActivity);

    void inject(ContactListPresenter contactListPresenter);

    void inject(ContactSmsFragment smsFragment);

    void inject(ContactStatsFragment statsFragment);

    void inject(ContactInfoFragment infoFragment);

    void inject(ContactDetailsActivity viewPagerActivity);

    void inject(ContactNotificationService contactNotificationService);

    void inject(WeeksPassed weeksPassed);

    void inject(DaysPassed daysPassed);
}
