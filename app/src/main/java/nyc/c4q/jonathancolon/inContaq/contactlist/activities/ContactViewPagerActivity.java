package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import io.realm.Realm;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;
import nyc.c4q.jonathancolon.inContaq.realm.RealmHelper;
import nyc.c4q.jonathancolon.inContaq.refactorcode.DaggerRxBusComponent;
import nyc.c4q.jonathancolon.inContaq.refactorcode.RxBusComponent;

import static nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity.CONTACT_ID;

public class ContactViewPagerActivity extends FragmentActivity {

    public Contact contact;
    ViewPager viewPager;
    RxBusComponent rxBusComponent;
    private Realm realm;
    private long contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        rxBusComponent = DaggerRxBusComponent.builder()
                .build();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_viewpager);
        Realm.init(getApplicationContext());

        realm = Realm.getDefaultInstance();
        RealmHelper.clearSmsRecords(realm);
        contactId = getIntent().getLongExtra(CONTACT_ID, -1);
        contact = RealmHelper.getByRealmID(realm, contactId);

        showViewPager();


//        Observable.fromCallable(() -> {
//            RetreiveContactMessages callable = new RetreiveContactMessages(getApplicationContext(),
//                    contactId);
//            return callable.getAllSms();
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(arrayList ->
//                        Toast.makeText(this, "COMPLETE", Toast.LENGTH_SHORT).show());
////// TODO: 5/13/17 here is where we will trigger stats to calculate
    }

    private void showViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager_contact_tabs);
        viewPager.setAdapter(new ContactFragmentPagerAdapter(getSupportFragmentManager(),
                ContactViewPagerActivity.this) {
        });

        viewPager.setOffscreenPageLimit(3);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout_contact_tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }

    public RxBusComponent getRxBusComponent() {
        return rxBusComponent;
    }
}
