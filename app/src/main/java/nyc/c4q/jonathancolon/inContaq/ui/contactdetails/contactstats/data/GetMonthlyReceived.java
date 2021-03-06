package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.model.Sms;


public class GetMonthlyReceived {

    private TreeMap<Integer, Integer> monthlyReceivedTreeMap;
    private ArrayList<Sms> smsList;

    public GetMonthlyReceived(ArrayList<Sms> smsList) {
        this.smsList = smsList;
    }

    public TreeMap<Integer, Integer> getMonthlyReceived() {
        ArrayList<String> receivedSms = new ArrayList<>();
        PrepareMonthlyTreeMap treeMap = new PrepareMonthlyTreeMap();
        monthlyReceivedTreeMap = treeMap.setUpMonthlyTreeMap();

        for (int i = 0; i < smsList.size(); i++) {
            if (smsList.get(i).getType().equals("1")) {
                receivedSms.add(smsList.get(i).getTimeStamp());
            }
        }
        monthlyReceivedTreeMap = mapMonthlyTexts(receivedSms);
        return monthlyReceivedTreeMap;
    }

    private TreeMap<Integer, Integer> mapMonthlyTexts(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            long lg = Long.parseLong(list.get(i));
            DateTime juDate = new DateTime(lg);
            int month = juDate.getMonthOfYear();

            if (monthlyReceivedTreeMap.containsKey(month)) {
                monthlyReceivedTreeMap.put(month, monthlyReceivedTreeMap.get(month) + 1);
                monthlyReceivedTreeMap.entrySet();
            }
        }
        return monthlyReceivedTreeMap;
    }
}
