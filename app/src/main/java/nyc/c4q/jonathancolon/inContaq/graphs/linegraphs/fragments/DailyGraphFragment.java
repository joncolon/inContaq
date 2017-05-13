package nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.view.LineChartView;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.DailyGraph;


public class DailyGraphFragment extends Fragment {

    final String SENT_KEY = "hourlySent";
    final String RECEIVED_KEY = "hourlyReceived";
    final String TAG = DailyGraphFragment.class.getSimpleName();

    private LineChartView lineGraph;
    private String phoneNumber;
    private float[] hourlySent, hourlyReceived;

    public DailyGraphFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        lineGraph = (LineChartView) view.findViewById(R.id.daily_chart);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            hourlySent = bundle.getFloatArray(SENT_KEY);
            hourlyReceived = bundle.getFloatArray(RECEIVED_KEY);
        }
        showDailyGraph();
        return view;
    }

    private void showDailyGraph() {
        Log.e(TAG, "loading Daily Graph");
        DailyGraph dailyGraph = new DailyGraph(lineGraph, hourlySent, hourlyReceived);
        dailyGraph.showDailyGraph();
    }
}
