package nyc.c4q.jonathancolon.inContaq;

/**
 * Created by jonathancolon on 3/9/17.
 */

public class NameSplitter {

    //// FIXME: 3/10/17
    public static String[] FixMeSplitFirstAndLastName(String name) {

        if (name.trim().length() > 0) {
            if (name.split("\\w+").length > 1) {
                String[] parts = name.split("\\w+");
                return parts;
            }
            String[] splitName = new String[2];
            splitName[0] = name;
            splitName[1] = "";
            return splitName;
        }
        return null;
    }

    public String[] splitFirstAndLastName(String name) {
        if (name.trim().length() > 0) {
            String lastName = "";
            String firstName;
            if (name.split("\\s+").length > 1) {
                try {
                    lastName = name.substring(name.lastIndexOf(" ") + 1);
                    firstName = name.substring(0, name.lastIndexOf(' '));

                    String[] splitName = new String[2];
                    splitName[0] = firstName;
                    splitName[1] = lastName;
                    return splitName;
                } catch (Exception exception) {
                    throw exception;
                }
            } else {
                String[] splitName = new String[2];
                splitName[0] = name;
                splitName[1] = lastName;
                return splitName;
            }
        }
        throw new NullPointerException("NO NAME ENTERED");
    }

}

