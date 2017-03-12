package nyc.c4q.jonathancolon.inContaq.utlities.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextFreq {

    public static String getMostFreq() {

        // List will be object
        List<String> array = new ArrayList<>();
        array.add("String1");
        array.add("String2");
        array.add("String2");
        array.add("String2");
        array.add("String2");
        array.add("String1");
        array.add("String1");
        array.add("String4");
        array.add("String5");

        Map<String, Integer> map = new HashMap<String, Integer>();
        String mostFrequentWord = " ";
        int largest = 0;

        for (int i = 0; i < array.size(); i++) {
            if (map.get(array.get(i)) == null) {
                map.put(array.get(i), 1);
            } else {
                map.put(array.get(i), map.get(array.get(i)) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            if (value > largest) {
                largest = value;
                mostFrequentWord = key;
            }
        }
        return mostFrequentWord;
    }
}