package nyc.c4q.jonathancolon.inContaq.utlities;

import java.util.List;

/**
 * Created by jonathancolon on 6/10/17.
 */

public class ObjectUtils {
    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isEmptyList(List list) {
        return list == null || list.isEmpty();
    }
}
