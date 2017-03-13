package nyc.c4q.jonathancolon.inContaq.contactlist.recyclerviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nyc.c4q.jonathancolon.inContaq.R;


public class NotesFeedAdapter extends RecyclerView.Adapter<NotesFeedViewHolder> {
    @Override
    public NotesFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item, parent, false);
        return new NotesFeedViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(NotesFeedViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
