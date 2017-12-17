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
import nyc.c4q.jonathancolon.inContaq.model.ContactModel;
import nyc.c4q.jonathancolon.inContaq.model.SmsModel;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.ContactDetailsActivity;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.data.SmsReader;
import nyc.c4q.jonathancolon.inContaq.utlities.RxBus;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsUtils;

import static nyc.c4q.jonathancolon.inContaq.common.dagger.Injector.getApplicationComponent;
import static nyc.c4q.jonathancolon.inContaq.common.dagger.Injector.getRxBus;
import static nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity.CONTACT_KEY;
import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isEmptyList;

public class ContactSmsFragment extends Fragment {

    private static final String TAG = ContactSmsFragment.class.getSimpleName();
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.sms_toolbar) Toolbar toolbar;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @Inject RealmService realmService;
    @Inject
    SmsReader smsReader;
    @Inject
    SmsUtils smsUtils;
    private long realmID;
    private ArrayList<SmsModel> smsModelList;
    private RxBus rxBus;
    private ContactModel contactModel;
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
        contactModel = ((ContactDetailsActivity) getActivity()).contactModel;
        realmID = getActivity().getIntent().getLongExtra(CONTACT_KEY, -1);

        toolbar.setTitle(contactModel.getFullName());
        toolbar.setTitleTextColor(getActivity().getColor(R.color.light_font));
        toolbar.setSubtitleTextColor(getActivity().getColor(R.color.grey_font));


        retrieveSmsListInBackground(realmID);
        return view;
    }

    public void retrieveSmsListInBackground(long contactId) {
        progressBar.setVisibility(View.VISIBLE);
        Observable.fromCallable(() -> smsReader.retrieveSmsList(contactId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(arrayList -> {
                    smsModelList = arrayList;
                    progressBar.setVisibility(View.GONE);
                    ContactSmsFragment.this.setupRecyclerView(contactModel);
                    ContactSmsFragment.this.showRecyclerView();
                    Log.e("RxBus: ", "sending: waiting to send ");
                    ContactSmsFragment.this.sendEvent();
                    ContactSmsFragment.this.scrollListToBottom();

                    if (!isEmptyList(smsModelList)) {
                        long time = smsUtils.getLastContactedDate(contactModel);
                        StringBuilder lastContacted = smsUtils.smsDateFormat(time);
                        toolbar.setSubtitle(getString(R.string.last_contacted) + lastContacted);
                    } else {
                        toolbar.setSubtitle(R.string.no_sms_available);
                    }
                });
    }

    private void setupRecyclerView(ContactModel contactModel) {
        SmsAdapter adapter = new SmsAdapter(contactModel, smsUtils);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Collections.sort(smsModelList);
        adapter.setData(smsModelList);
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
            if (!isEmptyList(smsModelList)) {
                rxBus.send(smsModelList);
            } else {
                rxBus.send(new ContactDetailsActivity.SmsUnavailable());
            }
        }
    }

    synchronized private void scrollListToBottom() {
        recyclerView.post(() -> recyclerView.scrollToPosition(smsModelList.size() - 1));
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


