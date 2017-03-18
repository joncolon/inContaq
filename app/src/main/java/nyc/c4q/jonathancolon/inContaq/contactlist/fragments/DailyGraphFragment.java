package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.view.LineChartView;

import org.parceler.Parcels;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.utlities.graphs.linegraphs.DailyGraph;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.SmsHelper;


public class DailyGraphFragment extends Fragment {


    private LineChartView lineGraph;
    private ArrayList<Sms> lstSms;

    public DailyGraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        lineGraph = (LineChartView) view.findViewById(R.id.daily_chart);
        Contact contact = unwrapParcelledContact();
        lstSms = SmsHelper.getAllSms(getActivity(), contact);
        showDailyGraph();
        return view;
    }

    private void showDailyGraph() {
        DailyGraph dailyGraph = new DailyGraph(getContext(), lineGraph, lstSms);
        dailyGraph.showDailyGraph();
    }

    @Nullable
    private Contact unwrapParcelledContact() {
        return Parcels.unwrap(getActivity().getIntent().getParcelableExtra(ContactListActivity.PARCELLED_CONTACT));
    }

}
