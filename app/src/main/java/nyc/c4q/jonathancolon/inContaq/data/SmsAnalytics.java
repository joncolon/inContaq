package nyc.c4q.jonathancolon.inContaq.data;

import android.util.Log;

import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import nyc.c4q.jonathancolon.inContaq.data.asynctasks.MonthlyReceivedWorkerTask;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.MonthlySentWorkerTask;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.params.MonthlyTaskParams;


public class SmsAnalytics {

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
    private static final String TAG = SmsAnalytics.class.getSimpleName();
    private final int DEFAULT_VALUE = 0;
    private final String phoneNumber;
    private TreeMap<Integer, Integer> monthlySentTreeMap,
            monthlyReceivedTreeMap;
    private float[] hourlyReceived, hourlySent, monthlyReceived, monthlySent;
    private boolean isHourlyDataRetrieved, isMonthlyDataRetrieved;
    private String maxTimeReceived;



    public SmsAnalytics(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        loadMonthlyData();
    }

    public void loadMonthlyData() {
        Log.e(TAG, "loadMonthlyData: " + isMonthlyDataRetrieved);
        if (!isMonthlyDataRetrieved) {
            calculateMonthlyReceived();
            calculateMonthlySent();
            isMonthlyDataRetrieved = true;
            Log.e(TAG, "loadMonthlyData: loading monthly data");
        }
    }

    public float[] getMonthlyReceived() {
        return monthlyReceived;
    }


    public void setMaxTimeReceived(String maxTimeReceived) {
        this.maxTimeReceived = maxTimeReceived;
    }

    public TreeMap<Integer, Integer> getMonthlySentTreeMap() {
        return monthlySentTreeMap;
    }

    public TreeMap<Integer, Integer> getMonthlyReceivedTreeMap() {
        return monthlyReceivedTreeMap;
    }

    private void setMonthlyReceivedTreeMap(TreeMap<Integer, Integer> monthlyReceivedTreeMap) {
        this.monthlyReceivedTreeMap = monthlyReceivedTreeMap;
    }

    public float[] getMonthlySent() {
        return monthlySent;
    }


    // TODO: 5/8/17 call these methods before any other in this class
    synchronized private TreeMap<Integer, Integer> calculateMonthlySent() {
        monthlySentTreeMap = setUpMonthlyTreeMap();
        MonthlyTaskParams sentParams = new MonthlyTaskParams(phoneNumber, monthlySentTreeMap);
        MonthlySentWorkerTask monthlySentWorkerTask = new MonthlySentWorkerTask();

        try {
            monthlySentTreeMap = monthlySentWorkerTask.execute(sentParams).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return monthlySentTreeMap;
    }

    private TreeMap<Integer, Integer> setUpMonthlyTreeMap() {
        TreeMap<Integer, Integer> monthlyMapTemplate = new TreeMap<>();
        monthlyMapTemplate.put(JAN, DEFAULT_VALUE);
        monthlyMapTemplate.put(FEB, DEFAULT_VALUE);
        monthlyMapTemplate.put(MAR, DEFAULT_VALUE);
        monthlyMapTemplate.put(APR, DEFAULT_VALUE);
        monthlyMapTemplate.put(MAY, DEFAULT_VALUE);
        monthlyMapTemplate.put(JUN, DEFAULT_VALUE);
        monthlyMapTemplate.put(JUL, DEFAULT_VALUE);
        monthlyMapTemplate.put(AUG, DEFAULT_VALUE);
        monthlyMapTemplate.put(SEP, DEFAULT_VALUE);
        monthlyMapTemplate.put(OCT, DEFAULT_VALUE);
        monthlyMapTemplate.put(NOV, DEFAULT_VALUE);
        monthlyMapTemplate.put(DEC, DEFAULT_VALUE);
        return monthlyMapTemplate;
    }

    synchronized private TreeMap<Integer, Integer> calculateMonthlyReceived() {

        monthlyReceivedTreeMap = setUpMonthlyTreeMap();
        MonthlyTaskParams sentParams = new MonthlyTaskParams(phoneNumber, monthlyReceivedTreeMap);
        MonthlyReceivedWorkerTask monthlyReceivedWorkerTask = new MonthlyReceivedWorkerTask();

        try {
            monthlyReceivedTreeMap = monthlyReceivedWorkerTask.execute(sentParams).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        setMonthlyReceivedTreeMap(monthlyReceivedTreeMap);
        return monthlyReceivedTreeMap;
    }



}
