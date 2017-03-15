package nyc.c4q.jonathancolon.inContaq.contactlist.recyclerviews;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;

/**
 * Created by wesniemarcelin on 3/12/17.
 */

public class PhotoFeedViewHolder extends RecyclerView.ViewHolder {
    TextView photoText;
    ImageView photoView;
    private Contact contact;
    View view;
    public PhotoFeedViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        photoText = (TextView) itemView.findViewById(R.id.photo_number);
        photoView = (ImageView) itemView.findViewById(R.id.contact_photo);

    }

    public void bind(Contact contact) {
        this.contact = contact;
        photoText.setText("1");
        photoView.setImageResource(R.drawable.bg_polaroid_texture);
    }
}
