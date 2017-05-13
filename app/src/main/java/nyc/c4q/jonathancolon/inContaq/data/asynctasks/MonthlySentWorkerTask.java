package nyc.c4q.jonathancolon.inContaq.data.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.TreeMap;

import io.realm.Realm;
import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.params.MonthlyTaskParams;
import nyc.c4q.jonathancolon.inContaq.realm.RealmHelper;
import nyc.c4q.jonathancolon.inContaq.sms.model.Sms;


public class MonthlySentWorkerTask extends AsyncTask<MonthlyTaskParams, Void,
        TreeMap<Integer, Integer>> {

    private static TreeMap<Integer, Integer> monthlyTexts;
    private Realm realm;

    public MonthlySentWorkerTask() {
    }

    @Override
    protected void onPreExecute() {
        Log.i("MonthlySentTask", "Getting Monthly Total Sent...");
    }

    @Override
    protected TreeMap<Integer, Integer> doInBackground(MonthlyTaskParams... params) {
        realm = RealmHelper.getInstance();
        RealmResults<Sms> smsList = RealmHelper.getByMobileNumber(realm, params[0].phoneNumber);
        monthlyTexts = params[0].monthlyTexts;
        return getSmsStats(smsList);
    }

    private TreeMap<Integer, Integer> getSmsStats(RealmResults<Sms> list) {
        ArrayList<String> sentSms = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType().equals("2")) {
                sentSms.add(list.get(i).getTime());
            }
        }
        monthlyTexts = getMonthlyTexts(sentSms);
        return monthlyTexts;
    }

    private TreeMap<Integer, Integer> getMonthlyTexts(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            long lg = Long.parseLong(list.get(i));
            DateTime juSmsYear = new DateTime(lg);
            long currentTime = System.currentTimeMillis();
            DateTime juDateYear = new DateTime(currentTime);
            int monthSent = juSmsYear.getMonthOfYear();

            if (monthlyTexts.containsKey(monthSent)) {
                monthlyTexts.put(monthSent, monthlyTexts.get(monthSent) + 1);
                monthlyTexts.entrySet();
            }
        }
        RealmHelper.closeRealm(realm);
        return monthlyTexts;
    }

    @Override
    protected void onPostExecute(TreeMap<Integer, Integer> ret) {
        super.onPostExecute(ret);
    }
}