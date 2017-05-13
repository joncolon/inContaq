package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.db.chart.view.BarChartView;

import java.util.Date;
import java.util.TreeMap;

import io.realm.Realm;
import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactViewPagerActivity;
import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;
import nyc.c4q.jonathancolon.inContaq.data.AnalyticsFeedback;
import nyc.c4q.jonathancolon.inContaq.data.TimeMostContacted;
import nyc.c4q.jonathancolon.inContaq.data.TreeMapToFloatArray;
import nyc.c4q.jonathancolon.inContaq.data.WordCount;
import nyc.c4q.jonathancolon.inContaq.data.WordFrequency;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.HourlyReceivedTask;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.HourlySentTask;
import nyc.c4q.jonathancolon.inContaq.data.asynctasks.OnStatsLoaded;
import nyc.c4q.jonathancolon.inContaq.graphs.bargraphs.TotalSmsBarGraph;
import nyc.c4q.jonathancolon.inContaq.graphs.bargraphs.WordCountBarGraph;
import nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.fragments.DailyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.fragments.MonthlyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.graphs.linegraphs.fragments.WeeklyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.realm.RealmHelper;
import nyc.c4q.jonathancolon.inContaq.refactorcode.RxBus;
import nyc.c4q.jonathancolon.inContaq.refactorcode.RxBusComponent;
import nyc.c4q.jonathancolon.inContaq.sms.model.Sms;

import static nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity.CONTACT_ID;


public class ContactStatsFragment extends Fragment implements View.OnClickListener, OnStatsLoaded {

    int averageWordCountSent;
    int averageWordCountReceived;
    private ContactViewPagerActivity parentActivity;
    private TextView avgWordSentTV, avgWordReceivedTV, daysSinceContactedTV, timeOfFeedbackTv,
            commonWordReceivedTV, commonWordSentTV, getCommonWordSentTextTV,
            getCommonWordReceivedTextTV, youTV;
    private BarChartView wordAverageChart, totalWordCountChart;
    private RealmResults<Sms> smsList;
    private Realm realm;
    private long contactId;
    private TreeMap<Integer, Integer> hourlyReceivedTreeMap, hourlySentTreeMap;
    private ContactStatsFragment statsFrag;
    private Contact contact;
    public boolean hourlySentLoaded;
    public boolean hourlyReceivedLoaded;
    private float[] hourlySent;
    private float[] hourlyReceived;
    private RxBusComponent rxBusComponent;
    private RxBus rxBus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_stats, container, false);
        initViews(view);

        rxBusComponent = ((ContactViewPagerActivity) getActivity()).getRxBusComponent();
        rxBusComponent.inject(this);

        rxBus = rxBusComponent.getRxBus();
        Log.i("Dagger: ", "StatsFrag rxBus: " + rxBus);

        statsFrag = this;
        parentActivity = (ContactViewPagerActivity) getActivity();
        contactId = getActivity().getIntent().getLongExtra(CONTACT_ID, -1);
        contact = parentActivity.contact;

        return view;
    }

    private void initViews(View view) {
        avgWordSentTV = (TextView) view.findViewById(R.id.avg_sent_counter_tv);
        avgWordReceivedTV = (TextView) view.findViewById(R.id.avg_received_counter_tv);
        daysSinceContactedTV = (TextView) view.findViewById(R.id.day_counter_tv);
        timeOfFeedbackTv = (TextView) view.findViewById(R.id.time_of_day_feedback_tv);
        commonWordSentTV = (TextView) view.findViewById(R.id.common_sent_word);
        commonWordReceivedTV = (TextView) view.findViewById(R.id.common_received_word);
        getCommonWordReceivedTextTV = (TextView) view.findViewById(R.id.common_received_text);
        getCommonWordSentTextTV = (TextView) view.findViewById(R.id.common_sent_text);
        youTV = (TextView) view.findViewById(R.id.you_tv);

        Button monthlyBtn = (Button) view.findViewById(R.id.monthly_btn);
        Button weeklyBtn = (Button) view.findViewById(R.id.weekly_btn);
        Button dailyBtn = (Button) view.findViewById(R.id.daily_btn);

        monthlyBtn.setOnClickListener(this);
        weeklyBtn.setOnClickListener(this);
        dailyBtn.setOnClickListener(this);

        wordAverageChart = (BarChartView) view.findViewById(R.id.bar_chart_word_average);
        totalWordCountChart = (BarChartView) view.findViewById(R.id.bar_chart_total_sms);
    }

    public void displayStats() {
        realm = Realm.getDefaultInstance();
        contact = RealmHelper.getByRealmID(realm, contactId);
        String phoneNumber = contact.getMobileNumber();
        if (phoneNumber != null) {
            smsList = RealmHelper.getByMobileNumber(realm, contact.getMobileNumber());
        }
        if (smsList != null) {

            if (smsList.size() != 0) {
//                showMonthlyGraphFragment(); // TODO: 5/9/17 Enable
                long daysSinceLastContacted = getDifferenceDays();

                enableTextViewVisibility();
                getAverageWordCount(smsList);
                populateDataTextViews(daysSinceLastContacted);
                loadBarGraphs();
            }
        }
    }

    private long getDifferenceDays() {
        Date date1 = new Date((Long.valueOf(smsList.get(smsList.size() - 1).getTime())));
        Date date2 = new Date(System.currentTimeMillis());
        long difference = date2.getTime() - date1.getTime();
        return difference / (1000 * 60 * 60 * 24);
    }

    private void enableTextViewVisibility() {
        getCommonWordSentTextTV.setVisibility(View.VISIBLE);
        getCommonWordReceivedTextTV.setVisibility(View.VISIBLE);
        youTV.setVisibility(View.VISIBLE);
        timeOfFeedbackTv.setVisibility(View.VISIBLE);
        commonWordReceivedTV.setVisibility(View.VISIBLE);
        commonWordSentTV.setVisibility(View.VISIBLE);
    }

    private void getAverageWordCount(RealmResults<Sms> smsList) {
        averageWordCountSent = WordCount.getAverageWordCountSent(smsList);
        averageWordCountReceived = WordCount.getAverageWordCountReceived(smsList);
    }

    private void populateDataTextViews(long daysSinceLastContacted) {
        WordFrequency wordFrequency = new WordFrequency(smsList);
        AnalyticsFeedback analyticsFeedback = new AnalyticsFeedback();
        TimeMostContacted timeMostContacted = new TimeMostContacted();

        daysSinceContactedTV.setText(String.valueOf(daysSinceLastContacted));
        avgWordSentTV.setText(String.valueOf(averageWordCountSent));
        avgWordReceivedTV.setText(String.valueOf(averageWordCountReceived));
        timeOfFeedbackTv.setText(analyticsFeedback.timeFeedback(timeMostContacted.maxValue(hourlyReceivedTreeMap)));

        commonWordReceivedTV.setText(wordFrequency.mostCommonWordReceived());
        commonWordSentTV.setText(wordFrequency.mostCommonWordSent());
    }

    private void loadBarGraphs() {
        WordCountBarGraph wordCountBarGraph = new WordCountBarGraph(wordAverageChart, smsList);
        TotalSmsBarGraph totalSmsBarGraph = new TotalSmsBarGraph(totalWordCountChart, smsList);
        wordCountBarGraph.showBarGraph();
        totalSmsBarGraph.showBarGraph();
    }

    private void showMonthlyGraphFragment() {
        Fragment monthlyGraphFragment = new MonthlyGraphFragment();

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_frag_container, monthlyGraphFragment)
                .commit();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.monthly_btn:
