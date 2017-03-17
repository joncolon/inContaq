package nyc.c4q.jonathancolon.inContaq.utlities.graphs.linegraphs;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.utlities.sms.DailySmsAnalytics;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.Sms;

/**
 * Created by jonathancolon on 3/17/17.
 */

public class DailyGraphHelper {
    DailySmsAnalytics dailySmsAnalytics;
    private float[] receivedValues;
    private float[] sentValues;
    private int highestValue;
    private ArrayList<Sms> smsList;


    public DailyGraphHelper(ArrayList<Sms> smsList) {
        this.smsList = smsList;
        dailySmsAnalytics = new DailySmsAnalytics(smsList);
    }

    private void getLineGraphValues(ArrayList<Sms> smsList) {
    }

    public float[] getSentValues(){
        return dailySmsAnalytics.getSentValues();
    }
    public float[] getReceivedValue(){
        return dailySmsAnalytics.getReceivedValue();
    }

    synchronized public int getYValue() {
        int maxSent = findMaximumValue(getSentValues());
        int maxReceived = findMaximumValue(getReceivedValue());

        if (maxSent == 0 && maxReceived == 0){
            return highestValue = 100;
        }
        if (maxSent > maxReceived) {
            return highestValue = (int) getRound(maxSent);
        }
        return highestValue = (int) getRound(maxReceived);
    }

    @NonNull
    public String[] getXAxisLabels() {
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

    private long getRound(int input) {
        //this rounds up and multiples the value by a quarter to customize the Y axis to the contact
        return Math.round(input * 1.25);
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

}
