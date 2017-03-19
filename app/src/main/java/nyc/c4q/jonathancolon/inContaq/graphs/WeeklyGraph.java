package nyc.c4q.jonathancolon.inContaq.graphs;

import android.content.Context;
import android.util.Log;
import android.view.animation.BounceInterpolator;

import com.db.chart.Tools;
import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.weekly.WeeklyReceived;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.weekly.WeeklySent;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.weekly.WeeklyTask;

import static android.graphics.Color.parseColor;
import static com.db.chart.renderer.AxisRenderer.LabelPosition.NONE;

public class WeeklyGraph {

    private static final String BLUE_SAPPHIRE = "#0E587A";
    private static final String BLUE_MASTRICHT = "#02283A";
    private static final String RED_ROSE_MADDER = "#E71D36";
    private static final String WHITE_BABY_POWDER = "#FDFFFC";
    private static final String YELLOW_CRAYOLA = "#FF9F1C";
    private static final String WEEKLY_SENT = "Monthly Sent: ";
    private static final String WEEKLY_RECEIVED = "Monthly Received: ";
    private static final int SUN = 1;
    private static final int MON = 2;
    private static final int TUE = 3;
    private static final int WED = 4;
    private static final int THUR = 5;
    private static final int FRI = 6;
    private static final int SAT = 7;
    private static final int DEFAULT_VALUE = 0;
    private final String TAG = "sms";
    private Context context;
    private int highestValue;
    private LineChartView lineGraph;
    private float[] receivedValues;
    private float[] sentValues;
    private ArrayList<Sms> lstSms;

    public WeeklyGraph(Context context, LineChartView lineGraph, ArrayList<Sms> lstSms) {
        this.context = context;
        this.lineGraph = lineGraph;
        this.lstSms = lstSms;
    }

    public void showWeeklyGraph() {
        getLineGraphValues(lstSms);
        getHigehstValueForYaxis();
        loadGraph();
    }

    private void getLineGraphValues(ArrayList<Sms> lstSms) {
        receivedValues = setValues(getDailyReceived(lstSms));
        sentValues = setValues(getWeeklySent(lstSms));
    }

    private float[] setValues(TreeMap<Integer, Integer> numberOfTexts) {

        ArrayList<Float> list = new ArrayList<Float>();
        for (Map.Entry<Integer, Integer> entry : numberOfTexts.entrySet()) {

            Float value = entry.getValue().floatValue();
            list.add(value);
        }
        return convertFloats(list);
    }

    private TreeMap<Integer, Integer> getDailyReceived(ArrayList<Sms> texts) {

        TreeMap<Integer, Integer> weeklyReceived = setUpWeeklyTextMap();
        WeeklyTask receivedWeeklyParams = new WeeklyTask(texts, weeklyReceived);
        WeeklyReceived weeklyReceivedTask = new WeeklyReceived();

        try {

            weeklyReceived = weeklyReceivedTask.execute(receivedWeeklyParams).get();
            Log.e(TAG, WEEKLY_RECEIVED + weeklyReceived);

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();
        }
        return weeklyReceived;
    }

    private TreeMap<Integer, Integer> getWeeklySent(ArrayList<Sms> texts) {

        TreeMap<Integer, Integer> weeklySent = setUpWeeklyTextMap();
        WeeklyTask sentWeeklyParams = new WeeklyTask(texts, weeklySent);
        WeeklySent weeklySentWorkerTask = new WeeklySent();

        try {
            weeklySent = weeklySentWorkerTask.execute(sentWeeklyParams).get();
            Log.e(TAG, WEEKLY_SENT + weeklySent);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return weeklySent;
    }

    // This converts the generic "Float" to primitive float because the graphs only uses primitives
    private float[] convertFloats(List<Float> floats) {

        float[] ret = new float[floats.size()];
        Iterator<Float> iterator = floats.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    private TreeMap <Integer, Integer> setUpWeeklyTextMap() {

        TreeMap<Integer, Integer> weeklyMap = new TreeMap<>();

        weeklyMap.put(SUN, DEFAULT_VALUE);
        weeklyMap.put(MON, DEFAULT_VALUE);
        weeklyMap.put(TUE, DEFAULT_VALUE);
        weeklyMap.put(WED, DEFAULT_VALUE);
        weeklyMap.put(THUR, DEFAULT_VALUE);
        weeklyMap.put(FRI, DEFAULT_VALUE);
        weeklyMap.put(SAT, DEFAULT_VALUE);

        return weeklyMap;
    }

    private void getHigehstValueForYaxis() {
        highestValue = getYValue(sentValues, receivedValues);
    }

    synchronized private int getYValue(float[] sentValues, float[] receivedValues) {
        int maxSent = findMaximumValue(sentValues);
        int maxReceived = findMaximumValue(receivedValues);
        if (maxSent > maxReceived) {
            return highestValue = (int) getRound(maxSent);
        }
        return highestValue = (int) getRound(maxReceived);
    }

    private static int findMaximumValue(float[] inputArray) {

        float maxValue = inputArray[0];

        for (int i = 1; i < inputArray.length; i++) {

            if (inputArray[i] > maxValue) {
                maxValue = Math.round(inputArray[i]);
            }
        }
        return (int) maxValue;
    }

    private long getRound(int input) {
        return Math.round(input * 1.25);
    }

    synchronized private void loadGraph() {

        setGraphData();
        setGraphAttributes();
        animateGraph();
    }

    private void setGraphData() {

        final String[] xAxisLabels = {
                context.getString(R.string.sun),
                context.getString(R.string.mon),
                context.getString(R.string.tue),
                context.getString(R.string.wed),
                context.getString(R.string.thu),
                context.getString(R.string.fri),
                context.getString(R.string.sat)
        };

        LineSet receivedValueDataSet = new LineSet(xAxisLabels, receivedValues);
        receivedValueDataSet.setColor(parseColor(YELLOW_CRAYOLA))
                .setDotsColor(parseColor(RED_ROSE_MADDER))
                .setFill(parseColor(BLUE_SAPPHIRE))
                .setThickness(6)
                .beginAt(0);
        lineGraph.addData(receivedValueDataSet);

        LineSet sentValueDataSet = new LineSet(xAxisLabels, sentValues);
        sentValueDataSet.setColor(parseColor("#b01cff"))
                .setDotsColor(parseColor("#1cb7ff"))
                .setDashed(new float[]{15f, 10f})
                .setThickness(6)
                .beginAt(0);
        lineGraph.addData(sentValueDataSet);
    }

    private void setGraphAttributes() {

        //todo REVIEW I forgot why I did this but I believe it crashes if we don't set this value to 100
        setHighestValueTo100();

        lineGraph.setBorderSpacing(Tools.fromDpToPx(2))
                .setAxisBorderValues(0, highestValue)
                .setYLabels(NONE)
                .setLabelsColor(parseColor(WHITE_BABY_POWDER))
                .setXAxis(false)
                .setYAxis(true)
                .setBackgroundColor(parseColor(BLUE_MASTRICHT));
    }

    private void animateGraph() {

        Animation anim = new Animation().setEasing(new BounceInterpolator());
        lineGraph.show(anim);
    }

    private void setHighestValueTo100() {

        if (highestValue == 0) {
            highestValue = 100;
        }
    }
}
