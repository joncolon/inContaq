package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.ContactDetailsActivity;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.data.GetAllSms;
import nyc.c4q.jonathancolon.inContaq.utlities.RxBus;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsHelper;

import static nyc.c4q.jonathancolon.inContaq.common.di.Injector.getApplicationComponent;
import static nyc.c4q.jonathancolon.inContaq.common.di.Injector.getRxBus;
import static nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity.CONTACT_KEY;
import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isEmptyList;

public class ContactSmsFragment extends Fragment {

    private static final String TAG = ContactSmsFragment.class.getSimpleName();
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.sms_toolbar) Toolbar toolbar;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @Inject RealmService realmService;
    @Inject GetAllSms getAllSms;
    @Inject SmsHelper smsHelper;
    private long realmID;
    private ArrayList<Sms> smsList;
    private RxBus rxBus;
    private Contact contact;
    private Unbinder unbinder;

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
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        rxBus = getRxBus();
        Log.i(TAG, "RxBus memory address: " + rxBus);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_contact_sms, container, false);

        unbinder = ButterKnife.bind(this, view);
        contact = ((ContactDetailsActivity) getActivity()).contact;
        realmID = getActivity().getIntent().getLongExtra(CONTACT_KEY, -1);

        toolbar.setTitle(contact.getFullName());
        toolbar.setTitleTextColor(getActivity().getColor(R.color.light_font));
        toolbar.setSubtitleTextColor(getActivity().getColor(R.color.grey_font));


        retrieveSmsListInBackground(realmID);
        return view;
    }

    public void retrieveSmsListInBackground(long contactId) {
        progressBar.setVisibility(View.VISIBLE);
        Observable.fromCallable(() -> getAllSms.retrieveSmsList(contactId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(arrayList -> {
                    smsList = arrayList;
                    progressBar.setVisibility(View.GONE);
                    ContactSmsFragment.this.setupRecyclerView(contact);
                    ContactSmsFragment.this.showRecyclerView();
                    Log.e("RxBus: ", "sending: waiting to send ");
                    ContactSmsFragment.this.sendEvent();
                    ContactSmsFragment.this.scrollListToBottom();

                    if (!isEmptyList(smsList)) {
                        long time = smsHelper.getLastContactedDate(contact);
                        StringBuilder lastContacted = smsHelper.smsDateFormat(time);
                        toolbar.setSubtitle(getString(R.string.last_contacted) + lastContacted);
                    } else {
                        toolbar.setSubtitle(R.string.no_sms_available);
                    }
                });
    }

    private void setupRecyclerView(Contact contact) {
        SmsAdapter adapter = new SmsAdapter(contact, smsHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Collections.sort(smsList);
        adapter.setData(smsList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    synchronized private void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void sendEvent() {
        Log.e("RxBus: ", "sending: SENT ");
        if (rxBus.hasObservers()) {
            rxBus.send(new ContactDetailsActivity.SmsLoaded());
            if (!isEmptyList(smsList)) {
                rxBus.send(smsList);
            } else {
                rxBus.send(new ContactDetailsActivity.SmsUnavailable());
            }
        }
    }

    synchronized private void scrollListToBottom() {
        recyclerView.post(() -> recyclerView.scrollToPosition(smsList.size() - 1));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realmService.closeRealm();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}


