package nyc.c4q.jonathancolon.inContaq.data.asynctasks.params;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;


public class DailyTaskParams {

    private static ArrayList<Sms> dailyListSms;
    private static TreeMap<Integer, Integer> dailyTexts;

    public DailyTaskParams(ArrayList<Sms> dailySms, TreeMap<Integer, Integer> dailyTextMsg) {
        dailyListSms = dailySms;
        dailyTexts = dailyTextMsg;
    }

    public static ArrayList<Sms> getdailySmsList() {
        return dailyListSms;
    }

    public static TreeMap<Integer, Integer> getDailyTexts() {
        return dailyTexts;
    }
}
