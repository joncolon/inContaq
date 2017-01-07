package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nyc.c4q.jonathancolon.inContaq.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactStatsFragment extends Fragment {

    public static ContactStatsFragment newInstance(String text) {

        ContactStatsFragment fragment = new ContactStatsFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_stats, container, false);

        TextView tv = (TextView) view.findViewById(R.id.tvFragThird);
        tv.setText("inContaq Stats");

        return view;
    }
}
