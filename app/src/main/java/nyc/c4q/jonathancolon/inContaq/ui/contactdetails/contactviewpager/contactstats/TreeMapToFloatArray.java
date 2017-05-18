package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jonathancolon on 5/9/17.
 */

public class TreeMapToFloatArray {

    public float[] convertTreeMapToFloats(TreeMap<Integer, Integer> treeMap) {
        ArrayList<Float> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
            Float value = entry.getValue().floatValue();
            list.add(value);
        }
        return convertFloats(list);
    }

    private float[] convertFloats(List<Float> floats) {
        float[] ret = new float[floats.size()];
        Iterator<Float> iterator = floats.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }
}