//                showMonthlyGraphFragment();
                break;
            case R.id.weekly_btn:
//                showWeeklyGraphFragment();
                break;
            case R.id.daily_btn:
                showDailyGraphFragment();
                break;
        }
    }

    private void showDailyGraphFragment() {
        Fragment dailyGraphFragment = new DailyGraphFragment();
        Bundle dailyGraphBundle = new Bundle();
        dailyGraphBundle.putFloatArray("hourlySent", hourlySent);
        dailyGraphBundle.putFloatArray("hourlyReceived", hourlyReceived);
        dailyGraphFragment.setArguments(dailyGraphBundle);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_frag_container, dailyGraphFragment)
                .commit();
    }

    private void showWeeklyGraphFragment() {
        Fragment weeklyGraphFragment = new WeeklyGraphFragment();

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_frag_container, weeklyGraphFragment)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RealmHelper.closeRealm(realm);
    }

    public void setHourlyReceivedTreeMap(TreeMap<Integer, Integer> treeMap) {
        hourlyReceivedTreeMap = treeMap;
        hourlyReceived = convertToFloats(hourlyReceivedTreeMap);
    }

    public void setHourlySentTreeMap(TreeMap<Integer, Integer> treeMap) {
        hourlySentTreeMap = treeMap;
        hourlySent = convertToFloats(hourlySentTreeMap);
    }

    @Override
    public void onStatsLoaded() {
        if (hourlyReceivedLoaded && hourlySentLoaded){
            displayStats();
        }
    }

    private void calculateHourlyReceived(Contact contact) {
        HourlyReceivedTask hourlyReceivedTask = new HourlyReceivedTask(statsFrag, this);
        hourlyReceivedTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, contact.getMobileNumber());
    }

    private void calculateHourlySent(Contact contact) {
        HourlySentTask dailySentWorkTask = new HourlySentTask(statsFrag, this);
        dailySentWorkTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                contact.getMobileNumber());
    }

    public float[] convertToFloats(TreeMap<Integer, Integer> treeMap){
        TreeMapToFloatArray FloatConverter = new TreeMapToFloatArray();
        return FloatConverter.convertTreeMapToFloats(treeMap);
    }
}


//        monthlySent = smsAnalytics.getMonthlySent();
//        monthlyReceived =smsAnalytics.getMonthlyReceived();
//        monthlyReceivedTreeMap = smsAnalytics.getMonthlyReceivedTreeMap();
//        monthlySentTreeMap = smsAnalytics.getMonthlySentTreeMap();


