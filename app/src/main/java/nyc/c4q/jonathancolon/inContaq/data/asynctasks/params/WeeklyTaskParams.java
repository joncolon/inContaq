package nyc.c4q.jonathancolon.inContaq.data.asynctasks.params;

import java.util.TreeMap;

public class WeeklyTaskParams {

    public final String phoneNumber;
    public final TreeMap<Integer, Integer> weeklyTexts;

    public WeeklyTaskParams(String phoneNumber, TreeMap<Integer, Integer> weeklyTexts) {
        this.phoneNumber = phoneNumber;
        this.weeklyTexts = weeklyTexts;
    }
}
