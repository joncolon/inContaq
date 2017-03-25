package nyc.c4q.jonathancolon.inContaq.data.asynctasks;

import android.os.AsyncTask;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.data.WordCount;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;

public class TotalWordCountWorkerTask extends AsyncTask<ArrayList<Sms>, Void, ArrayList<Integer>> {

    private static ArrayList<Integer> getTotalWordCount(ArrayList<Sms> smsList) {
        ArrayList<Integer> wordCount = new ArrayList<>();
        for (int i = 0; i < smsList.size(); i++) {
            wordCount.add(WordCount.getWordCountPerMessage(smsList.get(i)));
        }
        return wordCount;
    }

    @SafeVarargs
    @Override
    protected final ArrayList<Integer> doInBackground(ArrayList<Sms>... params) {
        return getTotalWordCount(params[0]);
    }
}
