package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nyc.c4q.jonathancolon.inContaq.R;

/**
 * Created by jello on 3/14/17.
 */

public class DialogFragment extends android.support.v4.app.DialogFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sample_dialog, container, false);
        getDialog().setTitle("Simple Dialog");
        return rootView;
    }

}
