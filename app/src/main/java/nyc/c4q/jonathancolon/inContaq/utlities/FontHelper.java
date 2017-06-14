package nyc.c4q.jonathancolon.inContaq.utlities;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.di.Injector;


public class FontHelper {
    @Inject
    Context context;

    public FontHelper() {
        Injector.getApplicationComponent().inject(this);
    }

    public void applyFont(TextView textView) {
        context.getAssets();
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.contact_list_font));
        textView.setTypeface(customFont);
    }
}
