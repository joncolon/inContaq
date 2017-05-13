package nyc.c4q.jonathancolon.inContaq.data.asynctasks.params;

import java.util.TreeMap;


public class MonthlyTaskParams {
    public final String phoneNumber;
    public final TreeMap<Integer, Integer> monthlyTexts;

    public MonthlyTaskParams(String phoneNumber, TreeMap<Integer, Integer> monthlyTexts) {
        this.phoneNumber = phoneNumber;
        this.monthlyTexts = monthlyTexts;
    }
}
