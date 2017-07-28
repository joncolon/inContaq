package nyc.c4q.jonathancolon.inContaq.notifications;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsHelper;

/**
 * Created by jonathancolon on 7/28/17.
 */

public class DaysPassed {
    static final long ONE_DAY = 86400000;
    SmsHelper smsHelper;

    @Inject
    public DaysPassed(@NonNull SmsHelper smsHelper) {
        this.smsHelper = smsHelper;
    }

    long daysSinceLastMsg(Contact contact) {
        long currentTime = System.currentTimeMillis();
        long lastMsg = smsHelper.getLastContactedDate(contact);
        if (lastMsg > 0) {
            long timeElapsed = currentTime - lastMsg;
            return timeElapsed / ONE_DAY;
        }
        return 0;
    }
}
