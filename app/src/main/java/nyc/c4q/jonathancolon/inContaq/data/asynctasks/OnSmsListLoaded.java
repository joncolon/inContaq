package nyc.c4q.jonathancolon.inContaq.data.asynctasks;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.sms.model.Sms;

/**
 * Created by jonathancolon on 5/9/17.
 */

public interface OnSmsListLoaded {
    void onSmsListLoaded(ArrayList<Sms> arrayList);
}
