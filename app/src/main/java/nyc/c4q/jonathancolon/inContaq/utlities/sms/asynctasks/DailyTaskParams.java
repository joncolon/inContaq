package nyc.c4q.jonathancolon.inContaq.utlities.sms.asynctasks;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;

/**
 * Created by Hyun on 3/11/17.
 */

public class DailyTaskParams {

    private static ArrayList<Sms> dailyListSms;
    private static TreeMap<Integer, Integer> dailyTexts;

    public DailyTaskParams(ArrayList<Sms> dailySms, TreeMap<Integer, Integer> dailyTextMsg) {
        dailyListSms = dailySms;
        dailyTexts = dailyTextMsg;
    }

    static ArrayList<Sms> getdailySmsList() {
        return dailyListSms;
    }

    static TreeMap<Integer, Integer> getDailyTexts() {
        return dailyTexts;
    }
}
