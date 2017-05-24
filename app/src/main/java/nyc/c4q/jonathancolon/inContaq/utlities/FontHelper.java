package nyc.c4q.jonathancolon.inContaq.utlities;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import nyc.c4q.jonathancolon.inContaq.R;


public class FontHelper {
    Context context;

    public FontHelper(Context context) {
        this.context = context;
    }

    public void applyFont(TextView textView) {
        context.getAssets();
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.contact_list_font));
        textView.setTypeface(customFont);
    }
}
