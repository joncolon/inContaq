package nyc.c4q.jonathancolon.inContaq.contactlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import org.parceler.Parcels;

import nyc.c4q.jonathancolon.inContaq.R;

public class ContactViewPagerActivity extends FragmentActivity implements AlertDialogCallback<String>{
    private static Contact contact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_viewpager);

        contact = Parcels.unwrap(getIntent().getParcelableExtra("Parcelled Contact"));
        Intent i = getIntent();
        String tabToOpen = i.getStringExtra("sms");

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ContactFragmentPagerAdapter(getSupportFragmentManager(),
                ContactViewPagerActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        if (tabToOpen.equals(tabToOpen)) {
            viewPager.setCurrentItem(1);
        }
    }


    @Override
    public void alertDialogCallback(String ret) {

    }
}
