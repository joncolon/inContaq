package nyc.c4q.jonathancolon.inContaq.utlities.sms.weekly;

import android.os.AsyncTask;
import android.util.Log;
import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.TreeMap;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.Sms;

public class WeeklyReceived extends AsyncTask<WeeklyTask, Void, TreeMap<Integer, Integer>> {

    private TreeMap<Integer, Integer> weeklyTexts;

    public WeeklyReceived() {
    }

    @Override
    protected void onPreExecute() {

        Log.i("WeeklyReceivedTask", "Getting Weekly Total Received...");
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

        ArrayList<String> receivedSms = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType().equals("1")) {
                receivedSms.add(list.get(i).getTime());
            }
        }
        weeklyTexts = getMonthlyTexts(receivedSms);
        return weeklyTexts;
    }

    private TreeMap<Integer, Integer> getMonthlyTexts(ArrayList<String> list) {

        for (int i = 0; i < list.size(); i++) {

            long lg = Long.parseLong(list.get(i));
            DateTime juDate = new DateTime(lg);
            int month = juDate.getMonthOfYear();

            if (weeklyTexts.containsKey(month)) {

                weeklyTexts.put(month, weeklyTexts.get(month) + 1);
                weeklyTexts.entrySet();
            }
        }
        return weeklyTexts;
    }
}
