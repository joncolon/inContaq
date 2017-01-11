package nyc.c4q.jonathancolon.inContaq.utilities.sms;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by jonathancolon on 1/11/17.
 */

public class MonthlyTaskParams {
    ArrayList<Sms> lstSms;
    TreeMap<Integer, Integer> monthlyTexts;

    public MonthlyTaskParams(ArrayList<Sms> lstSms, TreeMap<Integer, Integer> monthlyTexts) {
        this.lstSms = lstSms;
        this.monthlyTexts = monthlyTexts;
    }
}
