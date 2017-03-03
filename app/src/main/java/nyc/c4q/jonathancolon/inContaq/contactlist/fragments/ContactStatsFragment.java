package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;

import com.db.chart.Tools;
import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.MonthlyReceivedWorkerTask;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.MonthlySentWorkerTask;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.MonthlyTaskParams;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.SmsHelper;

import static android.graphics.Color.parseColor;
import static com.db.chart.renderer.AxisRenderer.LabelPosition;

//This fragment handles graph logic
public class ContactStatsFragment extends Fragment {

    /*
    These color constants are hardcoded for backwards compatibility.
     */
    private static final String BLUE_SAPPHIRE = "#0E587A";
    private static final String BLUE_MAASTRICHT = "#02283A";
    private static final String RED_ROSE_MADDER = "#E71D36";
    private static final String WHITE_BABY_POWDER = "#FDFFFC";
    private static final String YELLOW_CRAYOLA = "#FF9F1C";

    private static final String MONTHLY_SENT = "Monthly Sent: ";
    private static final String MONTHLY_RECEIVED = "Monthly Received: ";
    private static final int JAN = 1;
    private static final int FEB = 2;
    private static final int MAR = 3;
    private static final int APR = 4;
    private static final int MAY = 5;
    private static final int JUN = 6;
    private static final int JUL = 7;
    private static final int AUG = 8;
    private static final int SEP = 9;
    private static final int OCT = 10;
    private static final int NOV = 11;
    private static final int DEC = 12;
    private static final int DEFAULT_VALUE = 0;
    private final String TAG = "sms";

    int highestValue;
    private LineChartView lineGraph;

    private float[] receivedValues;
    private float[] sentValues;


    public static ContactStatsFragment newInstance() {
        ContactStatsFragment fragment = new ContactStatsFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_stats, container, false);

        initViews(view);
        Contact contact = unwrapParcelledContact();

        //Retrieves all the text messages to parse through
        ArrayList<Sms> lstSms = SmsHelper.getAllSms(getActivity(), contact);

        //Sets values for the line graph by parsing through the sms list
        getLineGraphValues(lstSms);
        getHigehstValueForYaxis();
        loadGraph();

