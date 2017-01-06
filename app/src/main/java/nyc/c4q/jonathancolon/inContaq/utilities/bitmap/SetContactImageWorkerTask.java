package nyc.c4q.jonathancolon.inContaq.utilities.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.lang.ref.WeakReference;

/**
 * Created by jonathancolon on 1/5/17.
 */

public class SetContactImageWorkerTask extends AsyncTask<byte[], Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    Context context;

    public SetContactImageWorkerTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);

    }

    @Override
    protected void onPreExecute() {
        Log.i("SetImageWorkerTask", "Loading image...");
    }

    // Decode image in background.

    @Override
    protected Bitmap doInBackground(byte[]... params) {
        return decodeBitmap(params[0]);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap ret) {
        if (imageViewReference != null && ret != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(ret);
            }
        }
    }

    public static Bitmap decodeBitmap(byte[] bytes){

        ByteArrayInputStream decodedImage = new ByteArrayInputStream(bytes);
        return BitmapFactory.decodeStream(decodedImage);
    }
}