package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.faceapi.GalleryImagesHelper;
import nyc.c4q.jonathancolon.inContaq.contactlist.recyclerviews.Photo;
import nyc.c4q.jonathancolon.inContaq.contactlist.recyclerviews.PhotoFeedAdapter;

public class PhotoFeedActivity extends AppCompatActivity {
    RecyclerView rvContacts;
    ArrayList<String> stringArrayList;
    ArrayList<Photo> photoArrayList;
    PhotoFeedAdapter adapter;
    RecyclerView.LayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_feed);

        rvContacts = (RecyclerView) findViewById(R.id.photo_rv);
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
}