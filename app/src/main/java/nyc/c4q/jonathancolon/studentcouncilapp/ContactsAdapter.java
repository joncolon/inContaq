package nyc.c4q.jonathancolon.studentcouncilapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jonathancolon on 10/27/16.
 */

public class ContactsAdapter extends RecyclerView.Adapter {
    private List<Contacts> contactsList;
    Context context;

    public ContactsAdapter(List<Contacts> contactsList, Context context){
        this.contactsList = contactsList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        return new ContactViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Contacts contacts = contactsList.get(position);
        ((ContactViewHolder) holder).mContactImage.setImageResource(contacts.getImage());
        ((ContactViewHolder) holder).mContactName.setText(contacts.getName());
        ((ContactViewHolder) holder).bind(contacts);

    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mContactImage;
        private TextView mContactName;
        Context context;

        public ContactViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            mContactImage = (ImageView) itemView.findViewById(R.id.contact_image);
            mContactName = (TextView) itemView.findViewById(R.id.contact_name);
            itemView.setOnClickListener(this);

        }

        public void bind (Contacts contacts){
            mContactName.setText(contacts.getName());
            Integer resource = contacts.getImage();
            if (resource != null){
                mContactImage.setImageResource(resource);
            }
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Contacts contacts = MainActivity.contactsList.get(position);
            Intent intent = new Intent(this.context, FragmentView.class);
            intent.putExtra(MainActivity.CONTACT_NAME_EXTRA, contacts.getName());
            intent.putExtra(MainActivity.CONTACT_IMAGE_EXTRA, contacts.getImage());
            intent.putExtra(MainActivity.CONTACT_NOTE_EXTRA, contacts.getNote());
            this.context.startActivity(intent);


        }
    }
}
