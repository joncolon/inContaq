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
import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.utlities.graphs.linegraphs.MonthlyGraph;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.SmsHelper;


public class MonthlyGraphFragment extends Fragment {


    private LineChartView lineGraph;
    private ArrayList<Sms> lstSms;

    public MonthlyGraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        lineGraph = (LineChartView) view.findViewById(R.id.daily_chart);
        Contact contact = unwrapParcelledContact();
        lstSms = SmsHelper.getAllSms(getActivity(), contact);
        showMonthlyGraph();

        return view;
    }

    private void showMonthlyGraph() {
        MonthlyGraph monthlyGraph = new MonthlyGraph(getContext(), lineGraph, lstSms);
        monthlyGraph.showMonthlyGraph();
    }

    @Nullable
    private Contact unwrapParcelledContact() {
        return Parcels.unwrap(getActivity().getIntent().getParcelableExtra(ContactListActivity.PARCELLED_CONTACT));
    }

}
