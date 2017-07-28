package nyc.c4q.jonathancolon.inContaq.utlities;

import android.content.Context;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.R;

public class AnalyticsFeedback {

    private static final int JAN = 1;
    private static final int FEB = 2;
    private static final int MAR = 3;
    private static final int APR = 4;
    private static final int MAY = 5;
    private static final int JUN = 6;
    private static final int JUL = 7;
    private static final int AUG = 8;
    private static final int SEP = 9;
    private static final int OCT = 10;
    private static final int NOV = 11;
    private static final int DEC = 12;

    private static final int SUN = 1;
    private static final int MON = 2;
    private static final int TUE = 3;
    private static final int WED = 4;
    private static final int THU = 5;
    private static final int FRI = 6;
    private static final int SAT = 7;

    private static final int MIDNIGHT = 0;
    private static final int TWELVE_PM = 4;
    private static final int THREE_PM = 5;
    private static final int SIX_PM = 6;
    private static final int NINE_PM = 7;
    private static final int TWELVE_AM = 8;

    private Context context;

    @Inject
    public AnalyticsFeedback(Context context) {
        this.context = context;
    }

    public String timeOfDayFeedback(String hour) {
        int hourInt = Integer.valueOf(hour);

        if (isMorning(hourInt)) {
            return context.getString(R.string.morning_text);
        }

        if (isNoon(hourInt)) {
            return context.getString(R.string.noon_text);
        }

        if (isAfternoon(hourInt)) {
            return context.getString(R.string.afternoon_text);
        }

        if (isEvening(hourInt)) {
            return context.getString(R.string.evening_text);
        }

        if (isNightTime(hourInt)) {
            return context.getString(R.string.night_text);
        }

        if (isMidnight(hourInt)) {
            return context.getString(R.string.midnight_text);
        }
        return null;
    }

    private boolean isMorning(int hourInt) {
        return hourInt > MIDNIGHT && hourInt < TWELVE_PM;
    }

    private boolean isNoon(int hourInt) {
        return hourInt == TWELVE_PM;
    }

    private boolean isAfternoon(int hourInt) {
        return hourInt == THREE_PM;
    }

    private boolean isEvening(int hourInt) {
        return hourInt == SIX_PM;
    }

    private boolean isNightTime(int hourInt) {
        return hourInt == NINE_PM;
    }

    private boolean isMidnight(int hourInt) {
        return hourInt == TWELVE_AM || hourInt == MIDNIGHT;
    }

    public String dayOfWeekFeedback(String day) {
        int dayInt = Integer.valueOf(day);

        switch (dayInt) {
            case SUN:
                return context.getString(R.string.sunday_text);
            case MON:
                return context.getString(R.string.monday_text);
            case TUE:
                return context.getString(R.string.tuesday_text);
            case WED:
                return context.getString(R.string.wednesday_text);
            case THU:
                return context.getString(R.string.thursday_text);
            case FRI:
                return context.getString(R.string.friday_text);
            case SAT:
                return context.getString(R.string.saturday_text);
        }
        return null;
    }

    public String monthOfYearFeedback(String month) {
        int dayInt = Integer.valueOf(month);

        switch (dayInt) {
            case JAN:
                return context.getString(R.string.jan_text);
            case FEB:
                return context.getString(R.string.feb_text);
            case MAR:
                return context.getString(R.string.mar_text);
            case APR:
                return context.getString(R.string.apr_text);
            case MAY:
                return context.getString(R.string.may_text);
            case JUN:
                return context.getString(R.string.jun_text);
            case JUL:
                return context.getString(R.string.jul_text);
            case AUG:
                return context.getString(R.string.aug_text);
            case SEP:
                return context.getString(R.string.sept_text);
            case OCT:
                return context.getString(R.string.oct_text);
            case NOV:
                return context.getString(R.string.nov_text);
            case DEC:
                return context.getString(R.string.dec_text);
        }
        return null;
    }
}
