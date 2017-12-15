package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.barcharts;

import com.db.chart.view.BarChartView;


public class TotalSmsBarChart extends BarChart {
    private String[] xAxisLabels = {"Sent", "Received"};

    public TotalSmsBarChart(BarChartView barChartView, int totalWordsReceived, int totalWordsSent) {
        super(barChartView, totalWordsReceived, totalWordsSent);
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