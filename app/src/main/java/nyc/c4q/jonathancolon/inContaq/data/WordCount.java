package nyc.c4q.jonathancolon.inContaq.data;

import android.util.Log;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.utlities.sms.SmsHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;

import static android.content.ContentValues.TAG;

public class WordCount {

    public WordCount() {
    }

    public static int getTotalSent(ArrayList<Sms> smsList) {
        smsList = SmsHelper.parseSentSms(smsList);
        ArrayList<Integer> wordCount = new ArrayList<Integer>();

        for (int i = 0; i < smsList.size(); i++) {
            wordCount.add(getWordCountPerMessage(smsList.get(i)));
        }
        return calculateTotalWords(wordCount);
    }

    public static int getTotalReceived(ArrayList<Sms> smsList) {
        smsList = SmsHelper.parseReceivedSms(smsList);
        ArrayList<Integer> wordCount = new ArrayList<Integer>();
        for (int i = 0; i < smsList.size(); i++) {
            wordCount.add(getWordCountPerMessage(smsList.get(i)));
        }
        return calculateTotalWords(wordCount);
    }

    private static int calculateTotalWords(ArrayList<Integer> numbers) {
        int sum = 0;
        int arraySize = numbers.size();
        for (int i = 0; i < arraySize; i++) {
            sum = sum + numbers.get(i);
        }
        return sum;
    }

    private static int getWordCountPerMessage(Sms sms) {
        String input = sms.getMsg();
        if (input == null || input.isEmpty()) {
            return 0;
        }
        String[] words = input.split("\\s+");
        return words.length;
    }

    private static ArrayList<Integer> getTotalWordCount(ArrayList<Sms> smsList) {
        ArrayList<Integer> wordCount = new ArrayList<Integer>();
        for (int i = 0; i < smsList.size(); i++) {
            wordCount.add(getWordCountPerMessage(smsList.get(i)));
        }
        return wordCount;
    }

    public static int getAverageWordCountSent(ArrayList<Sms> smsList) {
        SmsHelper sHelp = new SmsHelper();
        ArrayList<Sms> sentSms = sHelp.parseSentSms(smsList);
        Log.d(TAG, "getAverageWordCountSent: " + getTotalWordCount(sentSms));
        return calculateAverage(getTotalWordCount(sentSms));
    }

    public static int getAverageWordCountReceived(ArrayList<Sms> smsList) {
        SmsHelper sHelp = new SmsHelper();
        ArrayList<Sms> receivedSms = sHelp.parseReceivedSms(smsList);
        Log.d(TAG, "getAverageWordCountReceived: " + getTotalWordCount(receivedSms));
        return calculateAverage(getTotalWordCount(receivedSms));
    }

    private static int calculateAverage(ArrayList<Integer> numbers) {
        int sum = calculateTotalWords(numbers);
        if (sum != 0) {
            int average = sum / numbers.size();
            return average;
        }
        return sum;
    }
}
