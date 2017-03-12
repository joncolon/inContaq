package nyc.c4q.jonathancolon.inContaq.contactlist.adapters;

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

import com.github.florent37.beautifulparallax.ParallaxViewController;

import java.util.List;

import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.SmsHelper;

import static android.content.ContentValues.TAG;


public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsViewHolder> {
    private static final String ON_BIND_VIEW_HOLDER = "onBindViewHolder: ";
    private Context context;
    private final Listener listener;
    private List<Sms> smsList;
    private final Contact contact;


    private final ParallaxViewController parallaxViewController = new ParallaxViewController();

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

    public interface Listener {
        void onContactClicked(Sms smsDetail);
        void onContactLongClicked(Sms smsDetail);
    }

    public void setData(List<Sms> smsDetails) {
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

        void bind(Sms sms) {
            final Sms smsDetail = sms;

            setSmsDetails(sms);
            displaySmsByType(sms);

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

        void initViews(){
            senderId = (TextView) itemView.findViewById(R.id.senderId);
            messageRecieved = (TextView) itemView.findViewById(R.id.messageDetails);
            type = (TextView) itemView.findViewById(R.id.type);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.chat_view);
            cardBubble = (CardView) itemView.findViewById(R.id.chatBubble);
            timeDate = (TextView) itemView.findViewById(R.id.timeDate);
        }

        void setSmsDetails(Sms sms){
            StringBuilder time = SmsHelper.smsDateFormat(Long.parseLong(sms.getTime()));
            senderId.setText(sms.getAddress());
            timeDate.setText(time);
            messageRecieved.setText(sms.getMsg());
            messageRecieved.setMovementMethod(LinkMovementMethod.getInstance());
            type.setText(sms.getType());
        }

        void displaySmsByType(Sms sms){
            if (sms.getType().equals("1")){
                if (contact.getCellPhoneNumber() != null){
                    type.setText(contact.getFirstName() + " " + contact.getLastName());
                    linearLayout.setGravity(Gravity.START);

                    if (Build.VERSION.SDK_INT >= 23) {
                        cardBubble.setCardBackgroundColor(context.getColor(R.color.blue_cadet));
                        messageRecieved.setTextColor(context.getColor(R.color.white_baby_powder));
                        timeDate.setTextColor(context.getColor(R.color.white_baby_powder));
                    } else {
                        cardBubble.setCardBackgroundColor(Color.parseColor(BLUE_CADET));
                        messageRecieved.setTextColor(Color.parseColor(WHITE_BABY_POWDER));
                        timeDate.setTextColor(Color.parseColor(WHITE_BABY_POWDER));
                    }
                }
            } else {
                linearLayout.setGravity(Gravity.END);
                if (Build.VERSION.SDK_INT >= 23) {
                    cardBubble.setCardBackgroundColor(context.getColor(R.color.white_baby_powder));
                    messageRecieved.setTextColor(context.getColor(R.color.blue_sapphire));
                    timeDate.setTextColor(context.getColor(R.color.blue_sapphire));
                } else {
                    cardBubble.setCardBackgroundColor(Color.parseColor(WHITE_BABY_POWDER));
                    messageRecieved.setTextColor(Color.parseColor(BLUE_SAPPHIRE));
                    timeDate.setTextColor(Color.parseColor(BLUE_SAPPHIRE));
                }
            }
        }
    }
}
