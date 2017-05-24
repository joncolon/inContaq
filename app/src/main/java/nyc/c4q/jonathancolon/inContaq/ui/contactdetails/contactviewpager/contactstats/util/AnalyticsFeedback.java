package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.util;

public class AnalyticsFeedback {

    public AnalyticsFeedback() {
    }

    public String timeOfDayFeedback(String hour) {
        int hourInt = Integer.valueOf(hour);
        if (hourInt > 5 && hourInt < 12) {
            return "text each other mostly in the morning";
        }

        if (hourInt == 12) {
            return "text each other mostly at noon";
        }

        if (hourInt > 12 && hourInt < 17) {
            return "text each other mostly in the afternoon";
        }

        if (hourInt >= 17 && hourInt <= 20) {
            return "each other mostly in the evening";
        }

        if (hourInt >= 21 && hourInt < 24 || hourInt < 5) {
            return "text each other mostly at night";
        }

        if (hourInt == 24) {
            return "text each other mostly at midnight";
        }
        return null;
    }

    public String dayOfWeekFeedback(String day) {
        int dayInt = Integer.valueOf(day);

        switch (dayInt) {
            case 1:
                return "text eachother the most on Sundays";
            case 2:
                return "text eachother the most on Mondays";
            case 3:
                return "text eachother the most on Tuesdays";
            case 4:
                return "text eachother the most on Wednesdays";
            case 5:
                return "text eachother the most on Thursdays";
            case 6:
                return "text eachother the most on Fridays";
            case 7:
                return "text eachother the most on Saturdays";
        }
        return null;
    }

    public String monthOfYearFeedback(String month) {
        int dayInt = Integer.valueOf(month);

        switch (dayInt) {
            case 1:
                return "text each other the most in January";
            case 2:
                return "text each other the most in February";
            case 3:
                return "text each other the most in March";
            case 4:
                return "text each other the most in April";
            case 5:
                return "text each other the most in May";
            case 6:
                return "text each other the most in June";
            case 7:
                return "text each other the most in July";
            case 8:
                return "text each other the most in August";
            case 9:
                return "text each other the most in September";
            case 10:
                return "text each other the most in October";
            case 11:
                return "text each other the most in November";
            case 12:
                return "text each other the most in December";
        }
        return null;
    }
}
