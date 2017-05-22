package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.ContactViewPagerActivity;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data.GetHourlyReceived;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data.GetHourlySent;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data.GetMonthlyReceived;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data.GetMonthlySent;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data.GetWeeklyReceived;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data.GetWeeklySent;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data.TimeMostContacted;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data.TreeMapToFloatArray;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data.WordCount;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data.WordFrequency;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.graphs.bargraphs.TotalSmsBarGraph;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.graphs.bargraphs.WordCountBarGraph;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.graphs.linegraphs.fragments.DailyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.graphs.linegraphs.fragments.MonthlyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.graphs.linegraphs.fragments.WeeklyGraphFragment;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.util.AnalyticsFeedback;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.rxbus.RxBus;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.rxbus.RxBusComponent;
import nyc.c4q.jonathancolon.inContaq.utlities.PixelToDp;

import static android.view.View.GONE;


public class ContactStatsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ContactStatsFragment.class.getSimpleName();
    private static final float UNSELECTED_ELEVATION = 100f;
    private static final float SELECTED_ELEVATION = 10f;
    private BarChartView wordAverageChart, totalWordCountChart;
    private ArrayList<Sms> smsList;
    private CompositeDisposable disposables;
    private AnalyticsFeedback analyticsFeedback;
    private TimeMostContacted timeMostContacted;

    private CardView buttonDisplay;
    private ProgressBar progressBar;

    private Contact contact;
    private Context context;

    private CardView totalCard;
    private RelativeLayout textCard;
    private CardView wordCard;
    private CardView weeklyCard;
    private CardView monthlyCard;
    private CardView dailyCard;

    private TextView avgWordSentTV, avgWordReceivedTV, daysSinceContactedTV,
            timeOfFeedbackTv, commonWordReceivedTV, commonWordSentTV,
            getCommonWordSentTextTV, getCommonWordReceivedTextTV, youTV;

    private TreeMap<Integer, Integer>
            hourlyReceivedTreeMap, hourlySentTreeMap,
            monthlyReceivedTreeMap, monthlySentTreeMap,
            weeklyReceivedTreeMap, weeklySentTreeMap;

    private float[] hourlySent, hourlyReceived, monthlySent,
            monthlyReceived, weeklySent, weeklyReceived;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_stats, container, false);

        initViews(view);
        context = getContext();
        timeMostContacted = new TimeMostContacted();
        analyticsFeedback = new AnalyticsFeedback();
        contact = ((ContactViewPagerActivity) getActivity()).contact;

        return view;
    }

    private void initViews(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_stats);
        progressBar.setVisibility(View.VISIBLE);

        avgWordSentTV = (TextView) view.findViewById(R.id.avg_sent_counter_tv);
        avgWordReceivedTV = (TextView) view.findViewById(R.id.avg_received_counter_tv);
        daysSinceContactedTV = (TextView) view.findViewById(R.id.day_counter_tv);
        timeOfFeedbackTv = (TextView) view.findViewById(R.id.time_of_day_feedback_tv);
        commonWordSentTV = (TextView) view.findViewById(R.id.common_sent_word);
        commonWordReceivedTV = (TextView) view.findViewById(R.id.common_received_word);
        getCommonWordReceivedTextTV = (TextView) view.findViewById(R.id.common_received_text);
        getCommonWordSentTextTV = (TextView) view.findViewById(R.id.common_sent_text);
        youTV = (TextView) view.findViewById(R.id.you_tv);

        textCard = (RelativeLayout) view.findViewById(R.id.stat_summary);
        totalCard = (CardView) view.findViewById(R.id.card_total_sms);
        wordCard = (CardView) view.findViewById(R.id.card_word_count);
        dailyCard = (CardView) view.findViewById(R.id.daily_card);
        monthlyCard = (CardView) view.findViewById(R.id.monthly_card);
        weeklyCard = (CardView) view.findViewById(R.id.weekly_card);

        buttonDisplay = (CardView) view.findViewById(R.id.graph_btn);

        monthlyCard.setOnClickListener(this);
        weeklyCard.setOnClickListener(this);
        dailyCard.setOnClickListener(this);

        wordAverageChart = (BarChartView) view.findViewById(R.id.bar_chart_word_average);
        totalWordCountChart = (BarChartView) view.findViewById(R.id.bar_chart_total_sms);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: VISIBLE TO USER");
        RxBusComponent rxBusComponent = ((ContactViewPagerActivity)
                getActivity()).getRxBusComponent();
        rxBusComponent.inject(this);

        RxBus rxBus = rxBusComponent.getRxBus();
        Log.i(TAG, "RxBus memory address: " + rxBus);

        disposables = new CompositeDisposable();

        ConnectableFlowable<Object> eventEmitter = rxBus.asFlowable().publish();
        Log.e(TAG, "RxBus: accept: waiting to receive ");
        disposables.add(eventEmitter
                .subscribe(event -> {
                    if (event instanceof ContactViewPagerActivity.SmsLoaded) {
                        Log.e(TAG, "accept: SMS Loaded ");
                    }
                    if (event instanceof ContactViewPagerActivity.SmsUnavailable) {
                        Log.e(TAG, "accept: SMS Unavailable ");
                        progressBar.setVisibility(GONE);
                    }
                }));

        disposables.add(eventEmitter
                .subscribe(event -> {
                    if (event instanceof ArrayList) {
                        Log.e(TAG, "received: " + ((ArrayList) event).size());
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

    private void populateDataTextViews(long daysSinceLastContacted) {
        WordFrequency wordFrequency = new WordFrequency(smsList);

        getHourlyReceivedTreeMap();
        getHourlySentTreeMap();
        getWeeklyReceivedTreeMap();
        getWeeklySentTreeMap();
        getMonthlyReceivedTreeMap();
        getMonthlySentTreeMap();


        daysSinceContactedTV.setText(String.valueOf(daysSinceLastContacted));
        WordCount.setAvgWordCountSentText(smsList, avgWordSentTV);
        WordCount.setAvgWordCountReceivedText(smsList, avgWordReceivedTV);
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
                    Log.e(TAG, "getHourlyReceivedTreeMap: COMPLETE");

                    timeOfFeedbackTv.setText(analyticsFeedback
                            .timeOfDayFeedback(timeMostContacted
                                    .maxValue(hourlyReceivedTreeMap)));

                    timeOfFeedbackTv.setText(analyticsFeedback
                            .timeOfDayFeedback(timeMostContacted
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
                    Log.e(TAG, "getHourlySentTreeMap: COMPLETE");

                    progressBar.setVisibility(GONE);
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
                    Log.e(TAG, "getWeeklyReceivedTreeMap: COMPLETE");
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
                    Log.e(TAG, "getWeeklySentTreeMap: COMPLETE");
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
                    Log.e(TAG, "getMonthlyReceived: COMPLETE");

                    showMonthlyGraphFragment();
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
                    Log.e(TAG, "getMonthlySentTreeMap: COMPLETE");
                    showMonthlyGraphFragment();
                });
    }

    public float[] convertToFloats(TreeMap<Integer, Integer> treeMap) {
        TreeMapToFloatArray FloatConverter = new TreeMapToFloatArray();
        return FloatConverter.convertTreeMapToFloats(treeMap);
    }

    private void showMonthlyGraphFragment() {
        if (monthlyReceived != null && monthlySent != null) {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.monthly_card:
                if (monthlySent != null && monthlyReceived != null) {
                    showMonthlyGraphFragment();

                    monthlyCard.setCardBackgroundColor(context.getColor(R.color.red_madder));
                    weeklyCard.setCardBackgroundColor(context.getColor(R.color.charcoal));
                    dailyCard.setCardBackgroundColor(context.getColor(R.color.charcoal));

                    monthlyCard.setCardElevation(PixelToDp.convertPixelsToDp(SELECTED_ELEVATION, getContext()));
                    weeklyCard.setCardElevation(PixelToDp.convertPixelsToDp(UNSELECTED_ELEVATION, getContext()));
                    dailyCard.setCardElevation(PixelToDp.convertPixelsToDp(UNSELECTED_ELEVATION, getContext()));

                    timeOfFeedbackTv.setText(analyticsFeedback
                            .monthOfYearFeedback(timeMostContacted
                                    .maxValue(monthlyReceivedTreeMap)));

                    timeOfFeedbackTv.setText(analyticsFeedback
                            .monthOfYearFeedback(timeMostContacted
                                    .maxValue(monthlyReceivedTreeMap)));

                }
                break;
            case R.id.weekly_card:
                if (weeklySent != null && weeklyReceived != null) {
                    showWeeklyGraphFragment();

                    weeklyCard.setCardBackgroundColor(context.getColor(R.color.red_madder));
                    monthlyCard.setCardBackgroundColor(context.getColor(R.color.charcoal));
                    dailyCard.setCardBackgroundColor(context.getColor(R.color.charcoal));

                    weeklyCard.setCardElevation(PixelToDp.convertPixelsToDp(SELECTED_ELEVATION, getContext()));
                    monthlyCard.setCardElevation(PixelToDp.convertPixelsToDp(UNSELECTED_ELEVATION, getContext()));
                    dailyCard.setCardElevation(PixelToDp.convertPixelsToDp(UNSELECTED_ELEVATION, getContext()));


                    timeOfFeedbackTv.setText(analyticsFeedback
                            .dayOfWeekFeedback(timeMostContacted
                                    .maxValue(weeklyReceivedTreeMap)));
                }
                break;
            case R.id.daily_card:
                if (hourlySent != null && hourlyReceived != null) {
                    showHourlyGraphFragment();

                    dailyCard.setCardBackgroundColor(context.getColor(R.color.red_madder));
                    weeklyCard.setCardBackgroundColor(context.getColor(R.color.charcoal));
                    monthlyCard.setCardBackgroundColor(context.getColor(R.color.charcoal));

                    dailyCard.setCardElevation(PixelToDp.convertPixelsToDp(SELECTED_ELEVATION, getContext()));
                    weeklyCard.setCardElevation(PixelToDp.convertPixelsToDp(UNSELECTED_ELEVATION, getContext()));
                    monthlyCard.setCardElevation(PixelToDp.convertPixelsToDp(UNSELECTED_ELEVATION, getContext()));

                    timeOfFeedbackTv.setText(analyticsFeedback
                            .timeOfDayFeedback(timeMostContacted
                                    .maxValue(hourlyReceivedTreeMap)));

                    timeOfFeedbackTv.setText(analyticsFeedback
                            .timeOfDayFeedback(timeMostContacted
                                    .maxValue(hourlyReceivedTreeMap)));
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
        Log.d(TAG, "onStop: STOPPED");
    }

    @Override
    public void onStop() {
        super.onStop();
        disposables.clear();
        Log.d(TAG, "onStop: STOPPED");
    }
}



