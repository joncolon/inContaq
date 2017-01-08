package nyc.c4q.jonathancolon.inContaq.contactlist;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactInfoFragment;
import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactSmsFragment;
import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactStatsFragment;

/**
 * Created by jonathancolon on 1/6/17.
 */

public class ContactFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Contact Info", "Messages", "Stats" };
    private Context context;
    private Contact contact;

    public ContactFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {

            case 0: return ContactInfoFragment.newInstance();
            case 1: return ContactSmsFragment.newInstance("ContactSmsFragment, Instance 4");
            case 2: return ContactStatsFragment.newInstance("ContactStatsFragment, Instance 3");
            default: return ContactSmsFragment.newInstance("ContactSmsFragment, Instance 4");
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
