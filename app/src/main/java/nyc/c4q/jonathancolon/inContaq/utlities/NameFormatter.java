package nyc.c4q.jonathancolon.inContaq.utlities;

import java.security.InvalidParameterException;

import io.reactivex.annotations.NonNull;

public class NameFormatter {

    public static String[] splitFirstAndLastName(@NonNull String name) {
        if (isValid(name)) {
            String lastName = "";
            String firstName;

            if (hasMultipleNames(name)) {
                lastName = name.substring(name.lastIndexOf(" ") + 1);
                firstName = name.substring(0, name.lastIndexOf(' '));

                String[] splitName = new String[2];
                splitName[0] = firstName;
                splitName[1] = lastName;
                return splitName;
            }

            String[] splitName = new String[2];
            splitName[0] = name;
            splitName[1] = lastName;
            return splitName;
        }
        throw new InvalidParameterException("NO NAME ENTERED");
    }

    public static boolean hasMultipleNames(@NonNull String name) {
        return name.split("\\s+").length > 1;
    }

    private static boolean isValid(@NonNull String name) {
        return name.trim().length() > 0;
    }
}

