package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.linegraphs;


import com.db.chart.view.LineChartView;



public class WeeklyChart extends LineChart {

    private final String[] xAxisLabels = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};


    public WeeklyChart(LineChartView lineChart, float[] weeklyReceived, float[] weeklySent) {
        super(weeklySent, weeklyReceived, lineChart);
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
