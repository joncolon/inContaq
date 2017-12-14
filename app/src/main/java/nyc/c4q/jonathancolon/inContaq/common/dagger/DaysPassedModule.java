package nyc.c4q.jonathancolon.inContaq.common.dagger;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.notifications.DaysPassed;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsUtils;


@Module
class DaysPassedModule {

    @Provides
    DaysPassed providesDaysPassed(SmsUtils smsUtils){
        return new DaysPassed(smsUtils);
    }
}
