package nyc.c4q.jonathancolon.inContaq.contactlist.recyclerviews;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import nyc.c4q.jonathancolon.inContaq.R;

/**
 * Created by wesniemarcelin on 3/12/17.
 */

public class PhotoFeedViewHolder extends RecyclerView.ViewHolder {
    //    TextView photoText;
    ImageView photoView;
    private String contactPhoto;

    View view;
    public PhotoFeedViewHolder(View itemView) {
        super(itemView);
        view = itemView;
//        photoText = (TextView) itemView.findViewById(R.id.photo_number);
        photoView = (ImageView) itemView.findViewById(R.id.contact_photo);

    }

    public void bind(String contactPhoto) {
//        Photo photo = new Photo();
//        int resID = getResources().getIdentifier("img"+number, "raw", getPackageName());

        this.contactPhoto = contactPhoto;
//        photoText.setText("1");
//        photoView.setImageResource(Integer.parseInt(photo.getImagePath());
    }
}
