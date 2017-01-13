package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
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
import nyc.c4q.jonathancolon.inContaq.utilities.sms.MonthlyReceivedWorkerTask;
import nyc.c4q.jonathancolon.inContaq.utilities.sms.MonthlySentWorkerTask;
import nyc.c4q.jonathancolon.inContaq.utilities.sms.MonthlyTaskParams;
import nyc.c4q.jonathancolon.inContaq.utilities.sms.Sms;
import nyc.c4q.jonathancolon.inContaq.utilities.sms.SmsHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactStatsFragment extends Fragment {
    private final String TAG = "sms";
    private final String[] mLabels = {"Jan", "Fev", "Mar", "Apr", "Jun", "May", "Jul", "Aug", "Sep",
            "Oct", "Nov", "Dec"};
    private Runnable mBaseAction;
    private LineChartView mChart;
    private float[] mValues;

    public static ContactStatsFragment newInstance(String text) {

        ContactStatsFragment fragment = new ContactStatsFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_stats, container, false);
        mChart = (LineChartView) view.findViewById(R.id.daily_chart);
        TextView textview = (TextView) view.findViewById(R.id.sms_stats);

        Contact contact = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("Parcelled Contact"));
        ArrayList<Sms> lstSms = SmsHelper.getAllSms(getActivity(), contact);

        textview.setText("RECEIVED: " + getMonthlyReceieved(lstSms) + "\n" + "\n" + "SENT: " + getMonthlySent(lstSms));

        mValues = setReceivedValues(getMonthlyReceieved(lstSms));
        loadGraph();

        return view;
    }




    private TreeMap setUpMonthlyTextMap() {
        TreeMap<Integer, Integer> monthlyMap = new TreeMap<>();
        monthlyMap.put(1, 0);
        monthlyMap.put(2, 0);
        monthlyMap.put(3, 0);
        monthlyMap.put(4, 0);
        monthlyMap.put(5, 0);
        monthlyMap.put(6, 0);
        monthlyMap.put(7, 0);
        monthlyMap.put(8, 0);
        monthlyMap.put(9, 0);
        monthlyMap.put(10, 0);
        monthlyMap.put(11, 0);
        monthlyMap.put(12, 0);
        return monthlyMap;
    }

    public float[] setReceivedValues(TreeMap numberOfTexts) {
        Map<Integer, Integer> map = numberOfTexts;
        ArrayList<Float> list = new ArrayList<Float>();

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Float value = entry.getValue().floatValue();
            list.add(value);
        }
        convertFloats(list);

        return convertFloats(list);

    }

    private void loadGraph() {
        // Data
        LineSet dataset = new LineSet(mLabels, mValues);
        dataset.setColor(Color.parseColor("#FF9F1C"))
                .setFill(Color.parseColor("#0E587A"))
                .setDotsColor(Color.parseColor("#E71D36"))
                .setThickness(4)
                .beginAt(0);
        mChart.addData(dataset);


        // Chart
        mChart.setBorderSpacing(Tools.fromDpToPx(2))
                .setAxisBorderValues(0, 150)
                .setYLabels(AxisRenderer.LabelPosition.OUTSIDE)
                .setLabelsColor(Color.parseColor("#FDFFFC"))
                .setXAxis(false)
                .setYAxis(false)
                .setBackgroundColor(Color.parseColor("#052635"));


        Animation anim = new Animation().setEasing(new BounceInterpolator());

        mChart.show(anim);

    }

    private float[] convertFloats(List<Float> floats) {
        float[] ret = new float[floats.size()];
        Iterator<Float> iterator = floats.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    private TreeMap<Integer, Integer> getMonthlyReceieved(ArrayList<Sms> texts){
        TreeMap<Integer, Integer> monthlyReceived = setUpMonthlyTextMap();
        MonthlyTaskParams receievedParams = new MonthlyTaskParams(texts, monthlyReceived);
        MonthlyReceivedWorkerTask monthlyReceivedTask = new MonthlyReceivedWorkerTask();
        try {
            monthlyReceived = monthlyReceivedTask.execute(receievedParams).get();
            Log.e(TAG, "Monthly Received: " + monthlyReceived);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return monthlyReceived;
    }

    private TreeMap<Integer, Integer> getMonthlySent(ArrayList<Sms> texts){
        TreeMap<Integer, Integer> monthlySent = setUpMonthlyTextMap();
        MonthlyTaskParams sentParams = new MonthlyTaskParams(texts, monthlySent);
        MonthlySentWorkerTask monthlySentWorkerTask = new MonthlySentWorkerTask();

        try {
            monthlySent = monthlySentWorkerTask.execute(sentParams).get();
            Log.e(TAG, "Monthly Sent: " + monthlySent);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return monthlySent;
    }
}
