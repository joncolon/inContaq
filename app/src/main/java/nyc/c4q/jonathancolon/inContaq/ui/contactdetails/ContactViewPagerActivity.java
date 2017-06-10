package nyc.c4q.jonathancolon.inContaq.ui.contactdetails;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.rxbus.DaggerRxBusComponent;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.rxbus.RxBusComponent;
import nyc.c4q.jonathancolon.inContaq.utlities.Injector;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmService;

import static nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity.CONTACT_ID;

public class ContactViewPagerActivity extends FragmentActivity {

    public Contact contact;
    ViewPager viewPager;
    RxBusComponent rxBusComponent;
    private long contactId;

    @Inject
    RealmService realmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        rxBusComponent = DaggerRxBusComponent.builder()
                .build();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_viewpager);

        Injector.getApplicationComponent().inject(this);

        contactId = getIntent().getLongExtra(CONTACT_ID, -1);
        contact = realmService.getByRealmID(contactId);

        showViewPager();
    }

    private void showViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager_contact_tabs);
        viewPager.setAdapter(new ContactPagerAdapter(getSupportFragmentManager(),
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
        realmService.closeRealm();
    }

    public RxBusComponent getRxBusComponent() {
        return rxBusComponent;
    }

    public static class SmsLoaded {
    }

    public static class SmsUnavailable {

    }
}
