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

    public void loadImageFromUri(Uri uri, ImageView imageView){
        Picasso.with(context).load(uri).into(imageView);
    }

    public void loadImageFromString(String uriString, ImageView imageView){
        Picasso.with(context).load(Uri.parse(uriString)).into(imageView);
    }

}
