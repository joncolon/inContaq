package nyc.c4q.jonathancolon.inContaq.contactlist;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import nyc.c4q.jonathancolon.inContaq.R;


public class Animations {
    private final Context context;

    public Animations(Context context) {
        this.context = context;
    }

    public void fadeIn(View view){
        Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        view.startAnimation(fadeIn);
    }

    public void enterFab(View view){
        Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.enter_fab);
        view.startAnimation(slideIn);
    }

    public void exitFab(View view){
        Animation slideOut = AnimationUtils.loadAnimation(context, R.anim.enter_fab);
        view.startAnimation(slideOut);

    }
}
