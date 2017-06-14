package nyc.c4q.jonathancolon.inContaq.utlities;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by jonathancolon on 6/10/17.
 */


public class UiUtils {
//todo research timber see if its worth incorporating
//    public static void handleThrowable(Throwable throwable) {
//        Timber.e(throwable, throwable.toString());
//    }

    public static void showSnackbar(View view, String message, int length) {
        Snackbar.make(view, message, length).setAction("Action", null).show();
    }


}
