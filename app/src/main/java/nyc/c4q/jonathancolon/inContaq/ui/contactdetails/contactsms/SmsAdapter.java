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
import nyc.c4q.jonathancolon.inContaq.model.ContactModel;
import nyc.c4q.jonathancolon.inContaq.model.SmsModel;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsUtils;

import static android.content.ContentValues.TAG;


public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsViewHolder> {
    private static final String ON_BIND_VIEW_HOLDER = "onBindViewHolder: ";
    public static final int MARSHMALLOW = 23;
    private final ContactModel contactModel;
    private SmsUtils smsUtils;
    private Context context;
    private ArrayList<SmsModel> smsList;

    public SmsAdapter(ContactModel contactModel, SmsUtils smsUtils) {
        this.contactModel = contactModel;
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
        SmsModel smsModelDetail = smsList.get(position);
        holder.bind(smsModelDetail);
        Log.d(TAG, ON_BIND_VIEW_HOLDER + smsModelDetail.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public void setData(ArrayList<SmsModel> smsModelDetails) {
        this.smsList = smsModelDetails;
        notifyDataSetChanged();
    }

    //_____________________________________VIEWHOLDER___________________________________________________
    class SmsViewHolder extends RecyclerView.ViewHolder {

        private static final String RECEIVED = "1";
        private TextView senderId, messageRecieved, timeDate, type;
        private CardView cardBubble;
        private LinearLayout linearLayout;

        SmsViewHolder(View itemView) {
            super(itemView);
            initViews();
        }

        void initViews() {
            senderId = itemView.findViewById(R.id.senderId);
            messageRecieved = itemView.findViewById(R.id.messageDetails);
            type = itemView.findViewById(R.id.type);
            linearLayout = itemView.findViewById(R.id.chat_view);
            cardBubble = itemView.findViewById(R.id.chatBubble);
            timeDate = itemView.findViewById(R.id.timeDate);
        }

        void bind(SmsModel smsModel) {
            displaySmsByType(smsModel);
            populateTextViews(smsModel);
        }

        void displaySmsByType(SmsModel smsModel) {
            if (smsModel.getType().equals(RECEIVED)) {
                if (contactModel.getMobileNumber() != null) {
                    String text = new StringBuilder()
                            .append(contactModel.getFirstName())
                            .append(" ")
                            .append(contactModel.getLastName())
                            .toString();

                    type.setText(text);
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

        void populateTextViews(SmsModel smsModel) {
            StringBuilder time = smsUtils.smsDateFormat(Long.parseLong(smsModel.getTimeStamp()));
            senderId.setText(smsModel.getPhoneNumber());
            timeDate.setText(time);
            messageRecieved.setText(smsModel.getMessage());
            messageRecieved.setMovementMethod(LinkMovementMethod.getInstance());
            type.setText(smsModel.getType());
        }
    }
}
