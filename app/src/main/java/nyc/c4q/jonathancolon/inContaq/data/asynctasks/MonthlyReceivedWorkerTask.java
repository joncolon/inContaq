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


public class MonthlyReceivedWorkerTask extends AsyncTask<MonthlyTaskParams, Void,
        TreeMap<Integer, Integer>> {

    private TreeMap<Integer, Integer> monthlyTexts;
    private Realm realm;

    public MonthlyReceivedWorkerTask() {
    }

    @Override
    protected void onPreExecute() {
        Log.i("MonthlyReceivedTask", "Getting Monthly Total Received...");
    }

    @Override
    protected TreeMap<Integer, Integer> doInBackground(MonthlyTaskParams... params) {
        realm = RealmHelper.getInstance();
        RealmResults<Sms> smsList = RealmHelper.getByMobileNumber(realm, params[0].phoneNumber);
        monthlyTexts = params[0].monthlyTexts;
        return getSmsStats(smsList);
    }

    @Override
    protected void onPostExecute(TreeMap<Integer, Integer> ret) {
        super.onPostExecute(ret);
    }

    private TreeMap<Integer, Integer> getSmsStats(RealmResults<Sms> list){
        ArrayList<String> receivedSms = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType().equals("1")) {
                receivedSms.add(list.get(i).getTime());
            }
        }
            monthlyTexts = getMonthlyTexts(receivedSms);
        return monthlyTexts;
    }

    private TreeMap<Integer, Integer> getMonthlyTexts(ArrayList<String> list){
        for (int i = 0; i < list.size(); i++) {
            long lg = Long.parseLong(list.get(i));
            DateTime juDate = new DateTime(lg);
            int month = juDate.getMonthOfYear();

                if (monthlyTexts.containsKey(month)){
                    monthlyTexts.put(month, monthlyTexts.get(month) +1);
                    monthlyTexts.entrySet();
                }
            }
        RealmHelper.closeRealm(realm);
        return monthlyTexts;
    }
}