package nyc.c4q.jonathancolon.inContaq.utilities.sms;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.beautifulparallax.ParallaxViewController;

import java.util.List;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;

import static android.content.ContentValues.TAG;

/**
 * Created by jonathancolon on 10/27/16.
 */

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsViewHolder> {
    Context context;
    Listener listener;
    List<Sms> smsList;
    Contact contact;


    ParallaxViewController parallaxViewController = new ParallaxViewController();

    // ADAPTER CONSTRUCTORS
    public SmsAdapter(Listener listener, Contact contact) {
        this.listener = listener;
        this.contact = contact;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parallaxViewController.registerImageParallax(recyclerView);
    }

    @Override
    public SmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_row, parent, false);
        SmsViewHolder vh = new SmsViewHolder(itemView);
        context = parent.getContext();


        return vh;
    }

    @Override
    public void onBindViewHolder(SmsViewHolder holder, int position) {
        Sms smsDetail = smsList.get(position);


        holder.bind(smsDetail);

        Log.d(TAG, "onBindViewHolder: " + smsDetail.getAddress());
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public void updateList(List<Sms> smslist) {
        smsList = smslist;
        notifyDataSetChanged();
    }

    public void addItem(Sms smsDetail) {
        smsList.add(smsDetail);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        smsList.remove(position);
        notifyItemRemoved(position);
    }

//_____________________________________VIEWHOLDER___________________________________________________

    public class SmsViewHolder extends RecyclerView.ViewHolder {
        private TextView senderId, myID,senderCount,messageRecieved,timeStamp,timeDate, messageSent, type;
        private LinearLayout linearLayout;
        private CardView cardBubble;


        public SmsViewHolder(View itemView) {
            super(itemView);

            senderId = (TextView) itemView.findViewById(R.id.senderId);
            messageRecieved = (TextView) itemView.findViewById(R.id.messageDetails);
            type = (TextView) itemView.findViewById(R.id.type);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.chat_view);
            cardBubble = (CardView) itemView.findViewById(R.id.chatBubble);

            timeDate = (TextView) itemView.findViewById(R.id.timeDate);
        }

        public void bind(Sms sms) {
            final Sms smsDetail = sms;
            String time = SmsHelper.smsDateFormat(Long.parseLong(sms.getTime()));
            senderId.setText(sms.getAddress());
            timeDate.setText(time);
            messageRecieved.setText(sms.getMsg());
            messageRecieved.setMovementMethod(LinkMovementMethod.getInstance());
            type.setText(sms.getType());



            if (sms.getType().toString().equals("1")){
                if (contact.getCellPhoneNumber() != null){
                    type.setText(contact.getFirstName() + " " + contact.getLastName());
                    linearLayout.setGravity(Gravity.START);
                    cardBubble.setCardBackgroundColor(Color.parseColor("#5B9CAC"));
                    messageRecieved.setTextColor(Color.parseColor("#FFFFFF"));
                    timeDate.setTextColor(Color.parseColor("#FFFFFF"));

                } else {

                }
            } else{
                linearLayout.setGravity(Gravity.END);
                cardBubble.setCardBackgroundColor(Color.parseColor("#ffffff"));
                messageRecieved.setTextColor(Color.parseColor("#0E587A"));
                timeDate.setTextColor(Color.parseColor("#0E587A"));

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onContactClicked(smsDetail);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onContactLongClicked(smsDetail);
                    return true;
                }
            });
        }
    }

    public interface Listener {
        void onContactClicked(Sms smsDetail);
        void onContactLongClicked(Sms smsDetail);
    }

    public void setData(List<Sms> smsDetails) {
        this.smsList = smsDetails;
        notifyDataSetChanged();

    }

}
