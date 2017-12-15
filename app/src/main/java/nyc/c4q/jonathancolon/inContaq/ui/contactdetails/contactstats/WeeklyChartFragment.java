package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.view.LineChartView;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.linecharts.WeeklyChart;


public class WeeklyChartFragment extends Fragment {

    final String TAG = MonthlyChartFragment.class.getSimpleName();
    final String SENT_KEY = "weeklySent";
    final String RECEIVED_KEY = "weeklyReceived";

    private LineChartView lineChart;
    private float[] weeklyReceived, weeklySent;

    public WeeklyChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        lineChart = (LineChartView) view.findViewById(R.id.sms_trends_linechart_container);
        Bundle bundle = this.getArguments();
        weeklyReceived = bundle.getFloatArray(RECEIVED_KEY);
        weeklySent = bundle.getFloatArray(SENT_KEY);

        lineChart = (LineChartView) view.findViewById(R.id.sms_trends_linechart_container);
        if (weeklyReceived != null && weeklySent != null) {
            showWeeklyChart();
        }
        return view;
    }

    private void showWeeklyChart() {
        Log.e(TAG, "Loading Weekly Chart");
        WeeklyChart weeklyChart = new WeeklyChart(lineChart, weeklyReceived, weeklySent);
        weeklyChart.showLineChart();
    }

}

