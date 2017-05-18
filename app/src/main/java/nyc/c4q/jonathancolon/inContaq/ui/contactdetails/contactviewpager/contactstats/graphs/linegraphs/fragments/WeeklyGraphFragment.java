package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.graphs.linegraphs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.view.LineChartView;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.graphs.linegraphs.WeeklyGraph;


public class WeeklyGraphFragment extends Fragment {

    final String TAG = MonthlyGraphFragment.class.getSimpleName();
    final String SENT_KEY = "weeklySent";
    final String RECEIVED_KEY = "weeklyReceived";

    private LineChartView lineGraph;
    private float[] weeklyReceived, weeklySent;

    public WeeklyGraphFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        lineGraph = (LineChartView) view.findViewById(R.id.sms_over_time_graph);
        Bundle bundle = this.getArguments();
        weeklyReceived = bundle.getFloatArray(RECEIVED_KEY);
        weeklySent = bundle.getFloatArray(SENT_KEY);

        lineGraph = (LineChartView) view.findViewById(R.id.sms_over_time_graph);
        if (weeklyReceived != null && weeklySent != null) {
            showWeeklyGraph();
        }
        return view;
    }

    private void showWeeklyGraph() {
        Log.e(TAG, "loading Weekly Graph");

        WeeklyGraph weeklyGraph = new WeeklyGraph(lineGraph, weeklyReceived, weeklySent);
        weeklyGraph.showWeeklyGraph();
    }

}

