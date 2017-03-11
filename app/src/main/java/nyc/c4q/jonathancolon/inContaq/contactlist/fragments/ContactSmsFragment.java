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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fontometrics.Fontometrics;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Animations;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.PicassoHelper;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.contactlist.adapters.SmsAdapter;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.SmsHelper;

import static nyc.c4q.jonathancolon.inContaq.utlities.sqlite.SqlHelper.saveToDatabase;

public class ContactSmsFragment extends Fragment implements SmsAdapter.Listener {

    private static final int RESULT_LOAD_BACKGROUND_IMG = 2;
    private static final int RESULT_LOAD_CONTACT_IMG = 1;
    private final String TAG = "SET TEXT REQUEST: ";
    private TextView contactName;
    private ImageView contactImageIV, backgroundImageIV;
    private Contact contact;
    private static ContactSmsFragment inst;
    private SmsAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Sms> SmsList;
    private ImageView smsSendButton;
    private EditText smsEditText;

    public ContactSmsFragment() {
    }

    public static ContactSmsFragment newInstance() {
        ContactSmsFragment fragment = new ContactSmsFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    public static ContactSmsFragment instance() {
        return inst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
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
        populateSmsList();
        setupRecyclerView(contact);
        refreshRecyclerView();
        scrollListToBottom();
        showRecyclerView();

        return view;
    }

    synchronized public void sendMessage(View v) {

        String messageNumber = contact.getCellPhoneNumber();
        String messageText = smsEditText.getText().toString();
        String sent = "SMS_SENT";

        PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(), 0,
                new Intent(sent), 0);


        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if (getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(v.getContext(), "SMS sent",
                            Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "SMS could not sent",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter(sent));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(messageNumber, null, messageText, sentPI, null);

        //todo // FIXME: 3/9/17
        populateSmsList();
        refreshRecyclerView();
        scrollListToBottom();
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

    private void enableClickListeners() {
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

    public synchronized void populateSmsList() {
        SmsList = SmsHelper.getAllSms(getActivity(), contact);
    }

    private void setupRecyclerView(Contact contact) {
        adapter = (SmsAdapter) recyclerView.getAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SmsAdapter(this, contact));
    }

    public synchronized void refreshRecyclerView() {
        Collections.sort(SmsList);
        adapter.setData(SmsList);
        Log.d(TAG, "RefreshRV : " + SmsList.size());
        adapter.notifyDataSetChanged();
    }

    synchronized private void scrollListToBottom() {
        recyclerView.post(() -> recyclerView.scrollToPosition(adapter.getItemCount() - 1));
    }

    synchronized private void showRecyclerView() {
        Animations anim = new Animations(getActivity());
        recyclerView.setVisibility(View.VISIBLE);
        anim.fadeIn(recyclerView);
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
            Toast.makeText(getActivity(), R.string.error_message_general, Toast.LENGTH_LONG)
                    .show();
        }
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
