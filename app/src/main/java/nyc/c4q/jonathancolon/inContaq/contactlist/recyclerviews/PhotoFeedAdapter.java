package nyc.c4q.jonathancolon.inContaq.contactlist.recyclerviews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.faceapi.GalleryImagesHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.sqlite.SqlHelper;


public class PhotoFeedAdapter extends RecyclerView.Adapter<PhotoFeedViewHolder> {

    // Store a member variable for the contacts
    private ArrayList<String> mPhotos;
    // Store the context for easy access
    private Context mContext;
    private SQLiteDatabase db;


    public PhotoFeedAdapter(List<Contact> mContacts, Context mContext) {
        this.mPhotos = GalleryImagesHelper.getAllImagesPath(mContext);
        this.mContext = mContext;
    }

    private Context getContext(){
        return mContext;
    }
    @Override
    public PhotoFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        return new PhotoFeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoFeedViewHolder holder, int position) {
        String contactPhoto = mPhotos.get(position);
        holder.bind(contactPhoto);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }
}
