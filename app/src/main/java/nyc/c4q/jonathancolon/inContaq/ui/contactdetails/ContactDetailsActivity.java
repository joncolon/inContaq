package nyc.c4q.jonathancolon.inContaq.ui.contactdetails;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import javax.inject.Inject;

import butterknife.BindView;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.common.base.BaseActivity;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.model.Contact;

import static nyc.c4q.jonathancolon.inContaq.common.di.Injector.getApplicationComponent;
import static nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity.CONTACT_KEY;

public class ContactDetailsActivity extends BaseActivity implements ContactDetailsContract.View {

    @BindView(R.id.tablayout_contact_tabs)TabLayout tabLayout;
    @BindView(R.id.viewpager_contact_tabs)ViewPager viewPager;

    @Inject ContactDetailsPresenter presenter;
    public Contact contact;

    @Inject
    RealmService realmService;

    @Override
    protected void initializeDagger() {
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initializePresenter() {
        super.presenter = presenter;
        presenter.setView(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_contact_viewpager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        loadSelectedContact();
        showViewPager();
    }

    @Override
    public synchronized void loadSelectedContact() {
        long realmID = getIntent().getLongExtra(CONTACT_KEY, -1);
        contact = realmService.getByRealmID(realmID);
    }

    @Override
    public synchronized void showViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager_contact_tabs);
        viewPager.setAdapter(new ContactPagerAdapter(getSupportFragmentManager(),
                ContactDetailsActivity.this) {
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

    public static class SmsLoaded {
    }

    public static class SmsUnavailable {

    }
}
