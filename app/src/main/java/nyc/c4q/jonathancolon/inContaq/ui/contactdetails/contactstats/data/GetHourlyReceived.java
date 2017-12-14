package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.model.Sms;

/**
 * Created by jonathancolon on 5/14/17.
 */

public class GetHourlyReceived {
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
                receivedSms.add(smsList.get(i).getTimeStamp());
            }
        }
        hourlyReceivedTreeMap = mapHourlySms(receivedSms);
        return hourlyReceivedTreeMap;
    }

    // 12am=0, 3am=1, 6am=2 ,9am=3,12pm=4, (15)3pm=5, (18)6pm=6, (21)9pm=7, (24)12am=8
    private TreeMap<Integer, Integer> mapHourlySms(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            long lg = Long.parseLong(list.get(i));
            DateTime mDateTime = new DateTime(lg);
            int hourOfDay = mDateTime.getHourOfDay();

            if (hourOfDay == TWELVE_AM || hourOfDay == MIDNIGHT) {
                hourlyReceivedTreeMap.put(0, hourlyReceivedTreeMap.get(0) + 1);
                hourlyReceivedTreeMap.put(8, hourlyReceivedTreeMap.get(8) + 1);
            }
            if (hourOfDay <= THREE_AM && hourOfDay > TWELVE_AM) {
                hourlyReceivedTreeMap.put(1, hourlyReceivedTreeMap.get(1) + 1);
            }
            if (hourOfDay <= SIX_AM && hourOfDay > THREE_AM) {
                hourlyReceivedTreeMap.put(2, hourlyReceivedTreeMap.get(2) + 1);
            }
            if (hourOfDay <= NINE_AM && hourOfDay > SIX_AM) {
                hourlyReceivedTreeMap.put(3, hourlyReceivedTreeMap.get(3) + 1);
            }
            if (hourOfDay <= TWELVE_PM && hourOfDay > NINE_AM){
                hourlyReceivedTreeMap.put(4, hourlyReceivedTreeMap.get(4) + 1);
            }
            if (hourOfDay <= THREE_PM && hourOfDay > TWELVE_AM){
                hourlyReceivedTreeMap.put(5, hourlyReceivedTreeMap.get(5) + 1);
            }
            if (hourOfDay <= SIX_PM && hourOfDay > THREE_PM){
                hourlyReceivedTreeMap.put(6, hourlyReceivedTreeMap.get(6) + 1);
            }
            if (hourOfDay <= NINE_PM && hourOfDay > SIX_PM){
                hourlyReceivedTreeMap.put(7, hourlyReceivedTreeMap.get(7) + 1);
            }
            if (hourOfDay <= ELEVEN_PM && hourOfDay > NINE_PM){
                hourlyReceivedTreeMap.put(7, hourlyReceivedTreeMap.get(7) + 1);
            }
        }
        return hourlyReceivedTreeMap;
    }
}
