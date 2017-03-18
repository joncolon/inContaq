package nyc.c4q.jonathancolon.inContaq.utlities.font;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import nyc.c4q.jonathancolon.inContaq.R;


public class CustomFont {
    Context context;

    public CustomFont(Context context) {
        this.context = context;
    }

    public void setCustomFont(TextView textView) {
        context.getAssets();
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.contact_list_font));
        textView.setTypeface(customFont);
    }
}
