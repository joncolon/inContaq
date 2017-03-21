package nyc.c4q.jonathancolon.inContaq.contactlist;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoHelper {
    private final Context context;

    public PicassoHelper(Context context) {
        this.context = context;
    }

    public void preloadImages(int string){
        Picasso.with(context).load(string).fetch();
    }

    public void loadImageFromUri(Uri uri, ImageView imageView){
        Picasso.with(context).load(uri).resize(1000, 1000).centerInside().into(imageView);
    }

    public void loadImageFromResourceId(int resourceId, ImageView imageView){
        Picasso.with(context).load(resourceId).resize(1000, 1000).centerInside().into(imageView);
    }

}
