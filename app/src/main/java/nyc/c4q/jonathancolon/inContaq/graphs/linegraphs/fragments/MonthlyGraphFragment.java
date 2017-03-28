package nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.view.LineChartView;

import org.parceler.Parcels;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.MonthlyGraph;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;


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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            lstSms = Parcels.unwrap(bundle.getParcelable("smslist"));
            Log.d("missing bundle", lstSms.get(0).getMsg());
            showMonthlyGraph();
        }

        return view;
    }

    private void showMonthlyGraph() {
        MonthlyGraph monthlyGraph = new MonthlyGraph(getContext(), lineGraph, lstSms);
        monthlyGraph.showMonthlyGraph();
    }

}
