package nyc.c4q.jonathancolon.inContaq.ui.contactdetails;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.ContactSmsFragment;


class ContactPagerAdapter extends FragmentPagerAdapter {
    private final Context context;

    ContactPagerAdapter(FragmentManager fm, Context context) {
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
                return new nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.ContactStatsFragment();
            case 1:
                return new nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactinfo.ContactInfoFragment();
            case 2:
                return new ContactSmsFragment();
            default:
                return new nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactinfo.ContactInfoFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String[] tabTitles = new String[]{context.getString(R.string.stats),
                context.getString(R.string.contact_info), context.getString(R.string.messages)};
        return tabTitles[position];
    }
}
