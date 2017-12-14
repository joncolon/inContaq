package nyc.c4q.jonathancolon.inContaq.common.dagger;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.notifications.WeeksPassed;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsUtils;

/**
 * Created by jonathancolon on 7/28/17.
 */

@Module
public class WeeksPassedModule {

    @Provides
    WeeksPassed providesWeeksPassed(SmsUtils smsUtils) {
        return new WeeksPassed(smsUtils);
    }
}
