package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms;

import android.content.Context;
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
import nyc.c4q.jonathancolon.inContaq.utlities.SmsUtils;

import static android.content.ContentValues.TAG;


public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsViewHolder> {
    private static final String ON_BIND_VIEW_HOLDER = "onBindViewHolder: ";
    private static final int MARSHMALLOW = 23;
    private final Contact contact;
    private SmsUtils smsUtils;
    private Context context;
    private ArrayList<Sms> smsList;

    SmsAdapter(Contact contact, SmsUtils smsUtils) {
        this.contact = contact;
        this.smsUtils = smsUtils;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
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
        Log.d(TAG, ON_BIND_VIEW_HOLDER + smsDetail.getPhoneNumber());
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

        private static final String RECEIVED = "1";
        private TextView senderId, messageRecieved, timeDate, typeTV;
        private CardView cardBubble;
        private LinearLayout linearLayout;

        SmsViewHolder(View itemView) {
            super(itemView);
            initViews();
        }

        void initViews() {
            senderId = itemView.findViewById(R.id.senderId);
            messageRecieved = itemView.findViewById(R.id.messageDetails);
            typeTV = itemView.findViewById(R.id.type);
            linearLayout = itemView.findViewById(R.id.chat_view);
            cardBubble = itemView.findViewById(R.id.chatBubble);
            timeDate = itemView.findViewById(R.id.timeDate);
        }

        void bind(Sms sms) {
            displaySmsByType(sms);
            populateTextViews(sms);
        }

        void displaySmsByType(Sms sms) {
            if (sms.getType().equals(RECEIVED)) {
                if (contact.getMobileNumber() != null) {
                    String text = new StringBuilder()
                            .append(contact.getFirstName())
                            .append(" ")
                            .append(contact.getLastName())
                            .toString();

                    typeTV.setText(text);
                    linearLayout.setGravity(Gravity.START);

                    if (Build.VERSION.SDK_INT >= MARSHMALLOW) {
                        cardBubble.setCardBackgroundColor(context.getColor(R.color.charcoal));
                        messageRecieved.setTextColor(context.getColor(R.color.cardBackgroundColor));
                        timeDate.setTextColor(context.getColor(R.color.cardBackgroundColor));
                    }
                }
            } else {
                linearLayout.setGravity(Gravity.END);
                if (Build.VERSION.SDK_INT >= MARSHMALLOW) {
                    cardBubble.setCardBackgroundColor(context.getColor(R.color.carmine_pink_lite));
                    messageRecieved.setTextColor(context.getColor(R.color.cardBackgroundColor));
                    timeDate.setTextColor(context.getColor(R.color.cardBackgroundColor));
                }
            }
        }

        void populateTextViews(Sms sms) {
            StringBuilder time = smsUtils.smsDateFormat(Long.parseLong(sms.getTimeStamp()));
            senderId.setText(sms.getPhoneNumber());
            timeDate.setText(time);
            messageRecieved.setText(sms.getMessage());
            messageRecieved.setMovementMethod(LinkMovementMethod.getInstance());
            typeTV.setText(sms.getType());
        }
    }
}
