package nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.view.LineChartView;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.WeeklyGraph;


public class WeeklyGraphFragment extends Fragment {

    final String TAG = WeeklyGraphFragment.class.getSimpleName();
    final String BUNDLE_KEY = "phoneNumber";
    private LineChartView lineGraph;
    private String phoneNumber;

    public WeeklyGraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        lineGraph = (LineChartView) view.findViewById(R.id.daily_chart);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            phoneNumber = bundle.getString(BUNDLE_KEY);
        }
        showWeeklyGraph();

        return view;
    }

    private void showWeeklyGraph() {
        Log.e(TAG, "loading Weekly Graph");

        WeeklyGraph weeklyGraph = new WeeklyGraph(getContext(), lineGraph, phoneNumber);
        weeklyGraph.showWeeklyGraph();
    }

}

