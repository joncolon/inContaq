package nyc.c4q.jonathancolon.inContaq.utlities.sms.weekly;

import android.os.AsyncTask;
import android.util.Log;
import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.TreeMap;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.Sms;

public class WeeklySent extends AsyncTask<WeeklyTask, Void, TreeMap<Integer, Integer>> {

    private TreeMap<Integer, Integer> weeklyTexts;

    public WeeklySent() {
    }

    @Override
    protected void onPreExecute() {
        Log.i("WeeklySentTask", "Getting Weekly Total Sent...");
    }

    @Override
    protected TreeMap<Integer, Integer> doInBackground(WeeklyTask... params) {

        ArrayList<Sms> listSms = params[0].listSms;
        weeklyTexts = params[0].weeklyTexts;
        return getSmsStats(listSms);

    }

    @Override
    protected void onPostExecute(TreeMap<Integer, Integer> ret) {
        super.onPostExecute(ret);
    }

    private TreeMap<Integer, Integer> getSmsStats(ArrayList<Sms> list) {

        ArrayList<String> sentSms = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getType().equals("2")) {
                sentSms.add(list.get(i).getTime());
            }
        }
        weeklyTexts = getDailyTexts(sentSms);
        return weeklyTexts;
    }

    private TreeMap<Integer, Integer> getDailyTexts(ArrayList<String> list) {

        for (int i = 0; i < list.size(); i++) {

            long lg = Long.parseLong(list.get(i));
            DateTime juSmsYear = new DateTime(lg);
            int yearSent = juSmsYear.getYear();
            long currentTime = System.currentTimeMillis();
            DateTime juDateYear = new DateTime(currentTime);
            int currentYear = juDateYear.getYear();
            int monthSent = juSmsYear.getMonthOfYear();

            if (weeklyTexts.containsKey(monthSent)) {

                weeklyTexts.put(monthSent, weeklyTexts.get(monthSent) + 1);
                weeklyTexts.entrySet();
            }
        }
        return weeklyTexts;
    }
}
