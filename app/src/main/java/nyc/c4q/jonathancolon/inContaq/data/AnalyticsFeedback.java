package nyc.c4q.jonathancolon.inContaq.data;

public class AnalyticsFeedback {
    public AnalyticsFeedback() {
    }

    public String timeFeedback(String hour){
        int hourInt = Integer.valueOf(hour);
        if (hourInt > 5 && hourInt < 12){
            return "text each other mostly in the morning";
        }

        if (hourInt == 12){
            return "text each other mostly at noon";
        }

        if (hourInt > 12 && hourInt < 17){
            return "text each other mostly in the afternoon";
        }

        if (hourInt >= 17 && hourInt <= 20){
            return "each other mostly in the evening";
        }

        if (hourInt >= 21 && hourInt < 24 || hourInt < 5){
            return "text each other mostly at night";
        }

        if (hourInt == 24){
            return "text each other mostly at midnight";
        }
        return null;
    }
}
