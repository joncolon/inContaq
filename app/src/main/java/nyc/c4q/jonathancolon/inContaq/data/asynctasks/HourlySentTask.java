package nyc.c4q.jonathancolon.inContaq.data.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.TreeMap;

import io.realm.Realm;
import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.contactlist.fragments.ContactStatsFragment;
import nyc.c4q.jonathancolon.inContaq.data.HourlyTreeMap;
import nyc.c4q.jonathancolon.inContaq.realm.RealmHelper;
import nyc.c4q.jonathancolon.inContaq.sms.model.Sms;


public class HourlySentTask extends AsyncTask<String, Void,TreeMap<Integer, Integer>> {

    private OnStatsLoaded listener;
    private ContactStatsFragment statsFrag;
    private TreeMap<Integer, Integer> hourlySentTreeMap;
    private Realm realm;

    public HourlySentTask(ContactStatsFragment statsFrag, OnStatsLoaded listener) {
        this.statsFrag = statsFrag;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        Log.i("DailySentTask", "Getting Daily Total Sent..." );
    }

    @Override
    protected TreeMap<Integer, Integer> doInBackground(String... params) {
        realm = RealmHelper.getInstance();
        RealmResults<Sms> smsList = RealmHelper.getByMobileNumber(realm, params[0]);
        HourlyTreeMap hourlyTreeMap = new HourlyTreeMap();
        hourlySentTreeMap = hourlyTreeMap.setUpDailyTextMap();
        return getSmsStats(smsList);
    }

    @Override
    protected void onPostExecute(TreeMap<Integer, Integer> ret) {
        super.onPostExecute(ret);
        statsFrag.setHourlySentTreeMap(ret);
        statsFrag.hourlySentLoaded = true;
        listener.onStatsLoaded();
    }

    private TreeMap<Integer, Integer> getSmsStats(RealmResults<Sms> list) {
        ArrayList<String> receivedSms = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            String p = list.get(i).getType();
            if (p.equals("2")) {
                receivedSms.add(list.get(i).getTime());
            }
        }
        hourlySentTreeMap = getHourlyTexts(receivedSms);
        return hourlySentTreeMap;
    }

    // 12am=0, 3am=1, 6am=2 ,9am=3,12pm=4, (15)3pm=5, (18)6pm=6, (21)9pm=7, (24)12am=8

    private TreeMap<Integer, Integer> getHourlyTexts(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            long lg = Long.parseLong(list.get(i));
            DateTime mDateTime = new DateTime(lg);
            int hourOfDay = mDateTime.getHourOfDay();

            if (hourOfDay == 0) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(0);
                } else {
                    putIntoDailyTexts(0);
                }
            } else if (hourOfDay < 3) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(3);
                } else {
                    putIntoDailyTexts(3);
                }
            } else if (hourOfDay < 6) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(6);
                } else {
                    putIntoDailyTexts(6);
                }
            } else if (hourOfDay < 9) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(9);
                } else {
                    putIntoDailyTexts(9);
                }
            } else if (hourOfDay < 12) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(12);
                } else {
                    putIntoDailyTexts(12);
                }
            } else if (hourOfDay < 15) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(15);
                } else {
                    putIntoDailyTexts(15);
                }
            } else if (hourOfDay < 18) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(18);
                } else {
                    putIntoDailyTexts(18);
                }
            } else if (hourOfDay < 21) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(21);
                } else {
                    putIntoDailyTexts(21);
                }
            } else if (hourOfDay < 24) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(24);
                } else {
                    putIntoDailyTexts(24);
                }
            }
        }
        RealmHelper.closeRealm(realm);
        return hourlySentTreeMap;
    }

    private void findGetDailyTexts(int hourOfDay) {
        hourlySentTreeMap.put(hourOfDay, hourlySentTreeMap.get(hourOfDay) + 1);
        hourlySentTreeMap.entrySet();
    }

    private void putIntoDailyTexts(int hourOfDay) {
        hourlySentTreeMap.put(hourOfDay, 1);
        hourlySentTreeMap.entrySet();
    }
}
