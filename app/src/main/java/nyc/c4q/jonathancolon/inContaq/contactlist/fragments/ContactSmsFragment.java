package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactViewPagerActivity;
import nyc.c4q.jonathancolon.inContaq.contactlist.adapters.SmsAdapter;
import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.OnSmsListLoaded;
import nyc.c4q.jonathancolon.inContaq.realm.RealmHelper;
import nyc.c4q.jonathancolon.inContaq.refactorcode.RxBus;
import nyc.c4q.jonathancolon.inContaq.refactorcode.RxBusComponent;
import nyc.c4q.jonathancolon.inContaq.sms.RetreiveContactMessages;
import nyc.c4q.jonathancolon.inContaq.sms.model.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.Settings;

import static nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity.CONTACT_ID;


public class ContactSmsFragment extends Fragment implements SmsAdapter.Listener, OnSmsListLoaded {

    private static final int RESULT_LOAD_BACKGROUND_IMG = 2;
    private static final int RESULT_LOAD_CONTACT_IMG = 1;
    private TextView contactName;
    private ImageView contactImageIV, backgroundImageIV;
    private Contact contact;
    private SmsAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Sms> smsList;
    private EditText smsEditText;
    private ProgressBar progressBar;

    private Settings settings;
    private Realm realm;
    private long contactId;
    private Context context;
    private RxBusComponent rxBusComponent;
    private RxBus rxBus;

    public ContactSmsFragment() {
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

        contactId = getActivity().getIntent().getLongExtra(CONTACT_ID, -1);
        realm = RealmHelper.getInstance();
        contact = RealmHelper.getByRealmID(realm, contactId);
        context = getActivity();

        initializeSmsRadarService();
        initViews(view);
        initSettings();
        getAllSms(context, contactId);

        return view;
    }

    public void getAllSms(Context context, long contactId) {
        progressBar.setVisibility(View.VISIBLE);
        Observable.fromCallable(() -> {
            RetreiveContactMessages callable = new RetreiveContactMessages(context,
                    contactId);
            return callable.getAllSms();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Sms>>() {
                    @Override
                    public void accept(@NonNull ArrayList<Sms> arrayList) throws Exception {
                        smsList = arrayList;
                        progressBar.setVisibility(View.GONE);
                        setupRecyclerView(contact);
                        scrollListToBottom();
                        showRecyclerView();
                    }


                });

//// TODO: 5/13/17 here is where we will trigger stats to calculate
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

    private void initViews(View view) {
        contactName = (TextView) view.findViewById(R.id.name);
        contactImageIV = (ImageView) view.findViewById(R.id.contact_image);
        backgroundImageIV = (ImageView) view.findViewById(R.id.background_image);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        ImageView smsSendButton = (ImageView) view.findViewById(R.id.send_button);
        smsEditText = (EditText) view.findViewById(R.id.sms_edit_text);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        if (smsSendButton != null){
            smsSendButton.setOnClickListener(v -> ContactSmsFragment.this.sendMessage());
        }

        setClickListenersWhenInPortraitMode();
    }

    private void initSettings() {
        settings = Settings.get(getContext());
    }

    public void updateSms() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            ContactSmsFragment.this.refreshRecyclerView();
            ContactSmsFragment.this.scrollListToBottom();
        }, 2000);
    }

    synchronized public void sendMessage() {
        new Thread(() -> {
            String messageNumber = contact.getMobileNumber();
            String messageText = smsEditText.getText().toString();
            com.klinker.android.send_message.Settings sendSettings = new com.klinker.android.send_message.Settings();
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
            Typeface jaapokkiRegular = Typeface.createFromAsset(contactName.getContext().
                    getApplicationContext().getAssets(), getString(R.string.nameDisplayFont));

            contactName.setTypeface(jaapokkiRegular);
            displayContactInfo(contact);
            contactImageIV.setOnClickListener(v -> {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ContactSmsFragment.this.startActivityForResult(galleryIntent,
                        RESULT_LOAD_CONTACT_IMG);
            });
            if (backgroundImageIV != null) {
                backgroundImageIV.setOnClickListener(v -> {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    ContactSmsFragment.this.startActivityForResult(galleryIntent,
                            RESULT_LOAD_BACKGROUND_IMG);
                });
            }
        }
    }

    public synchronized void refreshRecyclerView() {
//        retrieveSmsList(realm, contact.getAddress());
        adapter = (SmsAdapter) recyclerView.getAdapter();
        Collections.sort(smsList);
        adapter.setData(smsList);
        adapter.notifyDataSetChanged();
        scrollListToBottom();
    }

    synchronized private void scrollListToBottom() {
        recyclerView.post(() -> recyclerView.scrollToPosition(smsList.size() - 1));
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

//    private void retrieveSmsList(Realm realm, String mobileNumber) {
//        smsList = RealmHelper.getByMobileNumber(realm, mobileNumber);
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                PicassoHelper ph = new PicassoHelper(getActivity());

                switch (requestCode) {
                    case 1:
                        setContactImage(data, ph);
                        break;
                    case 2:
                        setBackgroundImage(data, ph);
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

    private void setContactImage(Intent data, PicassoHelper ph) {
        Uri contactUri = data.getData();
        ph.loadImageFromUri(contactUri, contactImageIV);
        contact.setContactImage(contactUri.toString());
    }

    private void setBackgroundImage(Intent data, PicassoHelper ph) {
        Uri backgroundUri = data.getData();
        ph.loadImageFromUri(backgroundUri, backgroundImageIV);
        contact.setBackgroundImage(backgroundUri.toString());
    }

    @Override
    public void onContactClicked(Sms sms) {
        //TODO add functionality
    }

    @Override
    public void onContactLongClicked(Sms sms) {
        //TODO add functionality
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSmsRadarService();
        RealmHelper.clearSmsRecords(realm);
        RealmHelper.closeRealm(realm);

    }

    private void stopSmsRadarService() {
        SmsRadar.stopSmsRadarService(getContext());
    }

    private void setupRecyclerView(Contact contact) {
        adapter = new SmsAdapter(this, contact);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Collections.sort(smsList);
        adapter.setData(smsList);
        recyclerView.setAdapter(adapter);
    }

    synchronized private void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onSmsListLoaded(ArrayList<Sms> arrayList) {
        smsList = arrayList;
        setupRecyclerView(contact);
        scrollListToBottom();
        showRecyclerView();
    }
}


