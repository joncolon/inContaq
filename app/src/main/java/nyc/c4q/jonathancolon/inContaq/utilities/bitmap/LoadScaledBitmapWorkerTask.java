package nyc.c4q.jonathancolon.inContaq.utilities.bitmap;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.lang.ref.WeakReference;

/**
 * Created by jonathancolon on 1/6/17.
 */

public class LoadScaledBitmapWorkerTask extends AsyncTask<Uri, Void, Bitmap> {
    private final WeakReference<Uri> uriWeakReference;
    private final Context context;

    public LoadScaledBitmapWorkerTask(Uri uri, Context mContext) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        uriWeakReference = new WeakReference<Uri>(uri);
        context = mContext;
    }

    @Override
    protected Bitmap doInBackground(Uri... params) {
        Uri uri = params[0];
        return decodeBitmapFromFilePath(uri, 275, 275);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            final Uri uri = uriWeakReference.get();
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight,
                                     String filepath) {

        options = new BitmapFactory.Options();

        // Raw height and width of image
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        float height = options.outHeight;
        float width = options.outWidth;
        double inSampleSize = 1D;

        if (height > reqHeight || width > reqWidth) {
            int halfHeight = (int) (height / 2);
            int halfWidth = (int) (width / 2);

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return (int) inSampleSize;
    }

    public Bitmap decodeBitmapFromFilePath(Uri uri,
                                           int reqWidth, int reqHeight) {

        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getApplicationContext().getContentResolver().query(uri,
                filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgDecodableString, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight,
                imgDecodableString);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgDecodableString, options);
    }
}
