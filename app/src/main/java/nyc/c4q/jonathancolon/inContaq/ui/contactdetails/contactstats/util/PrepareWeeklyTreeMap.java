package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.util;

import java.util.TreeMap;

/**
 * Created by jonathancolon on 5/14/17.
 */

public class PrepareWeeklyTreeMap {

    private static final int SUN = 1;
    private static final int MON = 2;
    private static final int TUE = 3;
    private static final int WED = 4;
    private static final int THUR = 5;
    private static final int FRI = 6;
    private static final int SAT = 7;
    private final int DEFAULT_VALUE = 0;

    public TreeMap<Integer, Integer> setUpWeeklyTextMap() {

        TreeMap<Integer, Integer> weeklyMap = new TreeMap<>();

        weeklyMap.put(SUN, DEFAULT_VALUE);
        weeklyMap.put(MON, DEFAULT_VALUE);
        weeklyMap.put(TUE, DEFAULT_VALUE);
        weeklyMap.put(WED, DEFAULT_VALUE);
        weeklyMap.put(THUR, DEFAULT_VALUE);
        weeklyMap.put(FRI, DEFAULT_VALUE);
        weeklyMap.put(SAT, DEFAULT_VALUE);

        return weeklyMap;
    }
}
