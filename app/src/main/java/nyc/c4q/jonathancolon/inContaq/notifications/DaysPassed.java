package nyc.c4q.jonathancolon.inContaq.notifications;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.model.ContactModel;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsUtils;

/**
 * Created by jonathancolon on 7/28/17.
 */

public class DaysPassed {
    static final long ONE_DAY = 86400000;
    SmsUtils smsUtils;

    @Inject
    public DaysPassed(@NonNull SmsUtils smsUtils) {
        this.smsUtils = smsUtils;
    }

    long daysSinceLastMsg(ContactModel contactModel) {
        long currentTime = System.currentTimeMillis();
        long lastMsg = smsUtils.getLastContactedDate(contactModel);
        if (lastMsg > 0) {
            long timeElapsed = currentTime - lastMsg;
            return timeElapsed / ONE_DAY;
        }
        return 0;
    }
}
