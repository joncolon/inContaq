package nyc.c4q.jonathancolon.inContaq.utilities.bitmap;

import android.widget.ImageView;

/**
 * Created by jonathancolon on 1/6/17.
 */

public class BitmapHelper {

    private void setContactImage(byte[] bytes, ImageView imageView){
        SetContactImageWorkerTask task = new SetContactImageWorkerTask(imageView);
        task.execute(bytes);
    }
}
