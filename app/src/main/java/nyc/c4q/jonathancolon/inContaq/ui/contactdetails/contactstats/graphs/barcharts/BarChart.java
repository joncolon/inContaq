package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.barcharts;

import android.graphics.Color;

import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;

import static com.db.chart.Tools.fromDpToPx;
import static com.db.chart.renderer.AxisRenderer.LabelPosition.NONE;

abstract class BarChart {

    private static final int MINIMUM_Y_AXIS = 10;
    private static final String SENT_COLOR = "#EF7674";
    private static final String FILL_COLOR = "#000000";
    private static final String RECEIVED_COLOR = "#FDFFFC";
    BarChartView barChartView;
    private int sent;
    private int received;
    private String[] xAxisLabels = {"Sent", "Received"};

    BarChart(BarChartView barChartView, int received, int sent) {
        this.barChartView = barChartView;
        this.received = received;
        this.sent = sent;
    }

    public abstract void showBarChart();

    void loadChart() {
        setChartData();
        setChartAttributes();
    }

    private void setChartData() {
        BarSet barSet = new BarSet();
        Bar barSent = new Bar(xAxisLabels[0], sent);
        Bar barReceived = new Bar(xAxisLabels[1], received);
        barReceived.setColor(Color.parseColor(RECEIVED_COLOR));
        barSent.setColor(Color.parseColor(SENT_COLOR));
        barSet.addBar(barSent);
        barSet.addBar(barReceived);

        barChartView.addData(barSet);
    }

    private void setChartAttributes() {
        barChartView.setBarSpacing(fromDpToPx(15));
        barChartView.setRoundCorners(fromDpToPx(2));
        barChartView.setBarBackgroundColor(Color.parseColor(FILL_COLOR));

        barChartView.setXAxis(false)
                .setYAxis(false)
                .setAxisBorderValues(0, getYAxisValue())
                .setXLabels(NONE)
                .setYLabels(NONE);
    }

    private int getYAxisValue() {
        int highestValue = Math.max(received, sent);
        if (highestValue == 0) {
            highestValue = MINIMUM_Y_AXIS;
        }
        return addSpaceAboveHighestYValue(highestValue);
    }

    //this method to ensure empty space above the highest point in the graph.
    private int addSpaceAboveHighestYValue(int YAxis) {
        return (int) Math.round(YAxis * 1.25);
    }

    void setXAxisLabels(String[] labels) {
        xAxisLabels = labels;
    }
}
