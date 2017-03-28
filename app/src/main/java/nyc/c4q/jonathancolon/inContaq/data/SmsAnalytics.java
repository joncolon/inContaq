package nyc.c4q.jonathancolon.inContaq.data;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import nyc.c4q.jonathancolon.inContaq.data.asynctasks.DailyReceivedWorkerTask;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.DailySentWorkerTask;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.params.DailyTaskParams;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.MonthlyReceivedWorkerTask;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.MonthlySentWorkerTask;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.params.MonthlyTaskParams;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;


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
    private final int DEFAULT_VALUE = 0;
    private ArrayList<Sms> smsList;

    public SmsAnalytics(ArrayList<Sms> smsList) {
        this.smsList = smsList;
    }
//todo make these methods static
    public float[] getMonthlySentValues(ArrayList<Sms> smsList) {
        return convertTreeMapToFloats(getMonthlySent(smsList));
    }

    public float[] getMonthlyReceivedValues(ArrayList<Sms> smsList) {
        return convertTreeMapToFloats(getMonthlyReceived(smsList));
    }

    public float[] getHourlySentValues(ArrayList<Sms> smsList) {
        return convertTreeMapToFloats(getHourlySent(smsList));
    }

    public float[] getHourlyReceivedValues(ArrayList<Sms> smsList) {
        return convertTreeMapToFloats(getHourlyReceived(smsList));
    }

    private float[] convertFloats(List<Float> floats) {
        float[] ret = new float[floats.size()];
        Iterator<Float> iterator = floats.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    private TreeMap<Integer, Integer> getHourlySent(ArrayList<Sms> texts) {
        TreeMap<Integer, Integer> dailySent = setUpDailyTextMap();
        DailyTaskParams sentParams = new DailyTaskParams(texts, dailySent);
        DailySentWorkerTask dailySentWorkTask = new DailySentWorkerTask();

        try {
            dailySent = dailySentWorkTask.execute(sentParams).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return dailySent;
    }

    private TreeMap<Integer, Integer> getHourlyReceived(ArrayList<Sms> texts) {
        TreeMap<Integer, Integer> dailyReceived = setUpDailyTextMap();
        DailyTaskParams receivedParams = new DailyTaskParams(texts, dailyReceived);
        DailyReceivedWorkerTask dailyReceivedWorkerTask = new DailyReceivedWorkerTask();

        try {
            dailyReceivedWorkerTask.execute(receivedParams).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return dailyReceived;
    }

    private TreeMap<Integer, Integer> getMonthlySent(ArrayList<Sms> texts) {
        TreeMap<Integer, Integer> monthlySent = setUpMonthlyTextMap();
        MonthlyTaskParams sentParams = new MonthlyTaskParams(texts, monthlySent);
        MonthlySentWorkerTask monthlySentWorkerTask = new MonthlySentWorkerTask();

        try {
            monthlySent = monthlySentWorkerTask.execute(sentParams).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return monthlySent;
    }

    private TreeMap<Integer, Integer> getMonthlyReceived(ArrayList<Sms> texts) {

        TreeMap<Integer, Integer> monthlyReceived = setUpMonthlyTextMap();
        MonthlyTaskParams sentParams = new MonthlyTaskParams(texts, monthlyReceived);
        MonthlyReceivedWorkerTask monthlyReceivedWorkerTask = new MonthlyReceivedWorkerTask();

        try {
            monthlyReceived = monthlyReceivedWorkerTask.execute(sentParams).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return monthlyReceived;
    }

    private TreeMap<Integer, Integer> setUpDailyTextMap() {
        TreeMap<Integer, Integer> dailyMap = new TreeMap<>();
        int m = 0;
        for (int i = 0; i < 9; i++) { // 12am=0, 3am=1, 6am=2 ,9am=3,12pm=4, (15)3pm=5, (18)6pm=6, (21)9pm=7, (24)12am=8
            if (i == 0) {
                dailyMap.put(0, DEFAULT_VALUE);
                m += 1;
            } else {
                dailyMap.put((i * 3), DEFAULT_VALUE);
                m += 1;
            }
        }
        return dailyMap;
    }

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

    private float[] convertTreeMapToFloats(TreeMap<Integer, Integer> numberOfTexts) {
        ArrayList<Float> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : numberOfTexts.entrySet()) {
            Float value = entry.getValue().floatValue();
            list.add(value);
        }
        return convertFloats(list);
    }

    public String maxTimeReceivedText() {
        ArrayList<String> timeContactedList = new ArrayList<>();
        TreeMap<Integer, Integer> timeReceived = getHourlyReceived(smsList);
        Map.Entry<Integer, Integer> maxEntry = getMaxEntry(timeReceived);
        return String.valueOf(maxEntry.getKey());
    }

    public String maxTimeSentText() {
        ArrayList<String> timeContactedList = new ArrayList<>();
        TreeMap<Integer, Integer> timeReceived = getHourlyReceived(smsList);
        Map.Entry<Integer, Integer> maxEntry = getMaxEntry(timeReceived);
        return String.valueOf(maxEntry.getKey());
    }

    @Nullable
    private Map.Entry<Integer, Integer> getMaxEntry(TreeMap<Integer, Integer> timeReceived) {
        Map.Entry<Integer, Integer> maxEntry = null;

        for (Map.Entry<Integer, Integer> entry : timeReceived.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }
}
