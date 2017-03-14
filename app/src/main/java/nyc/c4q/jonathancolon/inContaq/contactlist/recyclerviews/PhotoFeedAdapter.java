package nyc.c4q.jonathancolon.inContaq.contactlist.recyclerviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nyc.c4q.jonathancolon.inContaq.R;


public class PhotoFeedAdapter extends RecyclerView.Adapter<PhotoFeedViewHolder> {
    @Override
    public PhotoFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        return new PhotoFeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoFeedViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
