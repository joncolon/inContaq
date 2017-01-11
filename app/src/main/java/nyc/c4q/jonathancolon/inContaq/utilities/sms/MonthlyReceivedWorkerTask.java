package nyc.c4q.jonathancolon.inContaq.utilities.sms;

import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by jonathancolon on 1/5/17.
 */

public class MonthlyReceivedWorkerTask extends AsyncTask<MonthlyTaskParams, Void, TreeMap<Integer, Integer>> {
    TreeMap<Integer, Integer> monthlyTexts;
    ArrayList<Sms> lstSms;

    public MonthlyReceivedWorkerTask() {
    }

    @Override
    protected void onPreExecute() {
        Log.i("MonthlyReceivedTask", "Getting Monthly Total Received...");
    }

    @Override
    protected TreeMap<Integer, Integer> doInBackground(MonthlyTaskParams... params) {
        lstSms = params[0].lstSms;
        monthlyTexts = params[0].monthlyTexts;
        return getSmsStats(lstSms);
    }

    @Override
    protected void onPostExecute(TreeMap<Integer, Integer> ret) {
        super.onPostExecute(ret);
    }

    private TreeMap<Integer, Integer> getSmsStats(ArrayList<Sms> list){
        ArrayList<String> receievedSms = new ArrayList<>();
        ArrayList<Sms> sentSms = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType().equals("1")) {
                receievedSms.add(list.get(i).getTime());
            }
        }
            monthlyTexts = getMonthlyTexts(receievedSms);
        return monthlyTexts;
    }

    private TreeMap<Integer, Integer> getMonthlyTexts(ArrayList<String> list){
        DateTime juDate;
            for (int i = 0; i < list.size(); i++) {
                long lg;
                lg = Long.parseLong(list.get(i));
                juDate = new DateTime(lg);
                int month = juDate.getMonthOfYear();

                if (monthlyTexts.containsKey(month)){
                    monthlyTexts.put(month, monthlyTexts.get(month) +1);
                    monthlyTexts.entrySet();
                }
            }
        return monthlyTexts;
    }
}