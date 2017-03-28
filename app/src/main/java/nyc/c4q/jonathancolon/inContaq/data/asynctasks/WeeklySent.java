package nyc.c4q.jonathancolon.inContaq.data.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.data.asynctasks.params.WeeklyTaskParams;
import nyc.c4q.jonathancolon.inContaq.sms.model.Sms;


public class WeeklySent extends AsyncTask<WeeklyTaskParams, Void, TreeMap<Integer, Integer>> {

    private TreeMap<Integer, Integer> weeklyTexts;

    public WeeklySent() {
    }

    @Override
    protected void onPreExecute() {
        Log.i("WeeklySentTask", "Getting Weekly Total Sent...");
    }

    @Override
    protected TreeMap<Integer, Integer> doInBackground(WeeklyTaskParams... params) {

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
        weeklyTexts = getWeeklyTexts(sentSms);
        return weeklyTexts;
    }


    private TreeMap<Integer, Integer> getWeeklyTexts(ArrayList<String> list) {

        for (int i = 0; i < list.size(); i++) {
            boolean fallsInWeek = false;
            Calendar smsCalendar = Calendar.getInstance();
            Calendar todayCalendar = Calendar.getInstance();
            Date smsDateeee = new Date(Long.parseLong(list.get(i)));
            Date todaysDate = new Date(System.currentTimeMillis());
            smsCalendar.setTime(smsDateeee);
            todayCalendar.setTime(todaysDate);

            int smsYear = smsCalendar.get(Calendar.YEAR);
            int smsMonth = smsCalendar.get(Calendar.MONTH);
            int smsDayIs = smsCalendar.get(Calendar.DAY_OF_MONTH);
            int smsDayOfWeek = smsCalendar.get(Calendar.DAY_OF_WEEK);
            int todayYear = todayCalendar.get(Calendar.YEAR);
            int todayMonth = todayCalendar.get(Calendar.MONTH);
            int todayDayIs = todayCalendar.get(Calendar.DAY_OF_MONTH);
            int todayDayOfWeek = todayCalendar.get(Calendar.DAY_OF_WEEK);

            int diffBetDays = (todayDayIs - smsDayIs);

//            fallsInWeek = isWithinTheWeek(todayDayOfWeek, diffBetDays,
//                    smsYear, todayYear,
//                    smsMonth, todayMonth);

                if (weeklyTexts.containsKey(smsDayOfWeek)) {
                    weeklyTexts.put(smsDayOfWeek, weeklyTexts.get(smsDayOfWeek) + 1);
                    weeklyTexts.entrySet();
            }
        }
        return weeklyTexts;
    }

//    private boolean isWithinTheWeek(int todayDayinWeek, int diffDays,
//                                    int smsYear, int todayYear,
//                                    int smsMonth, int todayMonth) {
//        return diffDays < todayDayinWeek && smsYear == todayYear && smsMonth == todayMonth;
//    }
}
