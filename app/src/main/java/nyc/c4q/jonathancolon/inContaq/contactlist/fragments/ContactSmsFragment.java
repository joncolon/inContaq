package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fontometrics.Fontometrics;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.contactlist.adapters.SmsAdapter;
import nyc.c4q.jonathancolon.inContaq.contactlist.Animations;
import nyc.c4q.jonathancolon.inContaq.contactlist.PicassoHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.SmsHelper;

import static nyc.c4q.jonathancolon.inContaq.utlities.sqlite.SqlHelper.saveToDatabase;

public class ContactSmsFragment extends Fragment implements SmsAdapter.Listener {

    private static final int RESULT_LOAD_BACKGROUND_IMG = 2;
    private static final int RESULT_LOAD_CONTACT_IMG = 1;
    private static TextView contactName;
    private static ImageView contactImageIV, backgroundImageIV;
    private static Contact contact;
    private final String TAG = "SET TEXT REQUEST: ";
    private SmsAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Sms> SmsList;
    private static ImageView smsSendButton;
    private static EditText smsEditText;

    public ContactSmsFragment() {
        //required empty public constructor
    }

    public static ContactSmsFragment newInstance() {
        ContactSmsFragment fragment = new ContactSmsFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_contact_sms, container, false);
        contact = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(ContactListActivity.PARCELLED_CONTACT));
        smsSendButton = (ImageView) view.findViewById(R.id.send_button);
        smsEditText = (EditText) view.findViewById(R.id.sms_edit_text);
        smsSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });
        initViews(view);
        enableClickListeners();
        displayContactInfo(contact);
        SmsList = SmsHelper.getAllSms(getActivity(), contact);
        setupRecyclerView(contact);
        refreshRecyclerView();
        scrollListToBottom();
        showRecyclerView();

        return view;
    }

    private void initViews(View view) {
        contactName = (TextView) view.findViewById(R.id.name);
        contactImageIV = (ImageView) view.findViewById(R.id.contact_img);
        backgroundImageIV = (ImageView) view.findViewById(R.id.background_image);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        contactName.setTypeface(Fontometrics.amatic_bold(getActivity()));

        contactImageIV.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galleryIntent, RESULT_LOAD_CONTACT_IMG);
        });
        if (backgroundImageIV != null) {
            backgroundImageIV.setOnClickListener(v -> {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, RESULT_LOAD_BACKGROUND_IMG);
            });
        }
    }

    synchronized private void displayContactInfo(Contact contact) {
        String nameValue = contact.getFirstName() + " " + contact.getLastName();
        PicassoHelper ph = new PicassoHelper(getActivity());

        if (contact.getBackgroundImage() != null) {
            ph.loadImageFromString(contact.getBackgroundImage(), backgroundImageIV);
        }
        if (contact.getContactImage() != null) {
            ph.loadImageFromString(contact.getContactImage(), contactImageIV);
        }
        contactName.setText(nameValue);
    }

    private void setupRecyclerView(Contact contact) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SmsAdapter(this, contact));
    }

    private synchronized void refreshRecyclerView() {
        adapter = (SmsAdapter) recyclerView.getAdapter();
        Collections.sort(SmsList);
        adapter.setData(SmsList);
        Log.d(TAG, "RefreshRV : " + SmsList.size());
        adapter.notifyDataSetChanged();
    }

    synchronized private void scrollListToBottom() {
        recyclerView.post(() -> recyclerView.scrollToPosition(adapter.getItemCount() - 1));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                PicassoHelper ph = new PicassoHelper(getActivity());

                switch (requestCode) {
                    case 1:
                        Uri contactUri = data.getData();
                        ph.loadImageFromUri(contactUri, contactImageIV);
                        contact.setContactImage(contactUri.toString());
                        saveToDatabase(contact, getActivity());
                        break;
                    case 2:
                        Uri backgroundUri = data.getData();
                        ph.loadImageFromUri(backgroundUri, backgroundImageIV);
                        contact.setBackgroundImage(backgroundUri.toString());
                        saveToDatabase(contact, getActivity());
                        break;
                }
            } else {
                Toast.makeText(getActivity(), R.string.error_message_photo_not_selected,
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "onActivityResult: " + e.toString());
            Toast.makeText(getActivity(), R.string.error_message_general, Toast.LENGTH_LONG)
                    .show();
        }
    }

    synchronized private void showRecyclerView(){
        Animations anim = new Animations(getActivity());
        recyclerView.setVisibility(View.VISIBLE);
        anim.fadeIn(recyclerView);
    }

    public void enableClickListeners(){
        contactImageIV.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galleryIntent, RESULT_LOAD_CONTACT_IMG);
        });
        if (backgroundImageIV != null) {
            backgroundImageIV.setOnClickListener(v -> {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, RESULT_LOAD_BACKGROUND_IMG);
            });
        }
    }

    //getActivity is the equivalent of getContext in fragements
    public void sendMessage(View v) {

//        String _messageNumber=smsEditText.getText().toString();
        String _messageNumber=contact.getCellPhoneNumber();
        String messageText = smsEditText.getText().toString();
        Sms newSms = new Sms();
        newSms.setMsg(messageText);
        newSms.setAddress(contact.getCellPhoneNumber());
        newSms.setTime(String.valueOf(System.currentTimeMillis()));
        newSms.setType("1");
        SmsList.add(newSms);
        String sent = "SMS_SENT";

        PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(), 0,
                new Intent(sent), 0);

        //---when the SMS has been sent---
        getActivity().registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if(getResultCode() == Activity.RESULT_OK)
                {
                    Toast.makeText(v.getContext(), "SMS sent",
                            Toast.LENGTH_SHORT).show();
                    SmsHelper.getAllSms(getContext(), contact);
                    refreshRecyclerView();
                    scrollListToBottom();

                }
                else
                {
                    Toast.makeText(getActivity(), "SMS could not sent",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter(sent));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(_messageNumber, null, messageText, sentPI, null);

//        SmsHelper.getAllSms(getActivity(), contact);
//        setupRecyclerView(contact);
//        refreshRecyclerView();
//        scrollListToBottom();
//        showRecyclerView();
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onContactClicked(Sms sms) {
        //TODO add functionality
    }

    @Override
    public void onContactLongClicked(Sms sms) {
        //TODO add functionality
    }
}
