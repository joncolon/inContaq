package nyc.c4q.jonathancolon.inContaq.graphs.linegraphs;

import android.content.Context;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.data.SmsAnalytics;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;


class MonthlyGraphHelper {
    private SmsAnalytics smsAnalytics;
    private Context context;

    MonthlyGraphHelper(Context context, ArrayList<Sms> smsList) {
        this.context = context;
        smsAnalytics = new SmsAnalytics(smsList);
    }

    synchronized int getYValue() {
        int maxSent = findMaximumValue(getSentValues());
        int maxReceived = findMaximumValue(getReceivedValue());
        int highestValue = Math.max(maxSent, maxReceived);

        if (highestValue == 0){
            highestValue = 100;
        }
        return increaseByQuarter(highestValue);
    }

    private static int findMaximumValue(float[] inputArray) {
        float maxValue = inputArray[0];
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i] > maxValue) {
                maxValue = Math.round(inputArray[i]);
            }
        }
        return (int) maxValue;
    }

    float[] getSentValues() {
        return smsAnalytics.getMonthlySentValues();
    }

    float[] getReceivedValue() {
        return smsAnalytics.getMonthlyReceivedValues();
    }

    private int increaseByQuarter(int input) {
        return (int) Math.round(input * 1.25);
    }

    String[] getXAxisLabels(){
        String[] xAxisLabels = new String[]{
                context.getString(R.string.jan), context.getString(R.string.feb),
                context.getString(R.string.mar), context.getString(R.string.apr),
                context.getString(R.string.jun), context.getString(R.string.may),
                context.getString(R.string.jul), context.getString(R.string.aug),
                context.getString(R.string.sep), context.getString(R.string.oct),
                context.getString(R.string.nov), context.getString(R.string.dec)
        };

        return xAxisLabels;
    }
}
