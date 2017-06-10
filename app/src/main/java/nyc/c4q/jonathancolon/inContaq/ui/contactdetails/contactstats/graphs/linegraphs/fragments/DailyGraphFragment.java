package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.linegraphs.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.view.LineChartView;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.linegraphs.HourlyGraph;


public class DailyGraphFragment extends Fragment {

    final String SENT_KEY = "hourlySent";
    final String RECEIVED_KEY = "hourlyReceived";
    final String TAG = DailyGraphFragment.class.getSimpleName();

    private LineChartView lineGraph;
    private float[] hourlySent, hourlyReceived;

    public DailyGraphFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        lineGraph = (LineChartView) view.findViewById(R.id.sms_over_time_graph);
        Bundle bundle = this.getArguments();
        hourlyReceived = bundle.getFloatArray(RECEIVED_KEY);
        hourlySent = bundle.getFloatArray(SENT_KEY);

        if (hourlyReceived != null && hourlySent != null) {
            showDailyGraph();
        }
        return view;
    }

    private void showDailyGraph() {
        Log.e(TAG, "loading Daily Graph");
        HourlyGraph hourlyGraph = new HourlyGraph(lineGraph, hourlySent, hourlyReceived);
        hourlyGraph.showDailyGraph();
    }
}
