package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.linegraphs;


import android.view.animation.BounceInterpolator;

import com.db.chart.Tools;
import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;

import static android.graphics.Color.parseColor;
import static com.db.chart.renderer.AxisRenderer.LabelPosition.NONE;
import static com.db.chart.renderer.AxisRenderer.LabelPosition.OUTSIDE;

abstract class LineChart {

    private static final int MINIMUM_Y_AXIS = 100;
    private static final String SENT_COLOR = "#EF7674";
    private static final String LABEL_COLOR = "#FDFFFC";
    private static final String RECEIVED_COLOR = "#FDFFFC";
    private String[] xAxisLabels;
    private float[] sent;
    private float[] received;
    private LineChartView lineChartView;

    LineChart(float[] sent, float[] received, LineChartView lineChartView) {
        this.sent = sent;
        this.received = received;
        this.lineChartView = lineChartView;
    }

    private void setChartAttributes() {
        lineChartView.setBorderSpacing(Tools.fromDpToPx(2))
                .setAxisBorderValues(0, getYValue())
                .setYLabels(NONE)
                .setXLabels(OUTSIDE)
                .setFontSize(24)
                .setAxisLabelsSpacing(15f)
                .setLabelsColor(parseColor(LABEL_COLOR))
                .setXAxis(false)
                .setYAxis(false);
    }

    private void prepareReceivedLineSet(float[] receivedValues) {
        LineSet dataReceivedValues = new LineSet(xAxisLabels, receivedValues);
        dataReceivedValues.setColor(parseColor(RECEIVED_COLOR))
                .setDotsColor(parseColor(RECEIVED_COLOR))
                .setThickness(4)
                .beginAt(0);
        lineChartView.addData(dataReceivedValues);
    }

    private void prepareSentLineSet(float[] sentValues) {
        LineSet dataSentValues = new LineSet(xAxisLabels, sentValues);
        dataSentValues.setColor(parseColor(SENT_COLOR))
                .setDotsColor(parseColor(SENT_COLOR))
                .setDashed(new float[]{1f, 1f})
                .setThickness(4)
                .beginAt(0);
        lineChartView.addData(dataSentValues);
    }

    public abstract void showLineChart();

    private void setChartData() {
        prepareReceivedLineSet(received);
        prepareSentLineSet(sent);
    }

    synchronized void loadLineChart() {
        setChartData();
        setChartAttributes();
        animateChart();
    }

    //this method to ensure empty space above the highest point in the graph.
    private int addSpaceAboveHighestYValue(int YAxis) {
        return (int) Math.round(YAxis * 1.25);
    }

    private int getYValue() {
        int maxSent = findMaximumValue(sent);
        int maxReceived = findMaximumValue(received);
        int highestValue = Math.max(maxSent, maxReceived);

        if (highestValue == 0) {
            highestValue = MINIMUM_Y_AXIS;
        }
        return addSpaceAboveHighestYValue(highestValue);
    }

    private void animateChart() {
        Animation anim = new Animation().setEasing(new BounceInterpolator());
        lineChartView.show(anim);
    }

    private int findMaximumValue(float[] input) {
        float maxValue = input[0];
        for (int i = 1; i < input.length; i++) {
            if (input[i] > maxValue) {
                maxValue = Math.round(input[i]);
            }
        }
        return (int) maxValue;
    }

    void setXAxisLabels(String[] labels) {
        xAxisLabels = labels;
    }
}
