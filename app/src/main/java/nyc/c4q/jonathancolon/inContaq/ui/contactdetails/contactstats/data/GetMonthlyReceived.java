package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.model.SmsModel;


public class GetMonthlyReceived {

    private TreeMap<Integer, Integer> monthlyReceivedTreeMap;
    private ArrayList<SmsModel> smsModelList;

    public GetMonthlyReceived(ArrayList<SmsModel> smsModelList) {
        this.smsModelList = smsModelList;
    }

    public TreeMap<Integer, Integer> getMonthlyReceived() {
        ArrayList<String> receivedSms = new ArrayList<>();
        PrepareMonthlyTreeMap treeMap = new PrepareMonthlyTreeMap();
        monthlyReceivedTreeMap = treeMap.setUpMonthlyTreeMap();

        for (int i = 0; i < smsModelList.size(); i++) {
            if (smsModelList.get(i).getType().equals("1")) {
                receivedSms.add(smsModelList.get(i).getTimeStamp());
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
