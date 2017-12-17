package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.model.SmsModel;


public class GetWeeklySent {

    private TreeMap<Integer, Integer> weeklyReceivedTreeMap;
    private ArrayList<SmsModel> smsModelList;

    public GetWeeklySent(ArrayList<SmsModel> smsModelList) {
        this.smsModelList = smsModelList;
    }

    public TreeMap<Integer, Integer> getWeeklySent() {
        ArrayList<String> sentSms = new ArrayList<>();
        PrepareWeeklyTreeMap treeMap = new PrepareWeeklyTreeMap();
        weeklyReceivedTreeMap = treeMap.setUpWeeklyTextMap();

        for (int i = 0; i < smsModelList.size(); i++) {
            if (smsModelList.get(i).getType().equals("2")) {
                sentSms.add(smsModelList.get(i).getTimeStamp());
            }
        }

        weeklyReceivedTreeMap = getWeeklyTexts(sentSms);
        return weeklyReceivedTreeMap;
    }

    private TreeMap<Integer, Integer> getWeeklyTexts(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {

            Calendar smsCalendar = Calendar.getInstance();
            Calendar todayCalendar = Calendar.getInstance();
            Date smsDate = new Date(Long.parseLong(list.get(i)));
            Date todaysDate = new Date(System.currentTimeMillis());
            smsCalendar.setTime(smsDate);
            todayCalendar.setTime(todaysDate);

            int smsDayOfWeek = smsCalendar.get(Calendar.DAY_OF_WEEK);

            if (weeklyReceivedTreeMap.containsKey(smsDayOfWeek)) {
                weeklyReceivedTreeMap.put(smsDayOfWeek, weeklyReceivedTreeMap.get(smsDayOfWeek) + 1);
                weeklyReceivedTreeMap.entrySet();
            }
        }
        return weeklyReceivedTreeMap;
    }
}

