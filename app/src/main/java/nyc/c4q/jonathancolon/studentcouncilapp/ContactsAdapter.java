package nyc.c4q.jonathancolon.studentcouncilapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by jonathancolon on 10/27/16.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private List<Data> mData = Collections.emptyList();
    Context context;

    // ADAPTER CONSTRUCTORS
    public ContactsAdapter(List<Data> list, Context context) {
        this.mData = list;
        this.context = context;
    }

    @Override
    public ContactsAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        ContactViewHolder vh = new ContactViewHolder(itemView, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
        Data data = mData.get(position);
        holder.mContactImage.setImageResource(mData.get(position).imageId);
        holder.mContactName.setText(mData.get(position).name);
        holder.mContactName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Data contact = mData.get(position);
                Intent intent = new Intent(context, FragmentView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra(MainActivity.CONTACT_NAME_EXTRA, contact.name);
                intent.putExtra(MainActivity.CONTACT_IMAGE_EXTRA, contact.imageId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateList(List<Data> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addItem(Data data) {
        mData.add(data);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

//_____________________________________VIEWHOLDER___________________________________________________

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private ImageView mContactImage;
        private TextView mContactName;


        Context context;

        public ContactViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            mContactImage = (ImageView) itemView.findViewById(R.id.contact_image);
            mContactName = (TextView) itemView.findViewById(R.id.contact_name);

        }

        public void bind(Contacts contacts) {
            mContactName.setText(contacts.getName());
            Integer resource = contacts.getImage();
            if (resource != null) {
                mContactImage.setImageResource(resource);
            }
        }
    }
}
