package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import org.parceler.Parcels;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.adapters.ContactFragmentPagerAdapter;
import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;

public class ContactViewPagerActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_viewpager);

        Contact contact = Parcels.unwrap(getIntent().getParcelableExtra(ContactListActivity.PARCELLED_CONTACT));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_contact_tabs);
        viewPager.setAdapter(new ContactFragmentPagerAdapter(getSupportFragmentManager(),
                ContactViewPagerActivity.this, contact){
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout_contact_tabs);
        tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(1);
    }

}
