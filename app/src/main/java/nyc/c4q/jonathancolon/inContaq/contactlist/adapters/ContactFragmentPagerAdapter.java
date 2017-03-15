package nyc.c4q.jonathancolon.inContaq.contactlist.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactInfoFragment;
import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactSmsFragment;
import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactStatsFragment;


public class ContactFragmentPagerAdapter extends FragmentPagerAdapter {
    private final Context context;

    public ContactFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0:
                return ContactInfoFragment.newInstance();
            case 1:
                return ContactSmsFragment.newInstance();
            case 2:
                return ContactStatsFragment.newInstance();
            default:
                return ContactSmsFragment.newInstance();
        }
    }



    @Override
    public CharSequence getPageTitle(int position) {
        String[] tabTitles = new String[]{context.getString(R.string.contact_info),
                context.getString(R.string.messages), context.getString(R.string.stats)};

        return tabTitles[position];
    }
}
