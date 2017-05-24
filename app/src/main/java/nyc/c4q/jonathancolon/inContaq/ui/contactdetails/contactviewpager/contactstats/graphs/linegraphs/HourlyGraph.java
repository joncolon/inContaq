package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.graphs.linegraphs;

import android.view.animation.BounceInterpolator;

import com.db.chart.Tools;
import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;

import static android.graphics.Color.parseColor;
import static com.db.chart.renderer.AxisRenderer.LabelPosition.NONE;
import static com.db.chart.renderer.AxisRenderer.LabelPosition.OUTSIDE;


public class HourlyGraph {
    private static final String SENT_COLOR = "#EF7674";
    private static final String LABEL_COLOR = "#FDFFFC";
    private static final String RECEIVED_COLOR = "#FDFFFC";
    private final String[] xAxisLabels = {
            "12AM", "3AM", "6AM", "9AM", "12PM", "3PM", "6PM", "9PM", "12AM"
    };

    private final LineChartView lineGraph;
    private final float[] hourlySent;
    private final float[] hourlyReceived;

    public HourlyGraph(LineChartView lineGraph, float[] hourlySent, float[] hourlyReceived) {
        this.lineGraph = lineGraph;
        this.hourlyReceived = hourlyReceived;
        this.hourlySent = hourlySent;
    }

    public void showDailyGraph() {
        loadGraph();
    }

    private void loadGraph() {
        setGraphData();
        setGraphAttributes(getYValue());
        animateGraph();
    }

    private void setGraphData() {
        prepareReceivedLineSet(xAxisLabels);
        prepareSentLineSet(xAxisLabels);
    }

    private void setGraphAttributes(int maxYvalue) {
        lineGraph.setBorderSpacing(Tools.fromDpToPx(2))
                .setAxisBorderValues(0, maxYvalue)
                .setYLabels(NONE)
                .setXLabels(OUTSIDE)
                .setFontSize(24)
                .setAxisLabelsSpacing(15f)
                .setLabelsColor(parseColor(LABEL_COLOR))
                .setXAxis(false)
                .setYAxis(false);
    }

    private synchronized int getYValue() {
        int maxSent = findMaximumValue(hourlySent);
        int maxReceived = findMaximumValue(hourlyReceived);
        int highestValue = Math.max(maxSent, maxReceived);
        if (highestValue == 0) {
            return 10;
        }
        return increaseByQuarter(highestValue);
    }

    private void animateGraph() {
        Animation anim = new Animation().setEasing(new BounceInterpolator());
        lineGraph.show(anim);
    }

    private void prepareReceivedLineSet(String[] xAxisLabels) {
        LineSet dataReceivedValues = new LineSet(xAxisLabels, hourlyReceived);
        dataReceivedValues.setColor(parseColor(RECEIVED_COLOR))
                .setDotsColor(parseColor(RECEIVED_COLOR))
                .setThickness(4)
                .beginAt(0);
        lineGraph.addData(dataReceivedValues);
    }

    private void prepareSentLineSet(String[] xAxisLabels) {
        LineSet dataSentValues = new LineSet(xAxisLabels, hourlySent);
        dataSentValues.setColor(parseColor(SENT_COLOR))
                .setDotsColor(parseColor(SENT_COLOR))
                .setDashed(new float[]{1f, 1f})
                .setThickness(4)
                .beginAt(0);
        lineGraph.addData(dataSentValues);
    }

    private int findMaximumValue(float[] inputArray) {
        float maxValue = inputArray[0];
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i] > maxValue) {
                maxValue = Math.round(inputArray[i]);
            }
        }
        return (int) maxValue;
    }

    private int increaseByQuarter(int input) {
        return (int) Math.round(input * 1.25);
    }
}