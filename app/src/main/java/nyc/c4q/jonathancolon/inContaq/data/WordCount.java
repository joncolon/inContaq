package nyc.c4q.jonathancolon.inContaq.data;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import nyc.c4q.jonathancolon.inContaq.data.asynctasks.TotalWordCountWorkerTask;
import nyc.c4q.jonathancolon.inContaq.sms.SmsHelper;
import nyc.c4q.jonathancolon.inContaq.sms.model.Sms;

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
        ArrayList<Integer> wordCount = new ArrayList<>();
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

    public static int getWordCountPerMessage(Sms sms) {
        String input = sms.getMsg();
        if (input == null || input.isEmpty()) {
            return 0;
        }
        String[] words = input.split("\\s+");
        return words.length;
    }

    public static int getAverageWordCountSent(ArrayList<Sms> smsList){
        TotalWordCountWorkerTask wordCountWorkerTask = new TotalWordCountWorkerTask();
        ArrayList<Sms> sentSms = SmsHelper.parseSentSms(smsList);
        ArrayList<Integer> totalSmsAmount = null;

        try {
            totalSmsAmount = wordCountWorkerTask.execute(sentSms).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return calculateAverage(totalSmsAmount);
    }

    public static int getAverageWordCountReceived(ArrayList<Sms> smsList){
        TotalWordCountWorkerTask wordCountWorkerTask = new TotalWordCountWorkerTask();
        ArrayList<Sms> receivedSms = SmsHelper.parseReceivedSms(smsList);
        ArrayList<Integer> totalSmsAmount = null;

        try {
            totalSmsAmount = wordCountWorkerTask.execute(receivedSms).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return calculateAverage(totalSmsAmount);
    }

    private static int calculateAverage(ArrayList<Integer> numbers) {
        int sum = calculateTotalWords(numbers);
        if (sum != 0) {
            return sum / numbers.size();
        }
        return sum;
    }
}
