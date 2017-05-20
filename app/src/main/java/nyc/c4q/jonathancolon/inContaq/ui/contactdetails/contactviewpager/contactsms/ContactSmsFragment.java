package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactsms;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.klinker.android.send_message.Message;
import com.klinker.android.send_message.Transaction;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.ContactViewPagerActivity;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.rxbus.RxBus;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.rxbus.RxBusComponent;
import nyc.c4q.jonathancolon.inContaq.utlities.FontHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmDbHelper;

import static nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity.CONTACT_ID;

public class ContactSmsFragment extends Fragment {

    private static final int RESULT_LOAD_BACKGROUND_IMG = 2;
    private static final int RESULT_LOAD_CONTACT_IMG = 1;
    private long contactId;
    private TextView contactName;
    private ImageView contactImageIV, backgroundImageIV, smsSendButton;
    private EditText smsEditText;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    private SmsAdapter adapter;
    private ArrayList<Sms> smsList;
    private Settings settings;
    private Context context;
    private ContentResolver contentResolver;
    private Realm realm;
    private PicassoHelper picasso;
    private RxBusComponent rxBusComponent;
    private RxBus rxBus;
    private Contact contact;

    public ContactSmsFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        rxBusComponent = ((ContactViewPagerActivity) getActivity()).getRxBusComponent();
        rxBusComponent.inject(this);
        super.onCreate(savedInstanceState);

