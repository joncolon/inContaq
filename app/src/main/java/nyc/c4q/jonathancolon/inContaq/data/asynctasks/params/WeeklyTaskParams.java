package nyc.c4q.jonathancolon.inContaq.data.asynctasks.params;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.sms.model.Sms;

public class WeeklyTaskParams {

    public final ArrayList<Sms> listSms;
    public final TreeMap<Integer, Integer> weeklyTexts;

    public WeeklyTaskParams(ArrayList<Sms> listSms, TreeMap<Integer, Integer> weeklyTexts) {
        this.listSms = listSms;
        this.weeklyTexts = weeklyTexts;
    }
}
