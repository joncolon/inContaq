package nyc.c4q.jonathancolon.inContaq.utlities.graphs;

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

import nyc.c4q.jonathancolon.inContaq.utlities.sms.DailyReceivedWorkerTask;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.DailySentWorkerTask;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.DailyTaskParams;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.Sms;

import static android.graphics.Color.parseColor;
import static com.db.chart.renderer.AxisRenderer.LabelPosition.NONE;

/**
 * Created by Hyun on 3/11/17.
 */

public class DailyGraph{
    private final String BLUE_SAPPHIRE = "#0E587A";
    private final String BLUE_MAASTRICHT = "#02283A";
    private final String RED_ROSE_MADDER = "#E71D36";
    private final String WHITE_BABY_POWDER = "#FDFFFC";
    private final String YELLOW_CRAYOLA = "#FF9F1C";
    private final String DAILY_SENT = "Daily Sent: ";
    private final String DAILY_RECEIVED = "Daily Received: ";
    private final int DEFAULT_VALUE = 0;
    private final String TAG = "sms";
    private float[] receivedValues;
    private float[] sentValues;
    private int highestValue;
    private Context context;
    private LineChartView lineGraph;
    private ArrayList<Sms> lstSms;
    private LineSet dataReceivedValues;
    private LineSet dataSentValues;

    public DailyGraph(Context context, LineChartView lineGraph, ArrayList<Sms> lstSms) {
        this.context = context;
        this.lineGraph = lineGraph;
        this.lstSms = lstSms;
    }

    public void showDailyGraph() {
        getLineGraphValues(lstSms);
        getHighestValueForYaxis();
        loadGraph();
    }

    synchronized private void loadGraph() {
        setGraphData();
        setGraphAttributes();
        animateGraph();
    }

    private void setGraphData() {
        final String[] xAxisLabels = new String[12];
        for (int i = 0; i < 12; i++) {
            if(i==0){
                xAxisLabels[i] = "12AM";
            } else if (i < 12) {
                xAxisLabels[i] = String.valueOf(i) + "AM";
            }
        }

        dataReceivedValues = new LineSet(xAxisLabels, receivedValues);
        dataReceivedValues.setColor(parseColor(YELLOW_CRAYOLA))
                .setDotsColor(parseColor(RED_ROSE_MADDER))
                .setFill(parseColor(BLUE_SAPPHIRE))
                .setThickness(6)
                .beginAt(0);
        lineGraph.addData(dataReceivedValues);

        dataSentValues = new LineSet(xAxisLabels, sentValues);
        dataSentValues.setColor(parseColor("#b01cff"))
                .setDotsColor(parseColor("#1cb7ff"))
                .setDashed(new float[]{15f, 10f})
                .setThickness(6)
                .beginAt(0);
        lineGraph.addData(dataSentValues);
    }

    private void setGraphAttributes() {

        //todo REVIEW I forgot why I did this but I believe it crashes if we don't set this value to 100
        setHighestValueTo100();

        //sets how the graph looks
        lineGraph.setBorderSpacing(Tools.fromDpToPx(2))
                .setAxisBorderValues(0, highestValue)
                .setYLabels(NONE)
                .setLabelsColor(parseColor(WHITE_BABY_POWDER))
                .setXAxis(false)
                .setYAxis(true)
                .setBackgroundColor(parseColor(BLUE_MAASTRICHT));
    }

    private TreeMap<Integer, Integer> setUpDailyTextMap() {
        TreeMap<Integer, Integer> dailyMap = new TreeMap<>();
        for (int i = 0; i < 12; i++) {
            dailyMap.put(i, DEFAULT_VALUE);
        }
        return dailyMap;
    }

    //prevents crash if no sms data is available
    private void setHighestValueTo100() {
        if (highestValue == 0) {
            highestValue = 100;
        }
    }

    private float[] setValues(TreeMap<Integer, Integer> numberOfTexts) {
        ArrayList<Float> list = new ArrayList<Float>();
        for (Map.Entry<Integer, Integer> entry : numberOfTexts.entrySet()) {
            Float value = entry.getValue().floatValue();
            list.add(value);
        }
        return convertFloats(list);
    }

    private void getLineGraphValues(ArrayList<Sms> lstSms) {
        receivedValues = setValues(getDailyReceived(lstSms));
        sentValues = setValues(getDailySent(lstSms));
    }

    private TreeMap<Integer, Integer> getDailyReceived(ArrayList<Sms> texts) {

        TreeMap<Integer, Integer> dailyReceived = setUpDailyTextMap();

        DailyTaskParams receivedParams = new DailyTaskParams(texts, dailyReceived);
        DailyReceivedWorkerTask dailyReceivedWorkerTask = new DailyReceivedWorkerTask();

        try {
            dailyReceivedWorkerTask.execute(receivedParams).get();
            Log.e(TAG, DAILY_RECEIVED + dailyReceived);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return dailyReceived;
    }

    private TreeMap<Integer, Integer> getDailySent(ArrayList<Sms> texts) {

        TreeMap<Integer, Integer> dailySent = setUpDailyTextMap();
        DailyTaskParams sentParams = new DailyTaskParams(texts, dailySent);
        DailySentWorkerTask dailySentWorkTask = new DailySentWorkerTask();

        try {
            dailySent = dailySentWorkTask.execute(sentParams).get();
            Log.e(TAG, DAILY_SENT + dailySent);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return dailySent;
    }

    private void getHighestValueForYaxis() {
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

    private long getRound(int input) {
        //this rounds up and multiples the value by a quarter to customize the Y axis to the contact
        return Math.round(input * 1.25);
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

    private float[] convertFloats(List<Float> floats) {
        float[] ret = new float[floats.size()];
        Iterator<Float> iterator = floats.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    private void animateGraph() {
        Animation anim = new Animation().setEasing(new BounceInterpolator());
        lineGraph.show(anim);
    }

    public LineChartView getLineGraph() {
        return lineGraph;
    }
}