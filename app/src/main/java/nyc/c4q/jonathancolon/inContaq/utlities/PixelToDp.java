package nyc.c4q.jonathancolon.inContaq.utlities;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class PixelToDp {

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
