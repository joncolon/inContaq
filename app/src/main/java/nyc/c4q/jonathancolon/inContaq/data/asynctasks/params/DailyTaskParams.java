package nyc.c4q.jonathancolon.inContaq.data.asynctasks.params;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.sms.model.Sms;


public class DailyTaskParams {

    public final ArrayList<Sms> listSms;
    public final TreeMap<Integer, Integer> dailyTexts;

    public DailyTaskParams(ArrayList<Sms> listSms, TreeMap<Integer, Integer> dailyTexts) {
        this.listSms = listSms;
        this.dailyTexts = dailyTexts;
    }
}
