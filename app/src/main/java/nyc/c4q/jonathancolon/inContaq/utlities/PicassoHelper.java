package nyc.c4q.jonathancolon.inContaq.utlities;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.di.Injector;

public class PicassoHelper {
    @Inject
    Context context;

    public PicassoHelper() {
        Injector.getApplicationComponent().inject(this);
    }

    public void preloadImages(String string) {
        Picasso.with(context).load(string).fetch();
    }

    public void loadImageFromUri(Uri uri, ImageView imageView) {
        Picasso.with(context).load(uri).resize(1000, 1000).centerInside().into(imageView);
    }

    public void loadImageFromString(String uriString, ImageView imageView) {
        Picasso.with(context).load(Uri.parse(uriString)).resize(1000, 1000).centerInside().into(imageView);
    }
}
