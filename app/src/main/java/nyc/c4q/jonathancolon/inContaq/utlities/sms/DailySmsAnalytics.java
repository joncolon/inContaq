package nyc.c4q.jonathancolon.inContaq.utlities.sms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by jonathancolon on 3/17/17.
 */

public class DailySmsAnalytics {

    private final int DEFAULT_VALUE = 0;
    private ArrayList<Sms> smsList;

    public DailySmsAnalytics(ArrayList<Sms> smsList) {
        this.smsList = smsList;
    }

    public float[] getSentValues(){
        return convertTreeMapToFloats(getDailySent(smsList));
    }
    public float[] getReceivedValue(){
        return convertTreeMapToFloats(getDailyReceived(smsList));
    }

    public float[] convertTreeMapToFloats(TreeMap<Integer, Integer> numberOfTexts) {
        ArrayList<Float> list = new ArrayList<Float>();
        for (Map.Entry<Integer, Integer> entry : numberOfTexts.entrySet()) {
            Float value = entry.getValue().floatValue();
            list.add(value);
        }
        return convertFloats(list);
    }

    private float[] convertFloats(List<Float> floats) {
        float[] ret = new float[floats.size()];
        Iterator<Float> iterator = floats.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    private TreeMap<Integer, Integer> setUpDailyTextMap() {
        TreeMap<Integer, Integer> dailyMap = new TreeMap<>();
        int m = 0;
        for (int i = 0; i < 9; i++) { // 12am=0, 3am=1, 6am=2 ,9am=3,12pm=4, (15)3pm=5, (18)6pm=6, (21)9pm=7, (24)12am=8
            if (i == 0) {
                dailyMap.put(0,DEFAULT_VALUE);
                m += 1;
            } else {
                dailyMap.put((i * 3),DEFAULT_VALUE);
                m += 1;
            }
        }
        return dailyMap;
    }

    public TreeMap<Integer, Integer> getDailyReceived(ArrayList<Sms> texts) {
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

    public TreeMap<Integer, Integer> getDailySent(ArrayList<Sms> texts) {
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
}
