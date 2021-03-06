package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.content.Context;
import android.support.annotation.NonNull;
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
import nyc.c4q.jonathancolon.inContaq.utlities.FontUtils;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoUtils;

import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isNull;


class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {
    private final Listener listener;
    private final ParallaxViewController parallaxViewController = new ParallaxViewController();
    private final Context context;
    private final PicassoUtils picassoUtils;
    private List<Contact> contactList;


    ContactListAdapter(Listener listener, @NonNull Context context,
                       @NonNull PicassoUtils picassoUtils) {
        this.context = context;
        this.listener = listener;
        this.picassoUtils = picassoUtils;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parallaxViewController.registerImageParallax(recyclerView);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.itemview_contactlist_rv, parent, false);

        FontUtils fontUtils = new FontUtils(context);
        ContactViewHolder vh = new ContactViewHolder(itemView, fontUtils);
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

    interface Listener {
        void onContactClicked(Contact contact);
        void onContactLongClicked(Contact contact);
    }

    //_____________________________________VIEWHOLDER_______________________________________________

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mBackGroundImage;
        private final ImageView mContactImage;
        private final TextView mContactName;
        private final TextView mContactInitials;


        ContactViewHolder(View itemView, FontUtils fontUtils) {
            super(itemView);
            mBackGroundImage = (ImageView) itemView.findViewById(R.id.background_image);
            mContactName = (TextView) itemView.findViewById(R.id.name);
            mContactImage = (ImageView) itemView.findViewById(R.id.contact_image);
            mContactInitials = (TextView) itemView.findViewById(R.id.contact_initials);
            fontUtils.applyFont(mContactName);
            fontUtils.applyFont(mContactInitials);
        }

        void bind(Contact c) {
            final Contact contact = c;
            mContactName.setText(contact.getFirstName() + " " + contact.getLastName());
            mContactInitials.setText((contact.getFirstName().substring(0, 1).toUpperCase()));

            displayBackgroundImage(contact);
            displayContactImage(contact);

            itemView.setOnClickListener(v -> {
                listener.onContactClicked(contact);
            });
            itemView.setOnLongClickListener(v -> {
                 listener.onContactLongClicked(contact);
                return true;
            });
        }

        private void displayBackgroundImage(Contact contact) {
            if (!isNull(contact.getBackgroundImage())) {
                picassoUtils.loadImageFromString(contact.getBackgroundImage(), mBackGroundImage);
            } else {
                mBackGroundImage.refreshDrawableState();
                mBackGroundImage.setImageDrawable(null);
            }
        }

        private void displayContactImage(Contact contact) {
            if (!isNull(contact.getContactImage())) {
                picassoUtils.loadImageFromString(contact.getContactImage(), mContactImage);
            } else {
                mContactImage.refreshDrawableState();
                mContactImage.setImageDrawable(null);
            }
        }
    }

}
