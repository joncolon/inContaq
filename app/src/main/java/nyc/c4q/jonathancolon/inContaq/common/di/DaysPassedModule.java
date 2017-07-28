package nyc.c4q.jonathancolon.inContaq.common.di;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.notifications.DaysPassed;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsHelper;


@Module
class DaysPassedModule {

    @Provides
    DaysPassed providesDaysPassed(SmsHelper smsHelper){
        return new DaysPassed(smsHelper);
    }
}
