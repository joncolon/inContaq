package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.util.PrepareHourlyTreeMap;

/**
 * Created by jonathancolon on 5/14/17.
 */

public class GetHourlyReceived {
    private TreeMap<Integer, Integer> hourlyReceivedTreeMap;
    private ArrayList<Sms> smsList;

    public GetHourlyReceived(ArrayList<Sms> smsList) {
        this.smsList = smsList;
    }

    public TreeMap<Integer, Integer> getHourlyReceived() {
        PrepareHourlyTreeMap treeMapPrep = new PrepareHourlyTreeMap();
        hourlyReceivedTreeMap = treeMapPrep.setUpHourlyTreeMap();
        ArrayList<String> receivedSms = new ArrayList<>();

        for (int i = 0; i < smsList.size(); i++) {
            String p = smsList.get(i).getType();
            if (p.equals("1")) {
                receivedSms.add(smsList.get(i).getTime());
            }
        }
        hourlyReceivedTreeMap = mapHourlySms(receivedSms);
        return hourlyReceivedTreeMap;
    }

    private TreeMap<Integer, Integer> mapHourlySms(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            long lg = Long.parseLong(list.get(i));
            DateTime mDateTime = new DateTime(lg);
            int hourOfDay = mDateTime.getHourOfDay();

            if (hourOfDay == 0) {
                if (hourlyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(0);
                } else {
                    intoGetDailyTexts(0);
                }
            } else if (hourOfDay < 3) {
                if (hourlyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(3);
                } else {
                    intoGetDailyTexts(3);
                }
            } else if (hourOfDay < 6) {
                if (hourlyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(6);
                } else {
                    intoGetDailyTexts(6);
                }
            } else if (hourOfDay < 9) {
                if (hourlyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(9);
                } else {
                    intoGetDailyTexts(9);
                }
            } else if (hourOfDay < 12) {
                if (hourlyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(12);
                } else {
                    intoGetDailyTexts(12);
                }
            } else if (hourOfDay < 15) {
                if (hourlyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(15);
                } else {
                    intoGetDailyTexts(15);
                }
            } else if (hourOfDay < 18) {
                if (hourlyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(18);
                } else {
                    intoGetDailyTexts(18);
                }
            } else if (hourOfDay < 21) {
                if (hourlyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(21);
                } else {
                    intoGetDailyTexts(21);
                }
            } else if (hourOfDay < 24) {
                if (hourlyReceivedTreeMap.containsKey(hourOfDay)) {
                    findGetDailyTexts(24);
                } else {
                    intoGetDailyTexts(24);
                }
            }

        }
        return hourlyReceivedTreeMap;

    }

    private void findGetDailyTexts(int hourOfDay) {
        hourlyReceivedTreeMap.put(hourOfDay, hourlyReceivedTreeMap.get(hourOfDay) + 1);
        hourlyReceivedTreeMap.entrySet();
    }

    private void intoGetDailyTexts(int hourOfDay) {
        hourlyReceivedTreeMap.put(hourOfDay, 1);
        hourlyReceivedTreeMap.entrySet();
    }
}
