package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.model.Sms;

/**
 * Created by jonathancolon on 5/14/17.
 */

public class GetHourlySent {
    TreeMap<Integer, Integer> hourlySentTreeMap;

    private static final int TWELVE_AM = 0;
    private static final int THREE_AM = 3;
    private static final int SIX_AM = 6;
    private static final int NINE_AM = 9;
    private static final int TWELVE_PM = 12;
    private static final int THREE_PM = 15;
    private static final int SIX_PM = 18;
    private static final int NINE_PM = 21;
    private static final int MIDNIGHT = 24;
    private static final int ELEVEN_PM = 23;

    public GetHourlySent() {
    }

    public TreeMap<Integer, Integer> getHourlySent(ArrayList<Sms> smsList) {
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

            if (hourOfDay == TWELVE_AM || hourOfDay == MIDNIGHT) {
                hourlySentTreeMap.put(0, hourlySentTreeMap.get(0) + 1);
                hourlySentTreeMap.put(8, hourlySentTreeMap.get(8) + 1);
            }
            if (hourOfDay <= THREE_AM && hourOfDay > TWELVE_AM) {
                hourlySentTreeMap.put(1, hourlySentTreeMap.get(1) + 1);
            }
            if (hourOfDay <= SIX_AM && hourOfDay > THREE_AM) {
                hourlySentTreeMap.put(2, hourlySentTreeMap.get(2) + 1);
            }
            if (hourOfDay <= NINE_AM && hourOfDay > SIX_AM) {
                hourlySentTreeMap.put(3, hourlySentTreeMap.get(3) + 1);
            }
            if (hourOfDay <= TWELVE_PM && hourOfDay > NINE_AM) {
                hourlySentTreeMap.put(4, hourlySentTreeMap.get(4) + 1);
            }
            if (hourOfDay <= THREE_PM && hourOfDay > TWELVE_AM) {
                hourlySentTreeMap.put(5, hourlySentTreeMap.get(5) + 1);
            }
            if (hourOfDay <= SIX_PM && hourOfDay > THREE_PM) {
                hourlySentTreeMap.put(6, hourlySentTreeMap.get(6) + 1);
            }
            if (hourOfDay <= NINE_PM && hourOfDay > SIX_PM) {
                hourlySentTreeMap.put(7, hourlySentTreeMap.get(7) + 1);
            }
            if (hourOfDay <= ELEVEN_PM && hourOfDay > NINE_PM) {
                hourlySentTreeMap.put(7, hourlySentTreeMap.get(7) + 1);
            }
        }
        return hourlySentTreeMap;
    }
}
