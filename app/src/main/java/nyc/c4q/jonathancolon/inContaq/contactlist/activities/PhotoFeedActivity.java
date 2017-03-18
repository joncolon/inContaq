package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.faceapi.FaceDetectionCall;
import nyc.c4q.jonathancolon.inContaq.contactlist.faceapi.GalleryImagesHelper;
import nyc.c4q.jonathancolon.inContaq.contactlist.faceapi.ImageHelper;
import nyc.c4q.jonathancolon.inContaq.contactlist.recyclerviews.Photo;
import nyc.c4q.jonathancolon.inContaq.contactlist.recyclerviews.PhotoFeedAdapter;

public class PhotoFeedActivity extends AppCompatActivity {
    RecyclerView rvContacts;
    ArrayList<String> stringArrayList;
    ArrayList<Photo> photoArrayList;
    PhotoFeedAdapter adapter;
    private Bitmap mBitmap;
    private Uri mImageUri;
    RecyclerView.LayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_feed);
        Log.d("jjjjj", "onCreate: " + this.getClass().getSimpleName());

        rvContacts = (RecyclerView) findViewById(R.id.photo_rv);
        mImageUri = savedInstanceState.getParcelable("ImageUri");
        mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                mImageUri, getContentResolver());
        llm = new GridLayoutManager(getApplicationContext(), 2);
        stringArrayList = GalleryImagesHelper.getAllImagesPath(getApplicationContext());
        photoArrayList = new ArrayList<>();

        for (int i = 0; i < stringArrayList.size(); i++) {
            Photo photo = new Photo();
            photo.setImagePath(stringArrayList.get(i));

            photoArrayList.add(photo);
        }

        adapter = new PhotoFeedAdapter(getApplicationContext(), photoArrayList);
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(llm);
        adapter.notifyDataSetChanged();

    }

    public void detect(View view) {
        // Put the image into an input stream for detection.

        Log.d("Detect", "detect method" + view);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Log.d("OutputStream", "output " + output);
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
        Log.d("inputStream callled ", "input stream name" + inputStream);

        // Start a background task to detect faces in the image.
        new FaceDetectionCall().execute(inputStream);

        // Prevent button click during detecting.
        setAllButtonsEnabledStatus(false);
    }

    private void setAllButtonsEnabledStatus(boolean isEnabled) {
        Button selectImageButton = (Button) findViewById(R.id.select_image);
        selectImageButton.setEnabled(isEnabled);

        Button detectButton = (Button) findViewById(R.id.detect);
        detectButton.setEnabled(isEnabled);

        Button ViewLogButton = (Button) findViewById(R.id.view_log);
        ViewLogButton.setEnabled(isEnabled);
    }
}