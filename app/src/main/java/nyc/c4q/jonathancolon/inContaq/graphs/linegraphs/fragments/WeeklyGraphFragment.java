package nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.fragments;

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
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;
import nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.WeeklyGraph;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.SmsHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;

/**
 * Created by Hyun on 3/17/17.
 */

public class WeeklyGraphFragment extends Fragment {


    private LineChartView lineGraph;
    private ArrayList<Sms> lstSms;

    public WeeklyGraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        lineGraph = (LineChartView) view.findViewById(R.id.daily_chart);
        Contact contact = unwrapParcelledContact();
        lstSms = SmsHelper.getAllSms(getActivity(), contact);
        showWeeklyGraph();

        return view;
    }

    private void showWeeklyGraph() {
        WeeklyGraph weeklyGraph = new WeeklyGraph(getContext(), lineGraph, lstSms);
        weeklyGraph.showWeeklyGraph();
    }

    @Nullable
    private Contact unwrapParcelledContact() {
        return Parcels.unwrap(getActivity().getIntent().getParcelableExtra(ContactListActivity.PARCELLED_CONTACT));
    }

}

