package nyc.c4q.jonathancolon.studentcouncilapp.contactlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.beautifulparallax.ParallaxViewController;

import java.io.ByteArrayInputStream;
import java.util.List;

import nyc.c4q.jonathancolon.studentcouncilapp.R;

/**
 * Created by jonathancolon on 10/27/16.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    Context context;
    Listener listener;
    List<Contact> contactList;

    ParallaxViewController parallaxViewController = new ParallaxViewController();

    // ADAPTER CONSTRUCTORS
    public ContactsAdapter(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parallaxViewController.registerImageParallax(recyclerView);
    }





    @Override
    public ContactsAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        ContactViewHolder vh = new ContactViewHolder(itemView);
        parallaxViewController.imageParallax(vh.mBackGroundImage);

        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.bind(contact);

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void updateList(List<Contact> contacts) {
        contactList = contacts;
        notifyDataSetChanged();
    }

    public void addItem(Contact contact) {
        contactList.add(contact);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        contactList.remove(position);
        notifyItemRemoved(position);
    }

//_____________________________________VIEWHOLDER___________________________________________________

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private ImageView mBackGroundImage, mContactImage ;
        private TextView mContactName, mContactInitials;


        Context context;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mBackGroundImage = (ImageView) itemView.findViewById(R.id.background_image);
            mContactName = (TextView) itemView.findViewById(R.id.contact_name);
            mContactImage = (ImageView) itemView.findViewById(R.id.contact_image);
            mContactInitials = (TextView) itemView.findViewById(R.id.contact_initials);

        }

        public void bind(Contact c) {
            final Contact contact = c;

            mContactName.setText(contact.getFirstName() + " " + contact.getLastName());
            mContactInitials.setText((contact.getFirstName().substring(0,1).toUpperCase()));

            //Integer contactImage = contact.getContactImage();

            if (contact.getBackgroundImage() != null){
                byte[] backgroundImage = contact.getBackgroundImage();
                ByteArrayInputStream imageStream = new ByteArrayInputStream(backgroundImage);
                Bitmap decodedImage = BitmapFactory.decodeStream(imageStream);

                mBackGroundImage.setImageBitmap(decodedImage);
            }

            if (contact.getContactImage() != null){
                byte[] contactImage = contact.getContactImage();
                ByteArrayInputStream imageStream = new ByteArrayInputStream(contactImage);
                Bitmap decodedImage = BitmapFactory.decodeStream(imageStream);


                mContactImage.setImageBitmap(decodedImage);
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onContactClicked(contact);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onContactLongClicked(contact);
                    return true;
                }
            });
        }
    }

    public interface Listener {
        void onContactClicked(Contact contact);
        void onContactLongClicked(Contact contact);
    }

    public void setData(List<Contact> contacts) {
        this.contactList = contacts;
        notifyDataSetChanged();

    }

}
