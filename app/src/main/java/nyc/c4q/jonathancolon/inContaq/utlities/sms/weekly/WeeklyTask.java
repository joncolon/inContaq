package nyc.c4q.jonathancolon.inContaq.utlities.sms.weekly;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;

public class WeeklyTask {

    final ArrayList<Sms> listSms;
    final TreeMap<Integer, Integer> weeklyTexts;

    public WeeklyTask(ArrayList<Sms> listSms, TreeMap<Integer, Integer> weeklyTexts) {
        this.listSms = listSms;
        this.weeklyTexts = weeklyTexts;
    }
}
