package nyc.c4q.jonathancolon.inContaq.notifications;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;

/**
 * Created by jonathancolon on 3/9/17.
 */

public class CustomAdapter extends ArrayAdapter<String> {
    public CustomAdapter(@NonNull Context context, @LayoutRes int resource, Contact contact) {
        super(context, resource);
    }

    public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, Contact contact) {
        super(context, resource, textViewResourceId);
    }

    public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull String[] objects, Contact contact) {
        super(context, resource, objects);
    }

    public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull String[] objects, Contact contact) {
        super(context, resource, textViewResourceId, objects);
    }

    public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects, Contact contact) {
        super(context, resource, objects);
    }

    public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<String> objects, Contact contact) {
        super(context, resource, textViewResourceId, objects);
    }
}
