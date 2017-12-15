package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.linecharts;

import com.db.chart.view.LineChartView;

public class MonthlyChart extends LineChart {

    private String[] xAxisLabels = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep",
            "Oct", "Nov", "Dec"};

    public MonthlyChart(LineChartView lineChartView, float[] monthlyReceived, float[] monthlySent) {
        super(monthlySent, monthlyReceived, lineChartView);
        setXAxisLabels(xAxisLabels);
    }

    @Override
    public void showLineChart() {
        loadLineChart();
    }

    @Override
    void setXAxisLabels(String[] labels) {
        super.setXAxisLabels(labels);
    }
}
