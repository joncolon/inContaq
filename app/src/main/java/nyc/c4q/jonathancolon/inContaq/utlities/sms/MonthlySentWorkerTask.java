package nyc.c4q.jonathancolon.inContaq.utlities.sms;

import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.TreeMap;


public class MonthlySentWorkerTask extends AsyncTask<MonthlyTaskParams, Void,
        TreeMap<Integer, Integer>> {

    private TreeMap<Integer, Integer> monthlyTexts;

    public MonthlySentWorkerTask() {
    }

    @Override
    protected void onPreExecute() {
        Log.i("MonthlySentTask", "Getting Monthly Total Sent...");
    }

    @Override
    protected TreeMap<Integer, Integer> doInBackground(MonthlyTaskParams... params) {
        ArrayList<Sms> listSms = params[0].listSms;
        monthlyTexts = params[0].monthlyTexts;
        return getSmsStats(listSms);
    }

    @Override
    protected void onPostExecute(TreeMap<Integer, Integer> ret) {
        super.onPostExecute(ret);
    }

    private TreeMap<Integer, Integer> getSmsStats(ArrayList<Sms> list){
        ArrayList<String> sentSms = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType().equals("2")) {
                sentSms.add(list.get(i).getTime());
            }
        }
        monthlyTexts = getMonthlyTexts(sentSms);
        return monthlyTexts;
    }

    private TreeMap<Integer, Integer> getMonthlyTexts(ArrayList<String> list){
        for (int i = 0; i < list.size(); i++) {
            long lg = Long.parseLong(list.get(i));
            DateTime juSmsYear = new DateTime(lg);
            int yearSent = juSmsYear.getYear();
            long currentTime = System.currentTimeMillis();
            DateTime juDateYear = new DateTime(currentTime);
            int currentYear = juDateYear.getYear();
            int monthSent = juSmsYear.getMonthOfYear();


            if (monthlyTexts.containsKey(monthSent)){
                monthlyTexts.put(monthSent, monthlyTexts.get(monthSent) +1);
                monthlyTexts.entrySet();
            }
        }
        return monthlyTexts;
    }
}