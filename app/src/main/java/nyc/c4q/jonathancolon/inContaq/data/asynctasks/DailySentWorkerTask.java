package nyc.c4q.jonathancolon.inContaq.data.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.data.asynctasks.params.DailyTaskParams;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;

import static nyc.c4q.jonathancolon.inContaq.data.asynctasks.params.DailyTaskParams.getdailySmsList;

public class DailySentWorkerTask extends AsyncTask<DailyTaskParams, Void,TreeMap<Integer, Integer>> {

    private TreeMap<Integer, Integer> dailySentTexts;

    public DailySentWorkerTask() {
    }

    @Override
    protected void onPreExecute() {
        Log.i("DailyReceivedTask", "Getting Daily Total Received...");
    }

    @Override
    protected TreeMap<Integer, Integer> doInBackground(DailyTaskParams... params) {
        ArrayList<Sms> listSms = getdailySmsList();
        dailySentTexts = DailyTaskParams.getDailyTexts();
        return getSmsStats(listSms);
    }

    @Override
    protected void onPostExecute(TreeMap<Integer, Integer> ret) {
        super.onPostExecute(ret);
    }

    private TreeMap<Integer, Integer> getSmsStats(ArrayList<Sms> list) {
        ArrayList<String> receivedSms = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            String p = list.get(i).getType();
            if (p.equals("2")) {
                receivedSms.add(list.get(i).getTime());
            }
        }
        dailySentTexts = getDailyTexts(receivedSms);
        return dailySentTexts;
    }

    // 12am=0, 3am=1, 6am=2 ,9am=3,12pm=4, (15)3pm=5, (18)6pm=6, (21)9pm=7, (24)12am=8

    private TreeMap<Integer, Integer> getDailyTexts(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            long lg = Long.parseLong(list.get(i));
            DateTime mDateTime = new DateTime(lg);
            int hourOfDay = mDateTime.getHourOfDay();


            if (hourOfDay == 0) {
                if (dailySentTexts.containsKey(hourOfDay)) {
                    findGetDailyTexts(0);
                } else {
                    intoGetDailyTexts(0);
                }
            } else if (hourOfDay < 3) {
                if (dailySentTexts.containsKey(hourOfDay)) {
                    findGetDailyTexts(3);
                } else {
                    intoGetDailyTexts(3);
                }
            }  else if (hourOfDay < 6) {
                if (dailySentTexts.containsKey(hourOfDay)) {
                    findGetDailyTexts(6);
                } else {
                    intoGetDailyTexts(6);
                }
            } else if (hourOfDay < 9) {
                if (dailySentTexts.containsKey(hourOfDay)) {
                    findGetDailyTexts(9);
                } else {
                    intoGetDailyTexts(9);
                }
            } else if (hourOfDay < 12) {
                if (dailySentTexts.containsKey(hourOfDay)) {
                    findGetDailyTexts(12);
                } else {
                    intoGetDailyTexts(12);
                }
            } else if (hourOfDay < 15) {
                if (dailySentTexts.containsKey(hourOfDay)) {
                    findGetDailyTexts(15);
                } else {
                    intoGetDailyTexts(15);
                }
            } else if (hourOfDay < 18) {
                if (dailySentTexts.containsKey(hourOfDay)) {
                    findGetDailyTexts(18);
                } else {
                    intoGetDailyTexts(18);
                }
            } else if (hourOfDay < 21) {
                if (dailySentTexts.containsKey(hourOfDay)) {
                    findGetDailyTexts(21);
                } else {
                    intoGetDailyTexts(21);
                }
            } else if (hourOfDay < 24) {
                if (dailySentTexts.containsKey(hourOfDay)) {
                    findGetDailyTexts(24);
                } else {
                    intoGetDailyTexts(24);
                }
            }

        }
        return dailySentTexts;
    }

    private void findGetDailyTexts(int hourOfDay) {
        dailySentTexts.put(hourOfDay, dailySentTexts.get(hourOfDay) + 1);
        dailySentTexts.entrySet();
    }

    private void intoGetDailyTexts(int hourOfDay) {
        dailySentTexts.put(hourOfDay, 1);
        dailySentTexts.entrySet();
    }
}
