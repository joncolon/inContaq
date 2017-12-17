package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data;

import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nyc.c4q.jonathancolon.inContaq.model.SmsModel;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsUtils;

public class WordCount {

    private SmsUtils smsUtils;

    @Inject
    public WordCount(SmsUtils smsUtils) {
        this.smsUtils = smsUtils;
    }

    public int totalWordCountSent(ArrayList<SmsModel> smsModelList) {
        ArrayList<SmsModel> smsModelSent = smsUtils.parseSentSms(smsModelList);
        ArrayList<Integer> wordCount = new ArrayList<>();

        for (int i = 0; i < smsModelSent.size(); i++) {
            wordCount.add(getWordCountPerMessage(smsModelList.get(i)));

        }
        return calculateTotalWords(wordCount);
    }

    private int getWordCountPerMessage(SmsModel smsModel) {
        String input = smsModel.getMessage();
        if (input == null || input.isEmpty()) {
            return 0;
        }
        String[] words = input.split("\\s+");
        for (String word : words) {
            if (Objects.equals(word.toLowerCase(), "smiling")) {
                Log.e("EMOJI", "checkForSmiling: " + word);
            }
        }
        return words.length;
    }

    private int calculateTotalWords(ArrayList<Integer> numbers) {
        int sum = 0;
        int arraySize = numbers.size();
        for (int i = 0; i < arraySize; i++) {
            sum = sum + numbers.get(i);
        }
        return sum;
    }

    public int totalWordCountReceived(ArrayList<SmsModel> smsModelList) {
        ArrayList<SmsModel> receivedSms = smsUtils.parseReceivedSms(smsModelList);
        ArrayList<Integer> count = new ArrayList<>();
        for (int i = 0; i < receivedSms.size(); i++) {
            count.add(getWordCountPerMessage(smsModelList.get(i)));
        }
        return calculateTotalWords(count);
    }

    public void setAvgWordCountSentText(ArrayList<SmsModel> list, TextView tv) {
        Observable.fromCallable(() -> averageWordCountSent(list))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wordCount -> tv.setText(String.valueOf(wordCount)));
    }

    public int averageWordCountSent(ArrayList<SmsModel> smsModelList) {
        ArrayList<SmsModel> sentSms = smsUtils.parseSentSms(smsModelList);
        ArrayList<Integer> wordCount = new ArrayList<>();

        for (int i = 0; i < sentSms.size(); i++) {
            wordCount.add(getWordCountPerMessage(sentSms.get(i)));
        }

        return calculateAverage(wordCount);
    }

    private int calculateAverage(ArrayList<Integer> numbers) {
        int sum = calculateTotalWords(numbers);
        if (sum != 0) {
            return sum / numbers.size();
        }
        return sum;
    }

    public void setAvgWordCountReceivedText(ArrayList<SmsModel> smsModelList, TextView tv) {
        Observable.fromCallable(() -> averageWordCountReceived(smsModelList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wordCount -> tv.setText(String.valueOf(wordCount)));
    }

    public int averageWordCountReceived(ArrayList<SmsModel> smsModelList) {
        ArrayList<SmsModel> receivedSms = smsUtils.parseReceivedSms(smsModelList);
        ArrayList<Integer> wordCount = new ArrayList<>();

        for (int i = 0; i < receivedSms.size(); i++) {
            wordCount.add(getWordCountPerMessage(receivedSms.get(i)));
        }

        return calculateAverage(wordCount);
    }
}
