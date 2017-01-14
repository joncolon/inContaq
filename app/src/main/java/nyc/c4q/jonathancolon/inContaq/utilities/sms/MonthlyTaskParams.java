package nyc.c4q.jonathancolon.inContaq.utilities.sms;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by jonathancolon on 1/11/17.
 */

public class MonthlyTaskParams {
    final ArrayList<Sms> listSms;
    final TreeMap<Integer, Integer> monthlyTexts;

    public MonthlyTaskParams(ArrayList<Sms> listSms, TreeMap<Integer, Integer> monthlyTexts) {
        this.listSms = listSms;
        this.monthlyTexts = monthlyTexts;
    }
}
