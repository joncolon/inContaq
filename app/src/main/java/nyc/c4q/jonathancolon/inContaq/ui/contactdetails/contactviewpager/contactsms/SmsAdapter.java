package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactsms;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
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

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsHelper;

import static android.content.ContentValues.TAG;


public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsViewHolder> {
    private static final String ON_BIND_VIEW_HOLDER = "onBindViewHolder: ";
    private final Contact contact;
    private Context context;
    private ArrayList<Sms> smsList;

    public SmsAdapter(Contact contact) {
        this.contact = contact;
    }


    @Override
    public SmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_sms_rv,
                parent, false);
        SmsViewHolder vh = new SmsViewHolder(itemView);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(SmsViewHolder holder, int position) {
        Sms smsDetail = smsList.get(position);
        holder.bind(smsDetail);
        Log.d(TAG, ON_BIND_VIEW_HOLDER + smsDetail.getAddress());
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public void setData(ArrayList<Sms> smsDetails) {
        this.smsList = smsDetails;
        notifyDataSetChanged();
    }

    //_____________________________________VIEWHOLDER___________________________________________________
    class SmsViewHolder extends RecyclerView.ViewHolder {

        private static final String WHITE_BABY_POWDER = "#FDFFFC";
        private static final String BLUE_CADET = "#5B9CAC";
        private static final String BLUE_SAPPHIRE = "#0E587A";

        private TextView senderId, messageRecieved, timeDate, type;
        private CardView cardBubble;
        private LinearLayout linearLayout;

        SmsViewHolder(View itemView) {
            super(itemView);
            initViews();
        }

        void initViews() {
            senderId = (TextView) itemView.findViewById(R.id.senderId);
            messageRecieved = (TextView) itemView.findViewById(R.id.messageDetails);
            type = (TextView) itemView.findViewById(R.id.type);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.chat_view);
            cardBubble = (CardView) itemView.findViewById(R.id.chatBubble);
            timeDate = (TextView) itemView.findViewById(R.id.timeDate);
        }

        void bind(Sms sms) {
            displaySmsByType(sms);
            populateTextViews(sms);
        }

        void displaySmsByType(Sms sms) {
            if (sms.getType().equals("1")) {
                if (contact.getMobileNumber() != null) {
                    type.setText(contact.getFirstName() + " " + contact.getLastName());
                    linearLayout.setGravity(Gravity.START);

                    if (Build.VERSION.SDK_INT >= 23) {
                        cardBubble.setCardBackgroundColor(context.getColor(R.color.charcoal));
                        messageRecieved.setTextColor(context.getColor(R.color.cardBackgroundColor));
                        timeDate.setTextColor(context.getColor(R.color.cardBackgroundColor));
                    } else {
                        cardBubble.setCardBackgroundColor(Color.parseColor(BLUE_CADET));
                        messageRecieved.setTextColor(Color.parseColor(WHITE_BABY_POWDER));
                        timeDate.setTextColor(Color.parseColor(WHITE_BABY_POWDER));
                    }
                }
            } else {
                linearLayout.setGravity(Gravity.END);
                if (Build.VERSION.SDK_INT >= 23) {
                    cardBubble.setCardBackgroundColor(context.getColor(R.color.carmine_pink_lite));
                    messageRecieved.setTextColor(context.getColor(R.color.cardBackgroundColor));
                    timeDate.setTextColor(context.getColor(R.color.cardBackgroundColor));
                } else {
                    cardBubble.setCardBackgroundColor(Color.parseColor(WHITE_BABY_POWDER));
                    messageRecieved.setTextColor(Color.parseColor(BLUE_SAPPHIRE));
                    timeDate.setTextColor(Color.parseColor(BLUE_SAPPHIRE));
                }
            }
        }

        void populateTextViews(Sms sms) {
            StringBuilder time = SmsHelper.smsDateFormat(Long.parseLong(sms.getTime()));
            senderId.setText(sms.getAddress());
            timeDate.setText(time);
            messageRecieved.setText(sms.getMsg());
            messageRecieved.setMovementMethod(LinkMovementMethod.getInstance());
            type.setText(sms.getType());
        }
    }
}
