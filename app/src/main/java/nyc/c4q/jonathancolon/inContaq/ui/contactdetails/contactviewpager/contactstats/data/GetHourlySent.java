package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.util.PrepareHourlyTreeMap;


public class GetHourlySent {
    private TreeMap<Integer, Integer> hourlySentTreeMap;
    private final ArrayList<Sms> smsList;

    public GetHourlySent(ArrayList<Sms> smsList) {
        this.smsList = smsList;
    }

    public TreeMap<Integer, Integer> getHourlySent() {
        PrepareHourlyTreeMap prepareHourlyTreeMap = new PrepareHourlyTreeMap();
        hourlySentTreeMap = prepareHourlyTreeMap.setUpHourlyTreeMap();
        ArrayList<String> sentSms = new ArrayList<>();

        for (int i = 0; i < smsList.size(); i++) {
            String p = smsList.get(i).getType();
            if (p.equals("2")) {
                sentSms.add(smsList.get(i).getTime());
            }
        }
        hourlySentTreeMap = mapHourlySms(sentSms);
        return hourlySentTreeMap;
    }

    // 12am=0, 3am=1, 6am=2 ,9am=3,12pm=4, (15)3pm=5, (18)6pm=6, (21)9pm=7, (24)12am=8

    private TreeMap<Integer, Integer> mapHourlySms(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            long lg = Long.parseLong(list.get(i));
            DateTime mDateTime = new DateTime(lg);
            int hourOfDay = mDateTime.getHourOfDay();


            if (hourOfDay == 0) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(0);
                } else {
                    intoGetDailyTexts(0);
                }
            } else if (hourOfDay < 3) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(3);
                } else {
                    intoGetDailyTexts(3);
                }
            } else if (hourOfDay < 6) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(6);
                } else {
                    intoGetDailyTexts(6);
                }
            } else if (hourOfDay < 9) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(9);
                } else {
                    intoGetDailyTexts(9);
                }
            } else if (hourOfDay < 12) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(12);
                } else {
                    intoGetDailyTexts(12);
                }
            } else if (hourOfDay < 15) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(15);
                } else {
                    intoGetDailyTexts(15);
                }
            } else if (hourOfDay < 18) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(18);
                } else {
                    intoGetDailyTexts(18);
                }
            } else if (hourOfDay < 21) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(21);
                } else {
                    intoGetDailyTexts(21);
                }
            } else if (hourOfDay < 24) {
                if (hourlySentTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(24);
                } else {
                    intoGetDailyTexts(24);
                }
            }

        }
        return hourlySentTreeMap;

    }

    private void findGetDailyTexts(int hourOfDay) {
        hourlySentTreeMap.put(hourOfDay, hourlySentTreeMap.get(hourOfDay) + 1);
        hourlySentTreeMap.entrySet();
    }

    private void intoGetDailyTexts(int hourOfDay) {
        hourlySentTreeMap.put(hourOfDay, 1);
        hourlySentTreeMap.entrySet();
    }
}
