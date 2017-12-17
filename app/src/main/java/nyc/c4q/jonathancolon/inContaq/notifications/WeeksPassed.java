package nyc.c4q.jonathancolon.inContaq.notifications;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.model.ContactModel;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsUtils;

/**
 * Created by jonathancolon on 7/28/17.
 */

public class WeeksPassed {

    private static final long ONE_WEEK = 604800000;
    private static final long TWO_WEEKS = 1209600000;
    private static final long THREE_WEEKS = 1814400000;
    private SmsUtils smsUtils;

    @Inject
    public WeeksPassed(@NonNull SmsUtils smsUtils) {
        this.smsUtils = smsUtils;
    }

    boolean hasWeekPassed(ContactModel c) {
        return System.currentTimeMillis() -
                smsUtils.getLastContactedDate(c) > ONE_WEEK;
    }

    boolean hasTwoWeeksPassed(ContactModel c) {
        return System.currentTimeMillis() -
                smsUtils.getLastContactedDate(c) > TWO_WEEKS;
    }

    boolean hasThreeWeeksPassed(ContactModel c) {
        return System.currentTimeMillis() -
                smsUtils.getLastContactedDate(c) > THREE_WEEKS;
    }

}
