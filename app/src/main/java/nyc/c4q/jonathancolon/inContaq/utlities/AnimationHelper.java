package nyc.c4q.jonathancolon.inContaq.utlities;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import nyc.c4q.jonathancolon.inContaq.R;


public class AnimationHelper {
    private final Context context;

    public AnimationHelper(Context context) {
        this.context = context;
    }

    public void enterFab(View view) {
        Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.enter_fab);
        view.startAnimation(slideIn);
    }

    public void exitFab(View view) {
        Animation slideOut = AnimationUtils.loadAnimation(context, R.anim.enter_fab);
        view.startAnimation(slideOut);
    }
}
