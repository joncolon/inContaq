package nyc.c4q.jonathancolon.inContaq.contactlist.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactInfoFragment;
import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactSmsFragment;
import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactStatsFragment;
import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.SmsHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;


public class ContactFragmentPagerAdapter extends FragmentPagerAdapter {
    private final Context context;
    private final ArrayList<Sms> smsList;

    public ContactFragmentPagerAdapter(FragmentManager fm, Context context, Contact contact) {
        super(fm);
        this.context = context;
        smsList = SmsHelper.getAllSms(context, contact);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0:
                Fragment infoFragment = new ContactInfoFragment();
                Bundle infoBundle = new Bundle();
                infoBundle.putParcelable("smslist", Parcels.wrap(smsList));
                infoFragment.setArguments(infoBundle);
                return infoFragment;
            case 1:
                Fragment smsFragment = new ContactSmsFragment();
                Bundle smsBundle = new Bundle();
                smsBundle.putParcelable("smslist", Parcels.wrap(smsList));
                smsFragment.setArguments(smsBundle);
                return smsFragment;
            case 2:
                Fragment statsFragment = new ContactStatsFragment();
                Bundle statsBundle = new Bundle();
                statsBundle.putParcelable("smslist", Parcels.wrap(smsList));
                statsFragment.setArguments(statsBundle);
                return statsFragment;
            default:
                Fragment defaultFragment = new ContactSmsFragment();
                Bundle defaultBundle = new Bundle();
                defaultBundle.putParcelable("smslist", Parcels.wrap(smsList));
                defaultFragment.setArguments(defaultBundle);
                return defaultFragment;
        }
    }



    @Override
    public CharSequence getPageTitle(int position) {
        String[] tabTitles = new String[]{context.getString(R.string.contact_info),
                context.getString(R.string.messages), context.getString(R.string.stats)};

        return tabTitles[position];
    }
}
