package nyc.c4q.jonathancolon.inContaq.utlities;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class PicassoUtils {

    private Context context;

    @Inject
    public PicassoUtils(@NonNull Context context) {
        this.context = context;
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
