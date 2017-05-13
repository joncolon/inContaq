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


public class HourlyReceivedTask extends AsyncTask<String, Void, TreeMap<Integer, Integer>> {

    private OnStatsLoaded listener;
    private ContactStatsFragment statsFrag;
    private TreeMap<Integer, Integer> dailyReceivedTreeMap;
    private Realm realm;

    public HourlyReceivedTask(ContactStatsFragment statsFrag, OnStatsLoaded listener) {
        this.listener = listener;
        this.statsFrag = statsFrag;
    }

    @Override
    protected void onPreExecute() {
        Log.i("DailyReceivedTask", "Getting Daily Total Received...");
    }

    @Override
    protected TreeMap<Integer, Integer> doInBackground(String... params) {
        realm = RealmHelper.getInstance();
        RealmResults<Sms> smsList = RealmHelper.getByMobileNumber(realm, params[0]);
        HourlyTreeMap hourlyTreeMap = new HourlyTreeMap();
        dailyReceivedTreeMap = hourlyTreeMap.setUpDailyTextMap();
        return getSmsStats(smsList);
    }

    @Override
    protected void onPostExecute(TreeMap<Integer, Integer> ret) {
        super.onPostExecute(ret);
        statsFrag.setHourlyReceivedTreeMap(ret);
        statsFrag.hourlyReceivedLoaded = true;
        listener.onStatsLoaded();
    }

    private TreeMap<Integer, Integer> getSmsStats(RealmResults<Sms> list) {
        ArrayList<String> receivedSms = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            String p = list.get(i).getType();
            if (p.equals("1")) {
                receivedSms.add(list.get(i).getTime());
            }
        }
        dailyReceivedTreeMap = getDailyTexts(receivedSms);
        return dailyReceivedTreeMap;
    }

    // 12am=0, 3am=1, 6am=2 ,9am=3,12pm=4, (15)3pm=5, (18)6pm=6, (21)9pm=7, (24)12am=8

    private TreeMap<Integer, Integer> getDailyTexts(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            long lg = Long.parseLong(list.get(i));
            DateTime mDateTime = new DateTime(lg);
            int hourOfDay = mDateTime.getHourOfDay();


            if (hourOfDay == 0) {
                if (dailyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(0);
                } else {
                    intoGetDailyTexts(0);
                }
            } else if (hourOfDay < 3) {
                if (dailyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(3);
                } else {
                    intoGetDailyTexts(3);
                }
            }  else if (hourOfDay < 6) {
                if (dailyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(6);
                } else {
                    intoGetDailyTexts(6);
                }
            } else if (hourOfDay < 9) {
                if (dailyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(9);
                } else {
                    intoGetDailyTexts(9);
                }
            } else if (hourOfDay < 12) {
                if (dailyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(12);
                } else {
                    intoGetDailyTexts(12);
                }
            } else if (hourOfDay < 15) {
                if (dailyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(15);
                } else {
                    intoGetDailyTexts(15);
                }
            } else if (hourOfDay < 18) {
                if (dailyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(18);
                } else {
                    intoGetDailyTexts(18);
                }
            } else if (hourOfDay < 21) {
                if (dailyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(21);
                } else {
                    intoGetDailyTexts(21);
                }
            } else if (hourOfDay < 24) {
                if (dailyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(24);
                } else {
                    intoGetDailyTexts(24);
                }
            }

        }
        RealmHelper.closeRealm(realm);
        return dailyReceivedTreeMap;

    }

    private void findGetDailyTexts(int hourOfDay) {
        dailyReceivedTreeMap.put(hourOfDay, dailyReceivedTreeMap.get(hourOfDay) + 1);
        dailyReceivedTreeMap.entrySet();
    }

    private void intoGetDailyTexts(int hourOfDay) {
        dailyReceivedTreeMap.put(hourOfDay, 1);
        dailyReceivedTreeMap.entrySet();
    }
}