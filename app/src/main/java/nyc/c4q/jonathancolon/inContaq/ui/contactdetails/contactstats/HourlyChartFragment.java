package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.view.LineChartView;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.linecharts.HourlyChart;


public class HourlyChartFragment extends Fragment {

    final String SENT_KEY = "hourlySent";
    final String RECEIVED_KEY = "hourlyReceived";
    final String TAG = HourlyChartFragment.class.getSimpleName();

    private LineChartView lineChart;
    private float[] hourlySent, hourlyReceived;

    public HourlyChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        lineChart = (LineChartView) view.findViewById(R.id.sms_trends_linechart_container);
        Bundle bundle = this.getArguments();
        hourlyReceived = bundle.getFloatArray(RECEIVED_KEY);
        hourlySent = bundle.getFloatArray(SENT_KEY);

        if (hourlyReceived != null && hourlySent != null) {
            showHourlyChart();
        }
        return view;
    }

    private void showHourlyChart() {
        Log.e(TAG, "Loading Hourly Chart");
        HourlyChart hourlyChart = new HourlyChart(lineChart, hourlySent, hourlyReceived);
        hourlyChart.showLineChart();
    }
}
