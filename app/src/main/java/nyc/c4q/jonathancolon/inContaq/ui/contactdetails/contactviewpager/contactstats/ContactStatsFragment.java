package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.db.chart.view.BarChartView;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.schedulers.Schedulers;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.di.RxBus;
import nyc.c4q.jonathancolon.inContaq.di.RxBusComponent;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.ContactViewPagerActivity;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.graphs.bargraphs.TotalSmsBarGraph;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.graphs.bargraphs.WordCountBarGraph;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.graphs.linegraphs.fragments.DailyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.graphs.linegraphs.fragments.MonthlyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.graphs.linegraphs.fragments.WeeklyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.util.AnalyticsFeedback;


public class ContactStatsFragment extends Fragment implements View.OnClickListener {

    int averageWordCountSent;
    int averageWordCountReceived;
    private ContactViewPagerActivity parentActivity;
    private TextView avgWordSentTV, avgWordReceivedTV, daysSinceContactedTV, timeOfFeedbackTv,
            commonWordReceivedTV, commonWordSentTV, getCommonWordSentTextTV,
            getCommonWordReceivedTextTV, youTV;
    private BarChartView wordAverageChart, totalWordCountChart;
    private ArrayList<Sms> smsList;
    private TreeMap<Integer, Integer> hourlyReceivedTreeMap, hourlySentTreeMap,
            monthlyReceivedTreeMap, monthlySentTreeMap, weeklyReceivedTreeMap, weeklySentTreeMap;
    private Contact contact;
    private float[] hourlySent, hourlyReceived, monthlySent, monthlyReceived, weeklySent,
            weeklyReceived;
    private RxBusComponent rxBusComponent;
    private RxBus rxBus;
    private CompositeDisposable disposables;
    private CardView totalCard, textCard, wordCard;
    private RelativeLayout buttonDisplay;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_stats, container, false);
        initViews(view);

        parentActivity = (ContactViewPagerActivity) getActivity();
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
        textCard = (CardView) view.findViewById(R.id.card_text_stats);
        totalCard = (CardView) view.findViewById(R.id.card_total_sms);
        wordCard = (CardView) view.findViewById(R.id.card_word_count);
        buttonDisplay = (RelativeLayout) view.findViewById(R.id.graph_btn);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_stats);
        progressBar.setVisibility(View.VISIBLE);


        Button monthlyBtn = (Button) view.findViewById(R.id.monthly_btn);
        Button weeklyBtn = (Button) view.findViewById(R.id.weekly_btn);
        Button dailyBtn = (Button) view.findViewById(R.id.daily_btn);

        monthlyBtn.setOnClickListener(this);
        weeklyBtn.setOnClickListener(this);
        dailyBtn.setOnClickListener(this);

        wordAverageChart = (BarChartView) view.findViewById(R.id.bar_chart_word_average);
        totalWordCountChart = (BarChartView) view.findViewById(R.id.bar_chart_total_sms);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("ON START", "onStart: VISIBLE TO USER");
        rxBusComponent = ((ContactViewPagerActivity) getActivity()).getRxBusComponent();
        rxBusComponent.inject(this);

        rxBus = rxBusComponent.getRxBus();
        Log.i("Dagger: ", "StatsFrag rxBus: " + rxBus);

        disposables = new CompositeDisposable();

        ConnectableFlowable<Object> eventEmitter = rxBus.asFlowable().publish();
        Log.e("RxBus: ", "accept: waiting to receive ");
        disposables.add(eventEmitter
                .subscribe(event -> {
                    if (event instanceof ContactViewPagerActivity.SmsLoaded) {
                        Log.e("RxBus: ", "accept: event received ");
                    }
                }));

        disposables.add(eventEmitter
                .subscribe(event -> {
                    if (event instanceof ArrayList) {
                        String string = ((ArrayList) event).get(1).getClass().toString();
                        Log.e("RxBus: ", "received: " + string);
                        smsList = ((ArrayList<Sms>) event);
                        displayStats();
                    }
                }));
        disposables.add(eventEmitter.connect());
    }

    public void displayStats() {
        String phoneNumber = contact.getMobileNumber();
        if (phoneNumber != null) {

            if (smsList != null) {
                if (smsList.size() != 0) {
                    long daysSinceLastContacted = getDifferenceDays();

                    enableTextViewVisibility();
                    getAverageWordCount(smsList);
                    populateDataTextViews(daysSinceLastContacted);
                    loadBarGraphs();
                }
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

    private void getAverageWordCount(ArrayList<Sms> smsList) {
        averageWordCountSent = WordCount.getAverageWordCountSent(smsList);
        averageWordCountReceived = WordCount.getAverageWordCountReceived(smsList);
    }

    private void populateDataTextViews(long daysSinceLastContacted) {
        WordFrequency wordFrequency = new WordFrequency(smsList);

        getHourlyReceivedTreeMap();
        getHourlySentTreeMap();
        getWeeklyReceivedTreeMap();
        getWeeklySentTreeMap();
        getMonthlyReceivedTreeMap();
        getMonthlySentTreeMap();


        daysSinceContactedTV.setText(String.valueOf(daysSinceLastContacted));
        avgWordSentTV.setText(String.valueOf(averageWordCountSent));
        avgWordReceivedTV.setText(String.valueOf(averageWordCountReceived));
        commonWordReceivedTV.setText(wordFrequency.mostCommonWordReceived());
        commonWordSentTV.setText(wordFrequency.mostCommonWordSent());

    }

    private void loadBarGraphs() {
        WordCountBarGraph wordCountBarGraph = new WordCountBarGraph(wordAverageChart, smsList);
        TotalSmsBarGraph totalSmsBarGraph = new TotalSmsBarGraph(totalWordCountChart, smsList);
        wordCountBarGraph.showBarGraph();
        totalSmsBarGraph.showBarGraph();
    }

    public void getHourlyReceivedTreeMap() {
        Observable.fromCallable(() -> {
            GetHourlyReceived callable = new GetHourlyReceived(smsList);
            return callable.getHourlyReceived();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(treeMap -> {
                    hourlyReceivedTreeMap = treeMap;
                    hourlyReceived = convertToFloats(hourlyReceivedTreeMap);
                    Log.e("FloatConvert", "getHourlyReceivedTreeMap: COMPLETE");

                    AnalyticsFeedback analyticsFeedback = new AnalyticsFeedback();
                    TimeMostContacted timeMostContacted = new TimeMostContacted();
                    timeOfFeedbackTv.setText(analyticsFeedback
                            .timeFeedback(timeMostContacted
                                    .maxValue(hourlyReceivedTreeMap)));
                });
    }

    public void getHourlySentTreeMap() {
        Observable.fromCallable(() -> {
            GetHourlySent callable = new GetHourlySent(smsList);
            return callable.getHourlySent();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(treeMap -> {
                    hourlySentTreeMap = treeMap;
                    hourlySent = convertToFloats(hourlySentTreeMap);
                    Log.e("FloatConvert", "getHourlySentTreeMap: COMPLETE");

                    progressBar.setVisibility(View.GONE);
                    textCard.setVisibility(View.VISIBLE);
                    totalCard.setVisibility(View.VISIBLE);
                    wordCard.setVisibility(View.VISIBLE);
                    buttonDisplay.setVisibility(View.VISIBLE);
                });
    }

    public void getWeeklyReceivedTreeMap() {
        Observable.fromCallable(() -> {
            GetWeeklyReceived callable = new GetWeeklyReceived(smsList);
            return callable.getWeeklyReceived();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(treeMap -> {
                    weeklyReceivedTreeMap = treeMap;
                    weeklyReceived = convertToFloats(weeklyReceivedTreeMap);
                    Log.e("FloatConvert", "getWeeklyReceivedTreeMap: COMPLETE");
                });
    }

    public void getWeeklySentTreeMap() {
        Observable.fromCallable(() -> {
            GetWeeklySent callable = new GetWeeklySent(smsList);
            return callable.getWeeklySent();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(treeMap -> {
                    weeklySentTreeMap = treeMap;
                    weeklySent = convertToFloats(weeklySentTreeMap);
                    Log.e("FloatConvert", "getWeeklySentTreeMap: COMPLETE");
                });
    }

    public void getMonthlyReceivedTreeMap() {
        Observable.fromCallable(() -> {
            GetMonthlyReceived callable = new GetMonthlyReceived(smsList);
            return callable.getMonthlyReceived();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(treeMap -> {
                    monthlyReceivedTreeMap = treeMap;
                    monthlyReceived = convertToFloats(monthlyReceivedTreeMap);
                    Log.e("FloatConvert", "getMonthlyReceived: COMPLETE");

                    if (monthlyReceived != null && monthlySent != null) {
                        showMonthlyGraphFragment();
                    }
                });
    }

    public void getMonthlySentTreeMap() {
        Observable.fromCallable(() -> {
            GetMonthlySent callable = new GetMonthlySent(smsList);
            return callable.getMonthlySent();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(treeMap -> {
                    monthlySentTreeMap = treeMap;
                    monthlySent = convertToFloats(monthlySentTreeMap);
                    Log.e("FloatConvert", "getMonthlySentTreeMap: COMPLETE");

                    if (monthlyReceived != null && monthlySent != null) {
                        showMonthlyGraphFragment();
                    }
                });
    }

    public float[] convertToFloats(TreeMap<Integer, Integer> treeMap) {
        TreeMapToFloatArray FloatConverter = new TreeMapToFloatArray();
        return FloatConverter.convertTreeMapToFloats(treeMap);
    }

    private void showMonthlyGraphFragment() {
        Fragment monthlyGraphFragment = new MonthlyGraphFragment();
        Bundle monthlyGraphBundle = new Bundle();
        monthlyGraphBundle.putFloatArray("monthlyReceived", monthlyReceived);
        monthlyGraphBundle.putFloatArray("monthlySent", monthlySent);
        monthlyGraphFragment.setArguments(monthlyGraphBundle);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_frag_container, monthlyGraphFragment)
                .commit();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.monthly_btn:
                if (monthlySent != null && monthlyReceived != null) {
                    showMonthlyGraphFragment();
                }
                break;
            case R.id.weekly_btn:
                if (weeklySent != null && weeklyReceived != null) {
                    showWeeklyGraphFragment();
                }
                break;
            case R.id.daily_btn:
                if (hourlySent != null && hourlyReceived != null) {
                    showHourlyGraphFragment();
                }
                break;
        }
    }

    private void showWeeklyGraphFragment() {
        Fragment weeklyGraphFragment = new WeeklyGraphFragment();
        Bundle weeklyGraphBundle = new Bundle();
        weeklyGraphBundle.putFloatArray("weeklyReceived", weeklyReceived);
        weeklyGraphBundle.putFloatArray("weeklySent", weeklySent);
        weeklyGraphFragment.setArguments(weeklyGraphBundle);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_frag_container, weeklyGraphFragment)
                .commit();
    }

    private void showHourlyGraphFragment() {
        Fragment dailyGraphFragment = new DailyGraphFragment();
        Bundle dailyGraphBundle = new Bundle();
        dailyGraphBundle.putFloatArray("hourlyReceived", hourlyReceived);
        dailyGraphBundle.putFloatArray("hourlySent", hourlySent);
        dailyGraphFragment.setArguments(dailyGraphBundle);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_frag_container, dailyGraphFragment)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        disposables.clear();
        Log.d("STATS FRAG: ", "onStop: STOPPED");
    }
}


