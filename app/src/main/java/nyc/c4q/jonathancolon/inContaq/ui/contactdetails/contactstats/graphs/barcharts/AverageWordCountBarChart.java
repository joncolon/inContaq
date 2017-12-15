package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.barcharts;

import com.db.chart.view.BarChartView;


public class AverageWordCountBarChart extends BarChart {

    private String[] xAxisLabels = {"Sent", "Received"};

    public AverageWordCountBarChart(BarChartView barChartView, int averageWordsReceived, int averageWordsSent) {
        super(barChartView, averageWordsReceived, averageWordsSent);
        setXAxisLabels(xAxisLabels);
    }

    @Override
    public void showBarChart() {
        loadChart();
        barChartView.show();
    }

    @Override
    void setXAxisLabels(String[] labels) {
        super.setXAxisLabels(labels);
    }
}
