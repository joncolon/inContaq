package nyc.c4q.jonathancolon.inContaq.contactlist.recyclerviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;


public class PhotoFeedAdapter extends RecyclerView.Adapter<PhotoFeedViewHolder> {

    // Store a member variable for the contacts
    private List<Contact> mContacts;
    // Store the context for easy access
    private Context mContext;

    public PhotoFeedAdapter(List<Contact> mContacts, Context mContext) {
        this.mContacts = mContacts;
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
        Contact contact = mContacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