        rxBus = rxBusComponent.getRxBus();
        Log.i("Dagger: ", "SmsFrag rxBus: " + rxBus);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_contact_sms, container, false);
        context = getActivity();
        contactId = getActivity().getIntent().getLongExtra(CONTACT_ID, -1);
        realm = RealmDbHelper.getInstance();
        contact = RealmDbHelper.getByRealmID(realm, contactId);
        picasso = new PicassoHelper(context);
        contentResolver = context.getContentResolver();


        initViews(view);
        initSettings();
        getAllSms(contentResolver, contactId);
        return view;
    }

    private void initViews(View view) {
        contactName = (TextView) view.findViewById(R.id.name);
        contactImageIV = (ImageView) view.findViewById(R.id.contact_image);
        backgroundImageIV = (ImageView) view.findViewById(R.id.background_image);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        smsSendButton = (ImageView) view.findViewById(R.id.send_button);
        smsEditText = (EditText) view.findViewById(R.id.sms_edit_text);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        if (smsSendButton != null) {
            smsSendButton.setOnClickListener(v -> ContactSmsFragment.this.sendMessage());
        }

        setClickListenersWhenInPortraitMode();
    }

    private void initSettings() {
        settings = Settings.get(getContext());
    }

    public void getAllSms(ContentResolver contentResolver, long contactId) {
        progressBar.setVisibility(View.VISIBLE);
        Observable.fromCallable(() -> {
            GetAllSms callable = new GetAllSms(contentResolver,
                    contactId);
            return callable.getAllSms();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(arrayList -> {
                    smsList = arrayList;
                    progressBar.setVisibility(View.GONE);
                        ContactSmsFragment.this.setupRecyclerView(contact);
                        ContactSmsFragment.this.scrollListToBottom();
                        ContactSmsFragment.this.showRecyclerView();
                        Log.e("RxBus: ", "sending: waiting to send ");
                        ContactSmsFragment.this.sendEvent();
                });
    }

    synchronized public void sendMessage() {
        new Thread(() -> {
            com.klinker.android.send_message.Settings sendSettings =
                    new com.klinker.android.send_message.Settings();
            String messageNumber = contact.getMobileNumber();
            String messageText = smsEditText.getText().toString();
            sendSettings.setMmsc(settings.getMmsc());
            sendSettings.setProxy(settings.getMmsProxy());
            sendSettings.setPort(settings.getMmsPort());
            sendSettings.setUseSystemSending(true);
            Transaction transaction = new Transaction(ContactSmsFragment.this.getContext(), sendSettings);
            Message message = new Message(messageText, messageNumber);
            transaction.sendNewMessage(message, Transaction.NO_THREAD_ID);
        }).start();
    }

    private void setClickListenersWhenInPortraitMode() {
        int value = getActivity().getResources().getConfiguration().orientation;

        if (value == Configuration.ORIENTATION_PORTRAIT) {
            FontHelper fontHelper = new FontHelper(context);
            fontHelper.applyFont(contactName);
            displayContactInfo(contact);

            contactImageIV.setOnClickListener(v -> {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ContactSmsFragment.this.startActivityForResult(galleryIntent,
                        RESULT_LOAD_CONTACT_IMG);
            });

            backgroundImageIV.setOnClickListener(v -> {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ContactSmsFragment.this.startActivityForResult(galleryIntent,
                        RESULT_LOAD_BACKGROUND_IMG);
            });
        }
    }

    private void setupRecyclerView(Contact contact) {
        adapter = new SmsAdapter(contact);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Collections.sort(smsList);
        adapter.setData(smsList);
        recyclerView.setAdapter(adapter);
    }

    synchronized private void scrollListToBottom() {
        recyclerView.post(() -> recyclerView.scrollToPosition(smsList.size() - 1));
    }

    synchronized private void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void sendEvent() {
        Log.e("RxBus: ", "sending: SENT ");
        if (rxBus.hasObservers()) {
            rxBus.send(new ContactViewPagerActivity.SmsLoaded());
            if (smsList.size() != 0 && smsList != null) {
                rxBus.send(smsList);
            }
            else{
                rxBus.send(new ContactViewPagerActivity.SmsUnavailable());
            }
        }
    }

    synchronized private void displayContactInfo(Contact contact) {
        String nameValue = contact.getFirstName() + " " + contact.getLastName();

        if (contact.getBackgroundImage() != null) {
            picasso.loadImageFromString(contact.getBackgroundImage(), backgroundImageIV);
        }
        if (contact.getContactImage() != null) {
            picasso.loadImageFromString(contact.getContactImage(), contactImageIV);
        }
        contactName.setText(nameValue);
    }

    private void initializeSmsRadarService() {
        SmsRadar.initializeSmsRadarService(getContext(), new SmsListener() {
            @Override
            public void onSmsSent(com.tuenti.smsradar.Sms sms) {
                updateSms();
            }

            @Override
            public void onSmsReceived(com.tuenti.smsradar.Sms sms) {
                updateSms();
            }
        });
    }

    public void updateSms() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            ContactSmsFragment.this.refreshRecyclerView();
            ContactSmsFragment.this.scrollListToBottom();
        }, 2000);
    }

    public synchronized void refreshRecyclerView() {
        adapter = (SmsAdapter) recyclerView.getAdapter();
        Collections.sort(smsList);
        adapter.setData(smsList);
        adapter.notifyDataSetChanged();
        scrollListToBottom();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {

                switch (requestCode) {
                    case 1:
                        updateContactImage(data, picasso);
                        break;
                    case 2:
                        setBackgroundImage(data, picasso);
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

    private void updateContactImage(Intent data, PicassoHelper picasso) {
        Uri contactImgUri = data.getData();
        picasso.loadImageFromUri(contactImgUri, contactImageIV);
        realm.executeTransaction(realm1 -> contact.setContactImage(contactImgUri.toString()));
    }

    public void setBackgroundImage(Intent data, PicassoHelper ph) {
        Uri bgImgUri = data.getData();
        ph.loadImageFromUri(bgImgUri, backgroundImageIV);
        realm.executeTransaction(realm1 -> contact.setBackgroundImage(bgImgUri.toString()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSmsRadarService();
        RealmDbHelper.closeRealm(realm);
    }

    private void stopSmsRadarService() {
        SmsRadar.stopSmsRadarService(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}


