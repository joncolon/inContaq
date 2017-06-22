package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data;

import java.util.TreeMap;


public class PrepareHourlyTreeMap {

    //12am occurs on both the start and end of the line graph.
    // 12am=0, 3am=1, 6am=2 ,9am=3, 12pm=4, (15)3pm=5, (18)6pm=6, (21)9pm=7, (24)12am=8
    private static final int TWELVE_AM = 0;
    private static final int THREE_AM = 1;
    private static final int SIX_AM = 2;
    private static final int NINE_AM = 3;
    private static final int TWELVE_PM = 4;
    private static final int THREE_PM = 5;
    private static final int SIX_PM = 6;
    private static final int NINE_PM = 7;
    private static final int MIDNIGHT = 8;

    synchronized public TreeMap<Integer, Integer> setUpHourlyTreeMap() {
        TreeMap<Integer, Integer> hourlyMapTemplate = new TreeMap<>();
        hourlyMapTemplate.put(TWELVE_AM, DEFAULT_VALUE);
        hourlyMapTemplate.put(THREE_AM, DEFAULT_VALUE);
        hourlyMapTemplate.put(SIX_AM, DEFAULT_VALUE);
        hourlyMapTemplate.put(NINE_AM, DEFAULT_VALUE);
        hourlyMapTemplate.put(TWELVE_PM, DEFAULT_VALUE);
        hourlyMapTemplate.put(THREE_PM, DEFAULT_VALUE);
        hourlyMapTemplate.put(SIX_PM, DEFAULT_VALUE);
        hourlyMapTemplate.put(NINE_PM, DEFAULT_VALUE);
        hourlyMapTemplate.put(MIDNIGHT, DEFAULT_VALUE);

        return hourlyMapTemplate;
    }

    private static final int DEFAULT_VALUE = 0;
}
