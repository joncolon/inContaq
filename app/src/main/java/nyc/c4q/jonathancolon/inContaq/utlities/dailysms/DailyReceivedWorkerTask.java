package nyc.c4q.jonathancolon.inContaq.utlities.dailysms;

import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.utlities.sms.Sms;

/**
 * Created by Hyun on 3/11/17.
 */

public class DailyReceivedWorkerTask extends AsyncTask<DailyTaskParams, Void, TreeMap<Integer, Integer>> {

    private TreeMap<Integer, Integer> dailyReceivedText;
    private ArrayList<Sms> listSms;

    public DailyReceivedWorkerTask() {
    }

    @Override
    protected void onPreExecute() {
        Log.i("DailyReceivedTask", "Getting Daily Total Received...");
    }

    @Override
    protected TreeMap<Integer, Integer> doInBackground(DailyTaskParams... params) {
        listSms = params[0].getdailyListSms();
        dailyReceivedText = params[0].getDailyTexts();
        return getSmsStats(listSms);
    }

    @Override
    protected void onPostExecute(TreeMap<Integer, Integer> ret) {
        super.onPostExecute(ret);
    }

    private TreeMap<Integer, Integer> getSmsStats(ArrayList<Sms> list) {
        ArrayList<String> receivedSms = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTime().equals(Calendar.getInstance())) {
                receivedSms.add(list.get(i).getTime());
            }
        }
        dailyReceivedText = getDailyTexts(receivedSms);
        return dailyReceivedText;
    }

    private TreeMap<Integer, Integer> getDailyTexts(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            long lg = Long.parseLong(list.get(i));
            DateTime mDateTime = new DateTime(lg);
            int hourOfDay = mDateTime.getHourOfDay();

            if (dailyReceivedText.containsKey(hourOfDay)) {
                dailyReceivedText.put(hourOfDay, dailyReceivedText.get(hourOfDay) + 1);
                dailyReceivedText.entrySet();
            }
        }
        return dailyReceivedText;
    }
}