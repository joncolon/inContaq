package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsHelper;

public class WordCount {

    public WordCount() {
    }

    public static int wordCountSent(ArrayList<Sms> smsList) {
        ArrayList<Sms> smsSent = SmsHelper.parseSentSms(smsList);
        ArrayList<Integer> wordCount = new ArrayList<>();

        for (int i = 0; i < smsSent.size(); i++) {
            wordCount.add(getWordCountPerMessage(smsList.get(i)));
        }

        return calculateTotalWords(wordCount);
    }

    private static int getWordCountPerMessage(Sms sms) {
        String input = sms.getMsg();
        if (input == null || input.isEmpty()) {
            return 0;
        }
        String[] words = input.split("\\s+");
        return words.length;
    }

    private static int calculateTotalWords(ArrayList<Integer> numbers) {
        int sum = 0;
        int arraySize = numbers.size();
        for (int i = 0; i < arraySize; i++) {
            sum = sum + numbers.get(i);
        }
        return sum;
    }

    public static int wordCountReceived(ArrayList<Sms> smsList) {
        ArrayList<Sms> receivedSms = SmsHelper.parseReceivedSms(smsList);
        ArrayList<Integer> wordCount = new ArrayList<>();
        for (int i = 0; i < receivedSms.size(); i++) {
            wordCount.add(getWordCountPerMessage(smsList.get(i)));
        }
        return calculateTotalWords(wordCount);
    }

    public static int getAverageWordCountReceived(ArrayList<Sms> smsList) {
        ArrayList<Sms> receivedSms = SmsHelper.parseReceivedSms(smsList);
        ArrayList<Integer> wordCount = new ArrayList<>();

        for (int i = 0; i < receivedSms.size(); i++) {
            wordCount.add(WordCount.getWordCountPerMessage(receivedSms.get(i)));
        }

        return calculateAverage(wordCount);
    }

    private static int calculateAverage(ArrayList<Integer> numbers) {
        int sum = calculateTotalWords(numbers);
        if (sum != 0) {
            return sum / numbers.size();
        }
        return sum;
    }

    public static int getAverageWordCountSent(ArrayList<Sms> smsList) {
        ArrayList<Sms> sentSms = SmsHelper.parseSentSms(smsList);
        ArrayList<Integer> wordCount = new ArrayList<>();

        for (int i = 0; i < sentSms.size(); i++) {
            wordCount.add(WordCount.getWordCountPerMessage(sentSms.get(i)));
        }

        return calculateAverage(wordCount);
    }
}
