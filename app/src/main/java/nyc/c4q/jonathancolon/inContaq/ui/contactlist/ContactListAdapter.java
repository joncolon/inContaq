package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.beautifulparallax.ParallaxViewController;

import java.util.List;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.utlities.FontHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoHelper;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {
    private final Listener listener;
    private final ParallaxViewController parallaxViewController = new ParallaxViewController();
    private List<Contact> contactList;

    public ContactListAdapter(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parallaxViewController.registerImageParallax(recyclerView);
    }

    @Override
    public ContactListAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.itemview_contactlist_rv, parent, false);

        FontHelper fontHelper = new FontHelper();
        ContactViewHolder vh = new ContactViewHolder(itemView, fontHelper);
        parallaxViewController.imageParallax(vh.mBackGroundImage);
        return vh;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.bind(contact);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


    public void setData(List<Contact> contacts) {
        this.contactList = contacts;
        notifyDataSetChanged();
    }

    private boolean hasContactImage(Contact contact) {
        return contact.getContactImage() != null;
    }

    private boolean hasBackgroundImage(Contact contact) {
        return contact.getBackgroundImage() != null;
    }

    interface Listener {
        void onContactClicked(Contact contact);
    }

    //_____________________________________VIEWHOLDER_______________________________________________
    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mBackGroundImage;
        private final ImageView mContactImage;
        private final TextView mContactName;
        private final TextView mContactInitials;


        ContactViewHolder(View itemView, FontHelper fontHelper) {
            super(itemView);
            mBackGroundImage = (ImageView) itemView.findViewById(R.id.background_image);
            mContactName = (TextView) itemView.findViewById(R.id.name);
            mContactImage = (ImageView) itemView.findViewById(R.id.contact_image);
            mContactInitials = (TextView) itemView.findViewById(R.id.contact_initials);
            fontHelper.applyFont(mContactName);
            fontHelper.applyFont(mContactInitials);
        }

        void bind(Contact c) {
            final Contact contact = c;
            mContactName.setText(contact.getFirstName() + " " + contact.getLastName());
            mContactInitials.setText((contact.getFirstName().substring(0, 1).toUpperCase()));
            PicassoHelper ph = new PicassoHelper();
            displayBackgroundImage(contact, ph);
            displayContactImage(contact, ph);

            itemView.setOnClickListener(v -> listener.onContactClicked(contact));
        }

        private void displayContactImage(Contact contact, PicassoHelper ph) {
            if (hasContactImage(contact)) {
                ph.loadImageFromString(contact.getContactImage(), mContactImage);
            } else {
                mContactImage.refreshDrawableState();
                mContactImage.setImageDrawable(null);
            }
        }

        private void displayBackgroundImage(Contact contact, PicassoHelper ph) {
            if (hasBackgroundImage(contact)) {
                ph.loadImageFromString(contact.getBackgroundImage(), mBackGroundImage);
            } else {
                mBackGroundImage.refreshDrawableState();
                mBackGroundImage.setImageDrawable(null);
            }
        }
    }

}
