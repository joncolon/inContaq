package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.LineChartView;

import org.joda.time.DateTime;
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
    private Contact contact;
    private ArrayList<Sms> lstSms;
    private TextView textview;
    private TreeMap<Integer, Integer> monthlyReceived;
    private TreeMap<Integer, Integer> monthlySent;
    private LineChartView mChart;
    private final String[] mLabels = {"Jan", "Fev", "Mar", "Apr", "Jun", "May", "Jul", "Aug", "Sep",
            "Oct", "Nov", "Dec"};
//    private final float[] mValues = {10f, 4.7f, 4.3f, 8f, 6.5f, 9.9f, 7f, 8.3f, 7.0f, 1.5f, 2.5f, 3.5f};
    private float[] mValues;

    public static ContactStatsFragment newInstance(String text) {

        ContactStatsFragment fragment = new ContactStatsFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_stats, container, false);
        mChart = (LineChartView) view.findViewById(R.id.daily_chart);
        textview = (TextView) view.findViewById(R.id.sms_stats);

        contact = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("Parcelled Contact"));
        lstSms = SmsHelper.getAllSms(getActivity(), contact);

        monthlySent = setUpMonthlyTextMap();
        monthlyReceived = setUpMonthlyTextMap();
        MonthlyTaskParams receievedParams = new MonthlyTaskParams(lstSms, monthlyReceived);
        MonthlyReceivedWorkerTask monthlyReceivedTask = new MonthlyReceivedWorkerTask();

        MonthlyTaskParams sentParams = new MonthlyTaskParams(lstSms, monthlySent);
        MonthlySentWorkerTask monthlySentWorkerTask = new MonthlySentWorkerTask();

        try {
            monthlySent = monthlySentWorkerTask.execute(sentParams).get();
            Log.e(TAG, "Monthly Sent: " + monthlySent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            monthlyReceived = monthlyReceivedTask.execute(receievedParams).get();
            Log.e(TAG, "Monthly Received: " + monthlyReceived);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        textview.setText("RECEIVED: " + monthlyReceived + "\n" + "\n" + "SENT: " + monthlySent);

        mValues = setReceivedValues(monthlyReceived);
        loadGraph();

        return view;
    }

    private void getSmsStats(ArrayList smsList, Contact contact){
        smsList = lstSms;
        ArrayList<Sms> receievedSms = new ArrayList<>();
        ArrayList<Sms> sentSms = new ArrayList<>();
        DateTime juDate;

        for (int i = 0; i < lstSms.size(); i++) {
            if (lstSms.get(i).getType().equals("1")) {
                receievedSms.add(lstSms.get(i));
            }
            if (lstSms.get(i).getType().equals("2")) {
                sentSms.add(lstSms.get(i));
            }
        }

        for (int i = 0; i < sentSms.size(); i++) {
            long lg;
            lg = Long.parseLong(sentSms.get(i).getTime());
            juDate = new DateTime(lg);
            Log.e(TAG, "Month Sent: " + juDate.getMonthOfYear());
            getMonthlyTexts(sentSms);

        }
        for (int i = 0; i < receievedSms.size(); i++) {
        }
    }

    private TreeMap setUpMonthlyTextMap(){
        TreeMap<Integer, Integer> monthlyMap = new TreeMap<>();
        monthlyMap.put(1,0);
        monthlyMap.put(2,0);
        monthlyMap.put(3,0);
        monthlyMap.put(4,0);
        monthlyMap.put(5,0);
        monthlyMap.put(6,0);
        monthlyMap.put(7,0);
        monthlyMap.put(8,0);
        monthlyMap.put(9,0);
        monthlyMap.put(10,0);
        monthlyMap.put(11,0);
        monthlyMap.put(12,0);
        return monthlyMap;
    }

    private TreeMap<Integer, Integer> getMonthlyTexts(ArrayList<Sms> list){
        DateTime juDate;

        for (Map.Entry<Integer, Integer> entry : monthlyReceived.entrySet())
        {
            for (int i = 0; i < list.size(); i++) {
                long lg;
                lg = Long.parseLong(list.get(i).getTime());
                juDate = new DateTime(lg);
                int month = juDate.getMonthOfYear();

                if (month == entry.getKey()){
                    monthlyReceived.put(month, entry.getKey()+1);
                }
            }
        }
        return monthlyReceived;
    }

    private void loadGraph(){
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
                .setAxisBorderValues(0, 100)
                .setYLabels(AxisRenderer.LabelPosition.OUTSIDE)
                .setLabelsColor(Color.parseColor("#6a84c3"))
                .setXAxis(true)
                .setYAxis(true);
        mChart.show();

    }

    public float[] setReceivedValues(TreeMap numberOfTexts){
        float[] convertedValues = new float[]{};
        Map<Integer, Integer> map = numberOfTexts;
        ArrayList<Float> list = new ArrayList<Float>();

        for(Map.Entry<Integer,Integer> entry : map.entrySet()) {
            Float value = entry.getValue().floatValue();
            list.add(value);
        }
        convertFloats(list);

        return convertFloats(list);

    }

    public static float[] convertFloats(List<Float> floats)
    {
        float[] ret = new float[floats.size()];
        Iterator<Float> iterator = floats.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }
}
