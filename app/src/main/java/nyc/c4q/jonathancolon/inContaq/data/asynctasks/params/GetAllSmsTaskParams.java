package nyc.c4q.jonathancolon.inContaq.data.asynctasks.params;

import android.content.Context;

import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;

public class GetAllSmsTaskParams {
    public final Contact contact;
    public final Context context;

    public GetAllSmsTaskParams(Contact contact, Context context) {
        this.contact = contact;
        this.context = context;
    }


}
