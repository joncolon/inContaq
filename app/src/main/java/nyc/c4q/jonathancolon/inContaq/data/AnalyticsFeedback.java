package nyc.c4q.jonathancolon.inContaq.data;

public class AnalyticsFeedback {
    public AnalyticsFeedback() {
    }

    public static String timeFeedback(String hour){
        int hourInt = Integer.valueOf(hour);
        if (hourInt > 5 && hourInt < 12){
            return "text each other mostly in the morning. Early birds, are we?";
        }

        if (hourInt == 12){
            return "text each other mostly at noon. Lunch buddies, much?";
        }

        if (hourInt > 12 && hourInt < 17){
            return "text each other mostly in the afternoon.";
        }

        if (hourInt >= 17 && hourInt <= 20){
            return "each other mostly in the evening. What's for dinner?";
        }

        if (hourInt >= 21 && hourInt < 24 || hourInt < 5){
            return "text each other mostly at night. Hoot hoot.";
        }

        if (hourInt == 24){
            return "text each other mostly at midnight. Is this your glass slipper?";
        }
        return null;
    }
}
