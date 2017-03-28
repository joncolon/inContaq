package nyc.c4q.jonathancolon.inContaq.graphs.linegraphs;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.data.SmsAnalytics;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;


class DailyGraphHelper {
    private SmsAnalytics smsAnalytics;


    DailyGraphHelper(ArrayList<Sms> smsList) {
        this.smsAnalytics = new SmsAnalytics(smsList);
    }

    synchronized int getYValue(ArrayList<Sms> smsList) {
        int maxSent = findMaximumValue(getSentValues(smsList));
        int maxReceived = findMaximumValue(getReceivedValue(smsList));
        int highestValue = Math.max(maxSent, maxReceived);
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

    float[] getSentValues(ArrayList<Sms> smsList) {
        return smsAnalytics.getHourlySentValues(smsList);
    }

    float[] getReceivedValue(ArrayList<Sms> smsList) {
        return smsAnalytics.getHourlyReceivedValues(smsList);
    }

    private int increaseByQuarter(int input) {
        return (int) Math.round(input * 1.25);
    }

    @NonNull
    String[] getXAxisLabels() {
        final String[] xAxisLabels = new String[9];
        int n = 0;
        for (int i = 0; i < 4; i++) { // 12am=0, 3am=1, 6am=2 ,9am=3
            if (i == 0) {
                xAxisLabels[n] = "12AM";
                n += 1;
            } else {
                xAxisLabels[n] = String.valueOf(i * 3) + "AM";
                n += 1;
            }
        }
        for (int i = 0; i <= 4; i++) { // 12pm=0, 3pm=1, 6pm=2, 9pm=3, 12am=4
            if (i == 0) {
                xAxisLabels[n] = "12PM";
                n += 1;
            } else if (i == 4) {
                xAxisLabels[n] = "12AM";
                n += 1;
            } else {
                xAxisLabels[n] = String.valueOf(i * 3) + "PM";
                n += 1;
            }
        }
        return xAxisLabels;
    }
}
