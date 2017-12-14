package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.linegraphs;

import com.db.chart.view.LineChartView;


public class HourlyChart extends LineChart {

    private final String[] xAxisLabels = {
            "12AM", "3AM", "6AM", "9AM", "12PM", "3PM", "6PM", "9PM", "12AM"};

    public HourlyChart(LineChartView lineChart, float[] hourlySent, float[] hourlyReceived) {
        super(hourlySent, hourlyReceived, lineChart);
        setXAxisLabels(xAxisLabels);
    }

    @Override
    public void showLineChart() {
        loadLineChart();
    }

    @Override
    void setXAxisLabels(String[] labels) {
        super.setXAxisLabels(xAxisLabels);
    }
}