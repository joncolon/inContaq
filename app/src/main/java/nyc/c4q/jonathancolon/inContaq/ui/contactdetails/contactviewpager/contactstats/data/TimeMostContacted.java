package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jonathancolon on 5/9/17.
 */

public class TimeMostContacted {

    public String maxValue(TreeMap<Integer, Integer> treeMap) {
        TreeMap<Integer, Integer> timeReceived = treeMap;
        Map.Entry<Integer, Integer> maxEntry = getMaxEntry(timeReceived);
        return String.valueOf(maxEntry.getKey());
    }

    private Map.Entry<Integer, Integer> getMaxEntry(TreeMap<Integer, Integer> timeReceived) {
        Map.Entry<Integer, Integer> maxEntry;
        maxEntry = null;

        for (Map.Entry<Integer, Integer> entry : timeReceived.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }
}
