package nyc.c4q.jonathancolon.inContaq.utlities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

import static android.content.Context.MODE_PRIVATE;

public class MaterialTapHelper {

    private Context context;
    private Activity activity;
    private SharedPreferences prefs;
    private RealmResults<Contact> contactList;

    public MaterialTapHelper(Context context, Activity activity,
                             SharedPreferences prefs, RealmResults<Contact> contactList) {
        this.context = context;
        this.activity = activity;
        this.prefs = prefs;
        this.contactList = contactList;
    }

    public void addFirstContactPrompt() {
        prefs = context.getSharedPreferences("inContaq", MODE_PRIVATE);
        if (prefs.getBoolean("intro", true)) {
            if (ObjectUtils.isEmptyList(contactList)) {
                new MaterialTapTargetPrompt.Builder(activity)
                        .setTarget(activity.findViewById(R.id.fab_add_contact))
                        .setPrimaryText(R.string.add_first_contact)
                        .setSecondaryText(R.string.tap_here)
                        .setBackgroundColour(context.getColor(R.color.charcoal))
                        .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                            @Override
                            public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                prefs.edit().putBoolean("intro", false).apply();
                            }

                            @Override
                            public void onHidePromptComplete() {
                            }
                        })
                        .show();
            }

        }
    }
}
