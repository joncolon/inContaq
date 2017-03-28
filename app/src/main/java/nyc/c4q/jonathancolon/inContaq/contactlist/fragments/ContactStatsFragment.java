package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.db.chart.view.BarChartView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.data.SmsAnalytics;
import nyc.c4q.jonathancolon.inContaq.data.WordCount;
import nyc.c4q.jonathancolon.inContaq.data.WordFrequency;
import nyc.c4q.jonathancolon.inContaq.graphs.bargraphs.TotalSmsBarGraph;
import nyc.c4q.jonathancolon.inContaq.graphs.bargraphs.WordCountBarGraph;
import nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.fragments.DailyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.fragments.MonthlyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.fragments.WeeklyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;

import static nyc.c4q.jonathancolon.inContaq.data.AnalyticsFeedback.timeFeedback;
import static nyc.c4q.jonathancolon.inContaq.data.WordFrequency.mostCommonWordReceived;
import static nyc.c4q.jonathancolon.inContaq.data.WordFrequency.mostCommonWordSent;


public class ContactStatsFragment extends Fragment implements View.OnClickListener {

    int averageWordCountSent;
    int averageWordCountReceived;
    private TextView avgWordSentTV, avgWordReceivedTV, daysSinceContactedTV, timeOfFeedbackTv,
            commonWordReceivedTV, commonWordSentTV;
    private BarChartView wordAverageChart, totalWordCountChart;
    private ArrayList<Sms> smsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_stats, container, false);
        initViews(view);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            smsList = Parcels.unwrap(bundle.getParcelable("smslist"));
        }

        if (smsList != null) {

            if (smsList.size() != 0) {

                if (savedInstanceState == null) {
                    showMonthlyGraphFragment();
                }

                getAverageWordCount(smsList);
                long daysSinceLastContacted = getDifferenceDays();
                SmsAnalytics smsAnalytics = new SmsAnalytics(smsList);
                WordFrequency wordFrequency = new WordFrequency(smsList);

                daysSinceContactedTV.setText(String.valueOf(daysSinceLastContacted));
                avgWordSentTV.setText(String.valueOf(averageWordCountSent));
                avgWordReceivedTV.setText(String.valueOf(averageWordCountReceived));
                timeOfFeedbackTv.setText(timeFeedback(smsAnalytics.maxTimeReceivedText()));
                commonWordReceivedTV.setText(mostCommonWordReceived());
                commonWordSentTV.setText(mostCommonWordSent());

                WordCountBarGraph wordCountBarGraph = new WordCountBarGraph(wordAverageChart, smsList);
                TotalSmsBarGraph totalSmsBarGraph = new TotalSmsBarGraph(totalWordCountChart, smsList);
                wordCountBarGraph.showBarGraph();
                totalSmsBarGraph.showBarGraph();
            }
        }

        return view;
    }

    private void initViews(View view) {
        avgWordSentTV = (TextView) view.findViewById(R.id.avg_sent_counter_tv);
        avgWordReceivedTV = (TextView) view.findViewById(R.id.avg_received_counter_tv);
        daysSinceContactedTV = (TextView) view.findViewById(R.id.day_counter_tv);
        timeOfFeedbackTv = (TextView) view.findViewById(R.id.time_of_day_feedback_tv);
        commonWordSentTV = (TextView) view.findViewById(R.id.common_sent_word);
        commonWordReceivedTV = (TextView) view.findViewById(R.id.common_received_word);
        Button monthlyBtn = (Button) view.findViewById(R.id.monthly_btn);
        Button weeklyBtn = (Button) view.findViewById(R.id.weekly_btn);
        Button dailyBtn = (Button) view.findViewById(R.id.daily_btn);
        monthlyBtn.setOnClickListener(this);
        weeklyBtn.setOnClickListener(this);
        dailyBtn.setOnClickListener(this);
        wordAverageChart = (BarChartView) view.findViewById(R.id.bar_chart_word_average);
        totalWordCountChart = (BarChartView) view.findViewById(R.id.bar_chart_total_sms);
    }

    private void showMonthlyGraphFragment() {
        Fragment monthlyGraphFragment = new MonthlyGraphFragment();
        Bundle monthlyGraphBundle = new Bundle();
        monthlyGraphBundle.putParcelable("smslist", Parcels.wrap(smsList));
        monthlyGraphFragment.setArguments(monthlyGraphBundle);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_frag_container, monthlyGraphFragment)
                .commit();
    }

    private void getAverageWordCount(ArrayList<Sms> smsList) {
        averageWordCountSent = WordCount.getAverageWordCountSent(smsList);
        averageWordCountReceived = WordCount.getAverageWordCountReceived(smsList);
    }

    private long getDifferenceDays() {
        Date date1 = new Date((Long.valueOf(smsList.get(smsList.size() - 1).getTime())));
        Date date2 = new Date(System.currentTimeMillis());
        long difference = date2.getTime() - date1.getTime();
        return difference / (1000 * 60 * 60 * 24);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.monthly_btn:
                showMonthlyGraphFragment();
                break;
            case R.id.weekly_btn:
                showWeeklyGraphFragment();
                break;
            case R.id.daily_btn:
                showDailyGraphFragment();
                break;
        }
    }

    private void showWeeklyGraphFragment() {
        Fragment weeklyGraphFragment = new WeeklyGraphFragment();
        Bundle weeklyGraphBundle = new Bundle();
        weeklyGraphBundle.putParcelable("smslist", Parcels.wrap(smsList));
        weeklyGraphFragment.setArguments(weeklyGraphBundle);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_frag_container, weeklyGraphFragment)
                .commit();
    }

    private void showDailyGraphFragment() {
        Fragment dailyGraphFragment = new DailyGraphFragment();
        Bundle dailyGraphBundle = new Bundle();
        dailyGraphBundle.putParcelable("smslist", Parcels.wrap(smsList));
        dailyGraphFragment.setArguments(dailyGraphBundle);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_frag_container, dailyGraphFragment)
                .commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
