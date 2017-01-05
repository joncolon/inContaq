package nyc.c4q.jonathancolon.studentcouncilapp.bitmap.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.lang.ref.WeakReference;

/**
 * Created by jonathancolon on 1/5/17.
 */

public class BitmapFromBytesWorkerTask extends AsyncTask<byte[], Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private byte[] data = new byte[10];

    public BitmapFromBytesWorkerTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);

    }

    // Decode image in background.

    @Override
    protected Bitmap doInBackground(byte[]... params) {
        return decodeBitmap(data);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public static Bitmap decodeBitmap(byte[] bytes){

        ByteArrayInputStream decodedImage = new ByteArrayInputStream(bytes);
        return BitmapFactory.decodeStream(decodedImage);
    }
}