package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.faceapi.GalleryImagesHelper;
import nyc.c4q.jonathancolon.inContaq.contactlist.recyclerviews.PhotoFeedAdapter;

public class PhotoFeedActivity extends AppCompatActivity {
    ArrayList<Contact> contacts;
    RecyclerView rvContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_feed);
        rvContacts = (RecyclerView) findViewById(R.id.photo_rv);

        GalleryImagesHelper.getAllImagesPath(getApplicationContext());


        PhotoFeedAdapter adapter = new PhotoFeedAdapter(contacts, getApplicationContext());
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }
}
