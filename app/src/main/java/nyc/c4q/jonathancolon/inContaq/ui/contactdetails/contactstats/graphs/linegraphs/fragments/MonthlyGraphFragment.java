package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.linegraphs.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.view.LineChartView;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.linegraphs.MonthlyGraph;


public class MonthlyGraphFragment extends Fragment {

    final String TAG = MonthlyGraphFragment.class.getSimpleName();
    final String SENT_KEY = "monthlySent";
    final String RECEIVED_KEY = "monthlyReceived";

    private LineChartView lineGraph;
    private float[] monthlySent, monthlyReceived;

    public MonthlyGraphFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        Bundle bundle = this.getArguments();
        monthlyReceived = bundle.getFloatArray(RECEIVED_KEY);
        monthlySent = bundle.getFloatArray(SENT_KEY);

        lineGraph = (LineChartView) view.findViewById(R.id.sms_over_time_graph);
        if (monthlyReceived != null && monthlySent != null) {
            showMonthlyGraph();
        }
        return view;
    }

    private void showMonthlyGraph() {
        Log.e(TAG, "loading Monthly Graph");

        MonthlyGraph monthlyGraph = new MonthlyGraph(lineGraph, monthlyReceived,
                monthlySent);
        monthlyGraph.showMonthlyGraph();
    }
}
