package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactInfoFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_info, container, false);

        TextView tv = (TextView) view.findViewById(R.id.tvFragSecond);
        tv.setText("Contact Information");

        return view;
    }

    public static ContactInfoFragment newInstance(Contact contact) {

        ContactInfoFragment fragment = new ContactInfoFragment();
        Bundle b = new Bundle();

        fragment.setArguments(b);

        return fragment;
    }
}
