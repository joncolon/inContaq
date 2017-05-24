package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsHelper;

public class WordFrequency {

    private static List<String> excludedWords = new ArrayList<>(Arrays.asList("the", "of", "and",
            "to", "a", "in", "for", "is", "on", "that", "by", "this", "with", "i", "you", "it",
            "not", "or", "be", "are", "from", "at", "as", "your", "have", "new", "more", "an",
            "was", "I'm", "I", "and", "just", "about", "about", "above", "after", "again",
            "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be",
            "because", "been", "before", "being", "below", "between", "both", "but", "by", "can't",
            "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing",
            "don't", "down", "during", "each", "few", "for", "from", "like", "got", "gotta",
            "has", "hasn't", "have", "i'd", "i'll", "haven't", "having", "he", "he'd", "he'll",
            "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how",
            "how's", "i'm", "i've", "if", "in", "into", "is", "isn't", "it's", "its", "itself",
            "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of",
            "off", "on", "once", "only", "or", "other", "ought", "our", "ourselves", "out", "over",
            "own", "same", "shan't", "she", "she'll", "she'd", "she's", "should", "shouldn't", "so",
            "some", "such", "than", "that", "that's", "the", "their", "theirs", "them",
            "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll",
            "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up",
            "very", "was", "wasn't", "we", "we'd", "we're", "we've", "were", "weren't", "what",
            "what's", "when", "when's", "where", "where's", "which", "who", "who's", "whom", "why",
            "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're",
            "you've", "your", "yours", "yourself", "yourselves", "further", "had", "hadn't",
            "gonna"));

    private ArrayList<Sms> smsList;

    public WordFrequency(ArrayList<Sms> smsList) {
        this.smsList = smsList;
    }


    public String mostCommonWordReceived() {
        ArrayList<Sms> smsReceived = SmsHelper.parseReceivedSms(smsList);
        ArrayList<String> wordArrayList = new ArrayList<>();

        for (int i = 0; i < smsReceived.size(); i++) {
            String message = smsReceived.get(i).getMsg();
            for (String word : message.split(" ")) {
                if (word.length() >= 3 && !excludedWords.contains(word)) {
                    wordArrayList.add(word);
                }
            }
        }
        return mostCommonElement(wordArrayList);
    }

    @Nullable
    private String mostCommonElement(List<String> list) {

        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {

            Integer frequency = map.get(list.get(i));
            if (frequency == null) {
                map.put(list.get(i), 1);
            } else {
                map.put(list.get(i), frequency + 1);
            }
        }

        String mostCommonKey = null;
        int maxValue = -1;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {

            if (entry.getValue() > maxValue) {
                mostCommonKey = entry.getKey();
                maxValue = entry.getValue();
            }
        }
        return mostCommonKey;
    }

    public String mostCommonWordSent() {
        ArrayList<Sms> smsSent = SmsHelper.parseSentSms(smsList);
        ArrayList<String> wordArrayList = new ArrayList<>();

        for (int i = 0; i < smsSent.size(); i++) {
            String message = smsSent.get(i).getMsg();
            for (String word : message.split(" ")) {
                if (word.length() > 3 && !excludedWords.contains(word)) {
                    wordArrayList.add(word);
                }
            }
        }
        return mostCommonElement(wordArrayList);
    }

}