package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.view.LineChartView;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.linecharts.MonthlyChart;


public class MonthlyChartFragment extends Fragment {

    final String TAG = MonthlyChartFragment.class.getSimpleName();
    final String SENT_KEY = "monthlySent";
    final String RECEIVED_KEY = "monthlyReceived";

    private LineChartView lineChart;
    private float[] monthlySent, monthlyReceived;

    public MonthlyChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        Bundle bundle = this.getArguments();
        monthlyReceived = bundle.getFloatArray(RECEIVED_KEY);
        monthlySent = bundle.getFloatArray(SENT_KEY);

        lineChart = (LineChartView) view.findViewById(R.id.sms_trends_linechart_container);
        if (monthlyReceived != null && monthlySent != null) {
            showMonthlyChart();
        }
        return view;
    }

    private void showMonthlyChart() {
        Log.e(TAG, "Loading Monthly Chart");

        MonthlyChart monthlyChart = new MonthlyChart(lineChart, monthlyReceived,
                monthlySent);
        monthlyChart.showLineChart();
    }
}
