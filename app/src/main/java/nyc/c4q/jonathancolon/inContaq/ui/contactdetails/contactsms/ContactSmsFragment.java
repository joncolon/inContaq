package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms;


import android.content.ContentResolver;
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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.ContactDetailsActivity;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.data.GetAllSms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.rxbus.RxBus;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.rxbus.RxBusComponent;
import nyc.c4q.jonathancolon.inContaq.db.RealmService;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsHelper;

import static nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity.CONTACT_ID;

public class ContactSmsFragment extends Fragment {

    private long contactId;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    private ArrayList<Sms> smsList;
    private Realm realm;
    private RxBus rxBus;
    private Contact contact;
    private Toolbar toolbar;

    @Inject
    RealmService realmService;

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
        RxBusComponent rxBusComponent =
                ((ContactDetailsActivity) getActivity()).getRxBusComponent();
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
        realm = realmService.getInstance();
        contact = ((ContactDetailsActivity) getActivity()).contact;
        contactId = getActivity().getIntent().getLongExtra(CONTACT_ID, -1);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        ContentResolver contentResolver = getActivity().getContentResolver();
        toolbar = (Toolbar)view.findViewById(R.id.sms_toolbar);
        toolbar.setTitle(contact.getFirstName() + " " + contact.getLastName());
        toolbar.setTitleTextColor(getActivity().getColor(R.color.light_font));
        toolbar.setSubtitleTextColor(getActivity().getColor(R.color.grey_font));

        getAllSms(contentResolver, contactId);
        initViews(view);
        return view;
    }

    public void getAllSms(ContentResolver contentResolver, long contactId) {
        progressBar.setVisibility(View.VISIBLE);
        Observable.fromCallable(() -> {
            GetAllSms callable = new GetAllSms(contentResolver, contactId, getContext());
            return callable.getAllSms();
        })
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

                    if (smsList.size() != 0){
                        long time = SmsHelper.getLastContactedDate(contentResolver, contact);
                        StringBuilder lastContacted = SmsHelper.smsDateFormat(time);
                        toolbar.setSubtitle("Last Contacted: " + lastContacted);
                    } else {
                        toolbar.setSubtitle("No SMS data available");
                    }
                });
    }

    private void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    private void setupRecyclerView(Contact contact) {
        SmsAdapter adapter = new SmsAdapter(contact);
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
            if (smsList.size() != 0 && smsList != null) {
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}


