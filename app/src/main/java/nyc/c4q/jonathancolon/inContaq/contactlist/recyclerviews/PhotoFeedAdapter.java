package nyc.c4q.jonathancolon.inContaq.contactlist.recyclerviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.R;


public class PhotoFeedAdapter extends RecyclerView.Adapter<PhotoFeedViewHolder> {

    // Store the context for easy access
    private Context mContext;
    // Store a member variable for the contacts
    private ArrayList<String> mPhotos;
    private ArrayList<Photo> photoArrayList;

    public PhotoFeedAdapter(Context mContext, ArrayList<Photo> photoArrayList) {
        this.mContext = mContext;
        this.photoArrayList = photoArrayList;
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
        Photo photo = photoArrayList.get(position);
        File f = new File(photo.getImagePath());
//        holder.bind(contactPhoto);
        Picasso.with(getContext()).load(f).into(holder.photoView);
    }

    @Override
    public int getItemCount() {
        return photoArrayList.size();
    }
}