package nyc.c4q.jonathancolon.inContaq.utlities.dailysms;

import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.utlities.sms.Sms;

/**
 * Created by Hyun on 3/11/17.
 */

public class DailySentWorkerTask extends AsyncTask<DailyTaskParams, Void,TreeMap<Integer, Integer>> {

    private TreeMap<Integer, Integer> dailyTexts;
    private ArrayList<Sms> listSms;

    public DailySentWorkerTask() {
    }

    @Override
    protected void onPreExecute() {
        Log.i("DailySentTask", "Getting Daily Total Sent...");
    }

    @Override
    protected TreeMap<Integer, Integer> doInBackground(DailyTaskParams... params) {
        listSms = params[0].getdailyListSms();
        dailyTexts = params[0].getDailyTexts();
        return getSmsStats(listSms);
    }

    @Override
    protected void onPostExecute(TreeMap<Integer, Integer> ret) {
        super.onPostExecute(ret);
    }

    private TreeMap<Integer, Integer> getSmsStats(ArrayList<Sms> listOfSms) {
        ArrayList<String> sentSms = new ArrayList<>();

        for (int i = 0; i < listOfSms.size(); i++) {
            if (listOfSms.get(i).getType().equals("2")) {
                sentSms.add(listOfSms.get(i).getTime());
            }
        }
        dailyTexts = getDailyTexts(sentSms);
        return dailyTexts;
    }

    private TreeMap<Integer, Integer> getDailyTexts(ArrayList<String> listOfSentSms) {

        for (int i = 0; i < listOfSentSms.size(); i++) {

            long parsedLongText = Long.parseLong(listOfSentSms.get(i));

            DateTime currSmsYear = new DateTime(parsedLongText);
            int weeklyYearSent = currSmsYear.getWeekyear();
            long currentTime = System.currentTimeMillis();

            DateTime currTimeYear = new DateTime(currentTime);
            int currentWeekYear = currTimeYear.getWeekyear();
            int weeklySent = currSmsYear.getWeekyear();


            if (dailyTexts.containsKey(weeklySent)) {
                dailyTexts.put(weeklySent, dailyTexts.get(weeklySent) + 1);
                dailyTexts.entrySet();
            }
        }
        return dailyTexts;
    }
}
