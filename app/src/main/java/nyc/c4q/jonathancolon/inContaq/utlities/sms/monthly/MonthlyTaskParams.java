package nyc.c4q.jonathancolon.inContaq.utlities.sms.monthly;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.utlities.sms.Sms;


public class MonthlyTaskParams {
    final ArrayList<Sms> listSms;
    final TreeMap<Integer, Integer> monthlyTexts;

    public MonthlyTaskParams(ArrayList<Sms> listSms, TreeMap<Integer, Integer> monthlyTexts) {
        this.listSms = listSms;
        this.monthlyTexts = monthlyTexts;
    }
}