        return view;
    }

    private void initViews(View view) {
        lineGraph = (LineChartView) view.findViewById(R.id.daily_chart);
    }

    @Nullable
    private Contact unwrapParcelledContact() {
        return Parcels.unwrap(getActivity().getIntent().getParcelableExtra(ContactListActivity.PARCELLED_CONTACT));
    }

    private void getLineGraphValues(ArrayList<Sms> lstSms) {
        receivedValues = setValues(getMonthlyReceived(lstSms));
        sentValues = setValues(getMonthlySent(lstSms));
    }

    private void getHigehstValueForYaxis() {
        // Get Y Value takes in the total amount of sent and received texts sent
        highestValue = getYValue(sentValues, receivedValues);
    }

    synchronized private void loadGraph() {
        setGraphData();
        setGraphAttributes();
        animateGraph();
    }

    private float[] setValues(TreeMap<Integer, Integer> numberOfTexts) {

        ArrayList<Float> list = new ArrayList<Float>();
        for (Map.Entry<Integer, Integer> entry : numberOfTexts.entrySet()) {
            Float value = entry.getValue().floatValue();
            list.add(value);
        }
        return convertFloats(list);
    }

    private TreeMap<Integer, Integer> getMonthlyReceived(ArrayList<Sms> texts) {

        TreeMap<Integer, Integer> monthlyReceived = setUpMonthlyTextMap();
        MonthlyTaskParams receivedParams = new MonthlyTaskParams(texts, monthlyReceived);
        MonthlyReceivedWorkerTask monthlyReceivedTask = new MonthlyReceivedWorkerTask();

        try {
            monthlyReceived = monthlyReceivedTask.execute(receivedParams).get();
            Log.e(TAG, MONTHLY_RECEIVED + monthlyReceived);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return monthlyReceived;
    }

    private TreeMap<Integer, Integer> getMonthlySent(ArrayList<Sms> texts) {

        TreeMap<Integer, Integer> monthlySent = setUpMonthlyTextMap();
        MonthlyTaskParams sentParams = new MonthlyTaskParams(texts, monthlySent);
        MonthlySentWorkerTask monthlySentWorkerTask = new MonthlySentWorkerTask();

        try {
            monthlySent = monthlySentWorkerTask.execute(sentParams).get();
            Log.e(TAG, MONTHLY_SENT + monthlySent);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return monthlySent;
    }

    /*
    this compares the total sent / received to find which has the greater value. It then rounds it
    to an int.
    */

    synchronized private int getYValue(float[] sentValues, float[] receivedValues) {
        int maxSent = getMax(sentValues);
        int maxReceived = getMax(receivedValues);
        if (maxSent > maxReceived) {
            return highestValue = (int) getRound(maxSent);
        }
        return highestValue = (int) getRound(maxReceived);
    }

    // You need to read through the WilliamChart Library docs to see how this graph works.
    private void setGraphData() {
        final String[] xAxisLabels =
                {getString(R.string.jan), getString(R.string.feb), getString(R.string.mar),
                        getString(R.string.apr), getString(R.string.jun), getString(R.string.may),
                        getString(R.string.jul), getString(R.string.aug), getString(R.string.sep),
                        getString(R.string.oct), getString(R.string.nov), getString(R.string.dec)};

        // Sets data for the first received values line on the graph
        LineSet dataSet = new LineSet(xAxisLabels, receivedValues);
        dataSet.setColor(parseColor(YELLOW_CRAYOLA))
                .setDotsColor(parseColor(RED_ROSE_MADDER))
                .setFill(parseColor(BLUE_SAPPHIRE))
                .setThickness(6)
                .beginAt(0);
        lineGraph.addData(dataSet);

        // Sets data for the first sent values line on the graph
        LineSet dataset = new LineSet(xAxisLabels, sentValues);
        dataset.setColor(parseColor("#b01cff"))
                .setDotsColor(parseColor("#1cb7ff"))
                .setDashed(new float[]{15f, 10f})
                .setThickness(6)
                .beginAt(0);
        lineGraph.addData(dataset);
    }

    private void setGraphAttributes() {

        //I forgot why I did this but I believe it crashes if we don't set this value to 100 at first
        setHighestValueTo100();

        lineGraph.setBorderSpacing(Tools.fromDpToPx(2))
                .setAxisBorderValues(0, highestValue)
                .setYLabels(LabelPosition.NONE)
                .setLabelsColor(parseColor(WHITE_BABY_POWDER))
                .setXAxis(false)
                .setYAxis(true)
                .setBackgroundColor(parseColor(BLUE_MAASTRICHT));
    }

    private void animateGraph() {
        Animation anim = new Animation().setEasing(new BounceInterpolator());
        lineGraph.show(anim);
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

    /*
    Here we create a TreeMap with months as the Key. The Default Value is set to 0 so a count of
    texts can be added to each month.

    FYI: We use TreeMaps rather than HashMaps because TreeMaps can be sorted.
     */
    private TreeMap<Integer, Integer> setUpMonthlyTextMap() {
        TreeMap<Integer, Integer> monthlyMap = new TreeMap<>();
        monthlyMap.put(JAN, DEFAULT_VALUE);
        monthlyMap.put(FEB, DEFAULT_VALUE);
        monthlyMap.put(MAR, DEFAULT_VALUE);
        monthlyMap.put(APR, DEFAULT_VALUE);
        monthlyMap.put(MAY, DEFAULT_VALUE);
        monthlyMap.put(JUN, DEFAULT_VALUE);
        monthlyMap.put(JUL, DEFAULT_VALUE);
        monthlyMap.put(AUG, DEFAULT_VALUE);
        monthlyMap.put(SEP, DEFAULT_VALUE);
        monthlyMap.put(OCT, DEFAULT_VALUE);
        monthlyMap.put(NOV, DEFAULT_VALUE);
        monthlyMap.put(DEC, DEFAULT_VALUE);
        return monthlyMap;
    }

    public static int getMax(float[] inputArray) {
        float maxValue = inputArray[0];
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i] > maxValue) {
                maxValue = Math.round(inputArray[i]);
            }
        }
        return (int) maxValue;
    }

    private long getRound(int input) {
        //this rounds up and multiples the value by a quarter to customize the Y axis to the contact
        return Math.round(input * 1.25);
    }

    private void setHighestValueTo100() {
        if (highestValue == 0) {
            highestValue = 100;
        }
    }
}
