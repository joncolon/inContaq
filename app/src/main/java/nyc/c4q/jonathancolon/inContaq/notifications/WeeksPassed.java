package nyc.c4q.jonathancolon.inContaq.notifications;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsHelper;

/**
 * Created by jonathancolon on 7/28/17.
 */

public class WeeksPassed {

    private static final long ONE_WEEK = 604800000;
    private static final long TWO_WEEKS = 1209600000;
    private static final long THREE_WEEKS = 1814400000;
    private SmsHelper smsHelper;

    @Inject
    public WeeksPassed(@NonNull SmsHelper smsHelper) {
        this.smsHelper = smsHelper;
    }

    boolean hasWeekPassed(Contact c) {
        return System.currentTimeMillis() -
                smsHelper.getLastContactedDate(c) > ONE_WEEK;
    }

    boolean hasTwoWeeksPassed(Contact c) {
        return System.currentTimeMillis() -
                smsHelper.getLastContactedDate(c) > TWO_WEEKS;
    }

    boolean hasThreeWeeksPassed(Contact c) {
        return System.currentTimeMillis() -
                smsHelper.getLastContactedDate(c) > THREE_WEEKS;
    }

}
