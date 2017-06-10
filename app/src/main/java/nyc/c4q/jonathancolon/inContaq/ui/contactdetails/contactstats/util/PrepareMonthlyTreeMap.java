package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.util;

import java.util.TreeMap;

/**
 * Created by jonathancolon on 5/14/17.
 */

public class PrepareMonthlyTreeMap {

    private static final int JAN = 1;
    private static final int FEB = 2;
    private static final int MAR = 3;
    private static final int APR = 4;
    private static final int MAY = 5;
    private static final int JUN = 6;
    private static final int JUL = 7;
    private static final int AUG = 8;
    private static final int SEP = 9;
    private static final int OCT = 10;
    private static final int NOV = 11;
    private static final int DEC = 12;
    private final int DEFAULT_VALUE = 0;

    public TreeMap<Integer, Integer> setUpMonthlyTreeMap() {
        TreeMap<Integer, Integer> monthlyMapTemplate = new TreeMap<>();
        monthlyMapTemplate.put(JAN, DEFAULT_VALUE);
        monthlyMapTemplate.put(FEB, DEFAULT_VALUE);
        monthlyMapTemplate.put(MAR, DEFAULT_VALUE);
        monthlyMapTemplate.put(APR, DEFAULT_VALUE);
        monthlyMapTemplate.put(MAY, DEFAULT_VALUE);
        monthlyMapTemplate.put(JUN, DEFAULT_VALUE);
        monthlyMapTemplate.put(JUL, DEFAULT_VALUE);
        monthlyMapTemplate.put(AUG, DEFAULT_VALUE);
        monthlyMapTemplate.put(SEP, DEFAULT_VALUE);
        monthlyMapTemplate.put(OCT, DEFAULT_VALUE);
        monthlyMapTemplate.put(NOV, DEFAULT_VALUE);
        monthlyMapTemplate.put(DEC, DEFAULT_VALUE);
        return monthlyMapTemplate;
    }
}
