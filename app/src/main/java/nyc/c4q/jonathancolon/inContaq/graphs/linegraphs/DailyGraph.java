package nyc.c4q.jonathancolon.inContaq.graphs.linegraphs;

import android.view.animation.BounceInterpolator;

import com.db.chart.Tools;
import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;

import static android.graphics.Color.parseColor;
import static com.db.chart.renderer.AxisRenderer.LabelPosition.NONE;
import static com.db.chart.renderer.AxisRenderer.LabelPosition.OUTSIDE;



public class DailyGraph {
    private static final String SENT_COLOR = "#EF7674";
    private static final String LABEL_COLOR = "#FDFFFC";
    private static final String RECEIVED_COLOR = "#FDFFFC";
    private final String[] xAxisLabels = {
            "12AM", "3AM", "6AM", "9AM", "12PM", "3PM", "6PM", "9PM", "12AM"
    };

    private LineChartView lineGraph;
    private float[] hourlySent;
    private float[] hourlyReceived;

    public DailyGraph(LineChartView lineGraph, float[] hourlySent, float[] hourlyReceived) {
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
        float[] receivedValues = hourlyReceived;
        float[] sentValues = hourlySent;
        prepareReceivedLineSet(xAxisLabels, receivedValues);
        prepareSentLineSet(xAxisLabels, sentValues);
    }

    private void prepareSentLineSet(String[] xAxisLabels, float[] sentValues) {
        LineSet dataSentValues = new LineSet(xAxisLabels, sentValues);
        dataSentValues.setColor(parseColor(SENT_COLOR))
                .setDotsColor(parseColor(SENT_COLOR))
                .setDashed(new float[]{1f, 1f})
                .setThickness(4)
                .beginAt(0);
        lineGraph.addData(dataSentValues);
    }

    private void prepareReceivedLineSet(String[] xAxisLabels, float[] receivedValues) {
        LineSet dataReceivedValues = new LineSet(xAxisLabels, receivedValues);
        dataReceivedValues.setColor(parseColor(RECEIVED_COLOR))
                .setDotsColor(parseColor(RECEIVED_COLOR))
                .setThickness(4)
                .beginAt(0);
        lineGraph.addData(dataReceivedValues);
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

    private void animateGraph() {
        Animation anim = new Animation().setEasing(new BounceInterpolator());
        lineGraph.show(anim);
    }

    private synchronized int getYValue() {
        int maxSent = findMaximumValue(hourlySent);
        int maxReceived = findMaximumValue(hourlyReceived);
        int highestValue = Math.max(maxSent, maxReceived);
        if (highestValue == 0){
            return 10;
        }
        return increaseByQuarter(highestValue);
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

// TODO: 5/8/17 delete
//    @NonNull
//    synchronized String[] getXAxisLabels() {
//        final String[] xAxisLabels = new String[9];
//        int n = 0;
//        for (int i = 0; i < 4; i++) { // 12am=0, 3am=1, 6am=2 ,9am=3
//            if (i == 0) {
//                xAxisLabels[n] = "12AM";
//                n += 1;
//            } else {
//                xAxisLabels[n] = String.valueOf(i * 3) + "AM";
//                n += 1;
//            }
//        }
//        for (int i = 0; i <= 4; i++) { // 12pm=0, 3pm=1, 6pm=2, 9pm=3, 12am=4
//            if (i == 0) {
//                xAxisLabels[n] = "12PM";
//                n += 1;
//            } else if (i == 4) {
//                xAxisLabels[n] = "12AM";
//                n += 1;
//            } else {
//                xAxisLabels[n] = String.valueOf(i * 3) + "PM";
//                n += 1;
//            }
//        }
//        return xAxisLabels;
//    }


}