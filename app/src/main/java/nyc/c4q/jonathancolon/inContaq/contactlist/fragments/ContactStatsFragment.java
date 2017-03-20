package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;
import nyc.c4q.jonathancolon.inContaq.data.AnalyticsFeedback;
import nyc.c4q.jonathancolon.inContaq.data.SmsAnalytics;
import nyc.c4q.jonathancolon.inContaq.data.WordCount;
import nyc.c4q.jonathancolon.inContaq.data.WordFrequency;
import nyc.c4q.jonathancolon.inContaq.graphs.bargraphs.TotalSmsBarGraph;
import nyc.c4q.jonathancolon.inContaq.graphs.bargraphs.WordCountBarGraph;
import nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.fragments.DailyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.fragments.MonthlyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.fragments.WeeklyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.SmsHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;


public class ContactStatsFragment extends Fragment implements View.OnClickListener {


    private Button monthlyBtn, weeklyBtn, dailyBtn;
    private TextView avgWordSentTV, avgWordsReceivedTV, daysSinceContactedTV, getAvgWordSentInfoTV,
            getGetAvgWordReceivedInfoTV, getDaysSinceContactedInfoTV, timeOfFeedbackTv,
            commonWordSentTv, commonWordReceivedTv;
    private ArrayList<Sms> smsList;
    private BarChartView wordAverageChart, totalWordCountChart;
    int averageWordCountSent;
    int averageWordCountReceived;

    public static ContactStatsFragment newInstance() {
        ContactStatsFragment fragment = new ContactStatsFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_stats, container, false);
        initViews(view);
        if (savedInstanceState == null) {
            showMonthlyGraphFragment();
        }

        smsList = SmsHelper.getAllSms(getContext(), unwrapParcelledContact());
        getAxisValues(smsList);

        if (smsList.size() != 0){
            Date date1 = new Date((Long.valueOf(smsList.get(0).getTime())));
            Date date2 = new Date(System.currentTimeMillis());
            long difference = date2.getTime() - date1.getTime();
            long differenceDays = difference / (1000 * 60 * 60 * 24);

            SmsAnalytics smsAnalytics = new SmsAnalytics(smsList);
            WordFrequency wordFrequency = new WordFrequency(smsList);
            AnalyticsFeedback analyticsFeedback = new AnalyticsFeedback();

            String wordReceivedText = String.valueOf(averageWordCountReceived);
            String wordSentText = String.valueOf(averageWordCountSent);
            daysSinceContactedTV.setText(String.valueOf(differenceDays));
            avgWordSentTV.setText(wordSentText);
//            avgWordsReceivedTV.setText(wordReceivedText);
            Log.d("WEIRD ERROR", wordReceivedText);
            timeOfFeedbackTv.setText(analyticsFeedback.timeFeedback(smsAnalytics.maxTimeReceivedText()));
            commonWordSentTv.setText(wordFrequency.mostCommonWordSent());
            commonWordReceivedTv.setText(wordFrequency.mostCommonWordReceived());

            Log.d("Common word received: ", wordFrequency.mostCommonWordReceived());
            Log.e("Common word sent: ", wordFrequency.mostCommonWordSent());
            Log.d("TIME: ", smsAnalytics.maxTimeReceivedText());
            Log.d("TIME: ", smsAnalytics.maxTimeSentText());
        }

        WordCountBarGraph wordCountBarGraph = new WordCountBarGraph(wordAverageChart, smsList);
        TotalSmsBarGraph totalSmsBarGraph = new TotalSmsBarGraph(totalWordCountChart, smsList);
        wordCountBarGraph.showBarGraph();
        totalSmsBarGraph.showBarGraph();

        return view;
    }
    private void getAxisValues(ArrayList<Sms> smsList) {
        averageWordCountSent = WordCount.getAverageWordCountSent(smsList);
        averageWordCountReceived = WordCount.getAverageWordCountReceived(smsList);
    }

    private void showMonthlyGraphFragment() {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_frag_container, new MonthlyGraphFragment())
                .commit();
    }

    private void showWeeklyGraphFragment() {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_frag_container, new WeeklyGraphFragment())
                .commit();
    }

    private void showDailyGraphFragment() {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_frag_container, new DailyGraphFragment())
                .commit();
    }

    private void initViews(View view) {
        avgWordSentTV = (TextView) view.findViewById(R.id.avg_sent_counter_tv);
        avgWordsReceivedTV = (TextView) view.findViewById(R.id.avg_received_counter_tv);
        daysSinceContactedTV = (TextView) view.findViewById(R.id.day_counter_tv);
        getAvgWordSentInfoTV = (TextView) view.findViewById(R.id.avg_msg_length_sent_info_tv);
        timeOfFeedbackTv = (TextView) view.findViewById(R.id.time_of_day_feedback_tv);
        commonWordReceivedTv = (TextView) view.findViewById(R.id.common_word_received);
        commonWordSentTv = (TextView) view.findViewById(R.id.common_word_sent);
        monthlyBtn = (Button) view.findViewById(R.id.monthly_btn);
        weeklyBtn = (Button) view.findViewById(R.id.weekly_btn);
        dailyBtn = (Button) view.findViewById(R.id.daily_btn);
        monthlyBtn.setOnClickListener(this);
        weeklyBtn.setOnClickListener(this);
        dailyBtn.setOnClickListener(this);
        wordAverageChart = (BarChartView) view.findViewById(R.id.bar_chart_word_average);
        totalWordCountChart = (BarChartView) view.findViewById(R.id.bar_chart_total_sms);
    }

    @Nullable
    private Contact unwrapParcelledContact() {
        return Parcels.unwrap(getActivity().getIntent().getParcelableExtra(ContactListActivity.PARCELLED_CONTACT));
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


}
