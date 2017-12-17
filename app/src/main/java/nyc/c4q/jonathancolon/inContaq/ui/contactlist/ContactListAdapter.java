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
import nyc.c4q.jonathancolon.inContaq.model.ContactModel;
import nyc.c4q.jonathancolon.inContaq.utlities.FontUtils;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoUtils;

import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isNull;


class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {
    private final Listener listener;
    private final ParallaxViewController parallaxViewController = new ParallaxViewController();
    private final Context context;
    private final PicassoUtils picassoUtils;
    private List<ContactModel> contactModelList;


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
        ContactModel contactModel = contactModelList.get(position);
        holder.bind(contactModel);
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
        return contactModelList.size();
    }


    public void setData(List<ContactModel> contactModels) {
        this.contactModelList = contactModels;
        notifyDataSetChanged();
    }

    interface Listener {
        void onContactClicked(ContactModel contactModel);
        void onContactLongClicked(ContactModel contactModel);
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

        void bind(ContactModel c) {
            final ContactModel contactModel = c;
            mContactName.setText(contactModel.getFirstName() + " " + contactModel.getLastName());
            mContactInitials.setText((contactModel.getFirstName().substring(0, 1).toUpperCase()));

            displayBackgroundImage(contactModel);
            displayContactImage(contactModel);

            itemView.setOnClickListener(v -> {
                listener.onContactClicked(contactModel);
            });
            itemView.setOnLongClickListener(v -> {
                 listener.onContactLongClicked(contactModel);
                return true;
            });
        }

        private void displayBackgroundImage(ContactModel contactModel) {
            if (!isNull(contactModel.getBackgroundImage())) {
                picassoUtils.loadImageFromString(contactModel.getBackgroundImage(), mBackGroundImage);
            } else {
                mBackGroundImage.refreshDrawableState();
                mBackGroundImage.setImageDrawable(null);
            }
        }

        private void displayContactImage(ContactModel contactModel) {
            if (!isNull(contactModel.getContactImage())) {
                picassoUtils.loadImageFromString(contactModel.getContactImage(), mContactImage);
            } else {
                mContactImage.refreshDrawableState();
                mContactImage.setImageDrawable(null);
            }
        }
    }

}
