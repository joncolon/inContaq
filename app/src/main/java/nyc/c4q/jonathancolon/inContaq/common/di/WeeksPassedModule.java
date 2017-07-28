package nyc.c4q.jonathancolon.inContaq.common.di;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.notifications.WeeksPassed;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsHelper;

/**
 * Created by jonathancolon on 7/28/17.
 */

@Module
public class WeeksPassedModule {

    @Provides
    WeeksPassed providesWeeksPassed(SmsHelper smsHelper) {
        return new WeeksPassed(smsHelper);
    }
}
