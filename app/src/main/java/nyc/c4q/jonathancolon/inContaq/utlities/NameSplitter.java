package nyc.c4q.jonathancolon.inContaq.utlities;

public class NameSplitter {

    public static String[] splitFirstAndLastName(String name) {
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

