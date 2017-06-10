package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.TreeMap;

import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.util.PrepareMonthlyTreeMap;

/**
 * Created by jonathancolon on 5/14/17.
 */

public class GetMonthlySent {

    private TreeMap<Integer, Integer> monthlySentTreeMap;
    private ArrayList<Sms> smsList;

    public GetMonthlySent(ArrayList<Sms> smsList) {
        this.smsList = smsList;
    }

    public TreeMap<Integer, Integer> getMonthlySent() {
        ArrayList<String> sentSms = new ArrayList<>();
        PrepareMonthlyTreeMap treeMap = new PrepareMonthlyTreeMap();
        monthlySentTreeMap = treeMap.setUpMonthlyTreeMap();

        for (int i = 0; i < smsList.size(); i++) {
            if (smsList.get(i).getType().equals("2")) {
                sentSms.add(smsList.get(i).getTime());
            }
        }
        monthlySentTreeMap = mapMonthlyTexts(sentSms);
        return monthlySentTreeMap;
    }

    private TreeMap<Integer, Integer> mapMonthlyTexts(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            long lg = Long.parseLong(list.get(i));
            DateTime juDate = new DateTime(lg);
            int month = juDate.getMonthOfYear();

            if (monthlySentTreeMap.containsKey(month)) {
                monthlySentTreeMap.put(month, monthlySentTreeMap.get(month) + 1);
                monthlySentTreeMap.entrySet();
            }
        }
        return monthlySentTreeMap;
    }
}
