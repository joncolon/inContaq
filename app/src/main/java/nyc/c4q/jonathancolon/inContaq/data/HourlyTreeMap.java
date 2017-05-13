package nyc.c4q.jonathancolon.inContaq.data;

import java.util.TreeMap;

/**
 * Created by jonathancolon on 5/9/17.
 */

public class HourlyTreeMap {

    synchronized public TreeMap<Integer, Integer> setUpDailyTextMap() {
        TreeMap<Integer, Integer> hourlyMapTemplate = new TreeMap<>();
        int m = 0;
        for (int i = 0; i < 9; i++) { // 12am=0, 3am=1, 6am=2 ,9am=3,12pm=4, (15)3pm=5, (18)6pm=6, (21)9pm=7, (24)12am=8
            int defaultValue = 0;
            if (i == 0) {
                hourlyMapTemplate.put(0, defaultValue);
                m += 1;
            } else {
                hourlyMapTemplate.put((i * 3), defaultValue);
                m += 1;
            }
        }
        return hourlyMapTemplate;
    }
}
