package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats;

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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.schedulers.Schedulers;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.ContactDetailsActivity;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data.GetHourlyReceived;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data.GetHourlySent;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data.GetMonthlyReceived;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data.GetMonthlySent;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data.GetWeeklyReceived;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data.GetWeeklySent;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data.TimeMostContacted;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data.TreeMapToFloatArray;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data.WordCount;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data.WordFrequency;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.barcharts.TotalSmsBarChart;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.barcharts.AverageWordCountBarChart;
import nyc.c4q.jonathancolon.inContaq.utlities.AnalyticsFeedback;
import nyc.c4q.jonathancolon.inContaq.utlities.RxBus;

import static android.view.View.GONE;
import static nyc.c4q.jonathancolon.inContaq.common.dagger.Injector.getApplicationComponent;
import static nyc.c4q.jonathancolon.inContaq.common.dagger.Injector.getRxBus;
import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isNull;
import static nyc.c4q.jonathancolon.inContaq.utlities.PixelToDp.convertPixelsToDp;


public class ContactStatsFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.avg_sent_counter_tv) TextView avgWordSentTV;
    @BindView(R.id.avg_received_counter_tv) TextView avgWordReceivedTV;
    @BindView(R.id.day_counter_tv) TextView daysSinceContactedTV;
    @BindView(R.id.time_of_day_feedback_tv) TextView feedbackTV;
    @BindView(R.id.common_sent_word) TextView commonWordSentTV;
    @BindView(R.id.common_received_word) TextView commonWordReceivedTV;
    @BindView(R.id.common_sent_text) TextView commonWordSentTextTV;
    @BindView(R.id.common_received_text) TextView commonReceivedWordTextTV;
    @BindView(R.id.you_tv) TextView youTV;
    @BindView(R.id.no_data_tv) TextView noDataTV;
    @BindView(R.id.bar_chart_word_average) BarChartView wordAverageChart;
    @BindView(R.id.bar_chart_total_sms) BarChartView totalWordCountChart;
    @BindView(R.id.stat_summary) RelativeLayout statSummary;
    @BindView(R.id.card_total_sms) CardView totalCard;
    @BindView(R.id.card_word_count) CardView wordCard;
    @BindView(R.id.hourly_card) CardView hourlyCard;
    @BindView(R.id.weekly_card) CardView weeklyCard;
    @BindView(R.id.monthly_card) CardView monthlyCard;
    @BindView(R.id.button_bar_card) CardView buttonBarCard;
    @BindView(R.id.progress_bar_stats) ProgressBar progressBar;

    @Inject WordCount wordCount;
    @Inject WordFrequency wordFrequency;
    @Inject AnalyticsFeedback analyticsFeedback;

    private ArrayList<Sms> smsList;
    private CompositeDisposable disposables;
    private TimeMostContacted timeMostContacted;
    private Context context;

    private Contact contact;
    private static final String TAG = ContactStatsFragment.class.getSimpleName();
    private static final float INACTIVE_ELEVATION = 70f;

    private static final float ACTIVE_ELEVATION = 0f;

    private TreeMap<Integer, Integer>
    hourlyReceivedTreeMap, hourlySentTreeMap,
    monthlyReceivedTreeMap, monthlySentTreeMap,
    weeklyReceivedTreeMap, weeklySentTreeMap;

    private float[]
    hourlySent, hourlyReceived,
    monthlySent, monthlyReceived,
    weeklySent, weeklyReceived;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_stats, container, false);
        unbinder = ButterKnife.bind(this, view);

        initProgressBar();
        context = getContext();
        timeMostContacted = new TimeMostContacted();
        contact = ((ContactDetailsActivity) getActivity()).contact;

        return view;
    }

    private void initProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: VISIBLE TO USER");

        getApplicationComponent().inject(this);
        RxBus rxBus = getRxBus();
        Log.i(TAG, "RxBus memory address: " + rxBus);

        disposables = new CompositeDisposable();

        ConnectableFlowable<Object> eventEmitter = rxBus.asFlowable().publish();
        Log.e(TAG, "RxBus: accept: waiting to receive ");
        disposables.add(eventEmitter
                .subscribe(event -> {
                    if (event instanceof ContactDetailsActivity.SmsLoaded) {
                        Log.e(TAG, "accept: SMS Loaded ");
                    }
                    if (event instanceof ContactDetailsActivity.SmsUnavailable) {
                        Log.e(TAG, "accept: SMS Unavailable ");
                        progressBar.setVisibility(GONE);
                        noDataTV.setVisibility(View.VISIBLE);
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
        if (phoneNumber != null && smsList != null) {
            if (smsList.size() > 0) {
                long daysSinceLastContacted = getDifferenceDays();
                showGraphButtons();
                enableTextViewVisibility();
                populateDataTextViews(daysSinceLastContacted);
                loadBarGraphs();
            }
        }
    }

    private long getDifferenceDays() {
        Date date1 = new Date((Long.valueOf(smsList.get(smsList.size() - 1).getTimeStamp())));
        Date date2 = new Date(System.currentTimeMillis());
        long difference = date2.getTime() - date1.getTime();
        return difference / (1000 * 60 * 60 * 24);
    }

    private void showGraphButtons() {
        monthlyCard.setVisibility(View.VISIBLE);
        weeklyCard.setVisibility(View.VISIBLE);
        hourlyCard.setVisibility(View.VISIBLE);
    }

    private void enableTextViewVisibility() {
        commonWordSentTextTV.setVisibility(View.VISIBLE);
        commonReceivedWordTextTV.setVisibility(View.VISIBLE);
        youTV.setVisibility(View.VISIBLE);
        feedbackTV.setVisibility(View.VISIBLE);
        commonWordReceivedTV.setVisibility(View.VISIBLE);
        commonWordSentTV.setVisibility(View.VISIBLE);
    }

    private void populateDataTextViews(long daysSinceLastContacted) {

        getHourlyReceivedTreeMap();
        getHourlySentTreeMap();
        getWeeklyReceivedTreeMap();
        getWeeklySentTreeMap();
        getMonthlyReceivedTreeMap();
        getMonthlySentTreeMap();

        daysSinceContactedTV.setText(String.valueOf(daysSinceLastContacted));
        wordCount.setAvgWordCountSentText(smsList, avgWordSentTV);
        wordCount.setAvgWordCountReceivedText(smsList, avgWordReceivedTV);
        commonWordReceivedTV.setText(wordFrequency.mostCommonWordReceived(smsList));
        commonWordSentTV.setText(wordFrequency.mostCommonWordSent(smsList));

    }

    private void loadBarGraphs() {
        int totalWordCountReceived = wordCount.totalWordCountReceived(smsList);
        int totalWordCountSent = wordCount.totalWordCountSent(smsList);
        int averageWordsReceived = wordCount.averageWordCountReceived(smsList);
        int averageWordsSent = wordCount.averageWordCountSent(smsList);

        AverageWordCountBarChart averageWordCountBarChart = new AverageWordCountBarChart(wordAverageChart, averageWordsReceived, averageWordsSent);
        TotalSmsBarChart totalSmsBarChart = new TotalSmsBarChart(totalWordCountChart, totalWordCountReceived, totalWordCountSent);
        averageWordCountBarChart.showBarChart();
        totalSmsBarChart.showBarChart();
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

                    feedbackTV.setText(analyticsFeedback
                            .timeOfDayFeedback(timeMostContacted
                                    .timeMostContacted(hourlyReceivedTreeMap)));
                });
    }

    public void getHourlySentTreeMap() {
        Observable.fromCallable(() -> {
            GetHourlySent callable = new GetHourlySent();
            return callable.getHourlySent(smsList);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(treeMap -> {
                    hourlySentTreeMap = treeMap;
                    hourlySent = convertToFloats(hourlySentTreeMap);
                    Log.e(TAG, "getHourlySentTreeMap: COMPLETE");

                    progressBar.setVisibility(GONE);
                    statSummary.setVisibility(View.VISIBLE);
                    totalCard.setVisibility(View.VISIBLE);
                    wordCard.setVisibility(View.VISIBLE);
                    buttonBarCard.setVisibility(View.VISIBLE);
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
            GetMonthlySent callable = new GetMonthlySent();
            return callable.getMonthlySent(smsList);
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
        if (!isNull(monthlyReceived) && !isNull(monthlySent)) {
            Fragment monthlyGraphFragment = new MonthlyChartFragment();
            Bundle monthlyGraphBundle = new Bundle();
            monthlyGraphBundle.putFloatArray("monthlyReceived", monthlyReceived);
            monthlyGraphBundle.putFloatArray("monthlySent", monthlySent);
            monthlyGraphFragment.setArguments(monthlyGraphBundle);

            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.linechart_fragment_container, monthlyGraphFragment)
                    .commit();
        }
    }

    @OnClick({R.id.hourly_card, R.id.weekly_card, R.id.monthly_card})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.monthly_card:
                if (!isNull(monthlySent) && !isNull(monthlyReceived)) {
                    showMonthlyGraphFragment();

                    hourlyCard.setCardBackgroundColor(context.getColor(R.color.charcoal));
                    hourlyCard.setCardElevation(convertPixelsToDp(INACTIVE_ELEVATION, context));
                    monthlyCard.setCardBackgroundColor(context.getColor(R.color.carmine_pink_lite));
                    monthlyCard.setCardElevation(convertPixelsToDp(ACTIVE_ELEVATION, context));
                    weeklyCard.setCardBackgroundColor(context.getColor(R.color.charcoal));
                    weeklyCard.setCardElevation(convertPixelsToDp(INACTIVE_ELEVATION, context));
                    feedbackTV.setText(analyticsFeedback
                            .monthOfYearFeedback(timeMostContacted
                                    .timeMostContacted(monthlyReceivedTreeMap)));
                }
                break;

            case R.id.weekly_card:
                if (!isNull(weeklySent) && !isNull(weeklyReceived)) {
                    showWeeklyGraphFragment();

                    hourlyCard.setCardBackgroundColor(context.getColor(R.color.charcoal));
                    hourlyCard.setCardElevation(convertPixelsToDp(INACTIVE_ELEVATION, context));
                    weeklyCard.setCardBackgroundColor(context.getColor(R.color.carmine_pink_lite));
                    weeklyCard.setCardElevation(convertPixelsToDp(ACTIVE_ELEVATION, context));
                    monthlyCard.setCardBackgroundColor(context.getColor(R.color.charcoal));
                    monthlyCard.setCardElevation(convertPixelsToDp(INACTIVE_ELEVATION, context));
                    feedbackTV.setText(analyticsFeedback
                            .dayOfWeekFeedback(timeMostContacted
                                    .timeMostContacted(weeklyReceivedTreeMap)));
                }
                break;

            case R.id.hourly_card:
                if (!isNull(hourlySent) && !isNull(hourlyReceived)) {
                    showHourlyGraphFragment();

                    hourlyCard.setCardBackgroundColor(context.getColor(R.color.carmine_pink_lite));
                    hourlyCard.setCardElevation(convertPixelsToDp(ACTIVE_ELEVATION, context));
                    weeklyCard.setCardBackgroundColor(context.getColor(R.color.charcoal));
                    weeklyCard.setCardElevation(convertPixelsToDp(INACTIVE_ELEVATION, context));
                    monthlyCard.setCardBackgroundColor(context.getColor(R.color.charcoal));
                    monthlyCard.setCardElevation(convertPixelsToDp(INACTIVE_ELEVATION, context));
                    feedbackTV.setText(analyticsFeedback
                            .timeOfDayFeedback(timeMostContacted
                                    .timeMostContacted(hourlyReceivedTreeMap)));
                }
                break;
        }
    }

    private void showWeeklyGraphFragment() {
        Fragment weeklyChartFragment = new WeeklyChartFragment();
        Bundle bundle = new Bundle();
        bundle.putFloatArray("weeklyReceived", weeklyReceived);
        bundle.putFloatArray("weeklySent", weeklySent);
        weeklyChartFragment.setArguments(bundle);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.linechart_fragment_container, weeklyChartFragment)
                .commit();
    }

    private void showHourlyGraphFragment() {
        Fragment hourlyChartFragment = new HourlyChartFragment();
        Bundle bundle = new Bundle();
        bundle.putFloatArray("hourlyReceived", hourlyReceived);
        bundle.putFloatArray("hourlySent", hourlySent);
        hourlyChartFragment.setArguments(bundle);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.linechart_fragment_container, hourlyChartFragment)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        Log.d(TAG, "onStop: STOPPED");
    }

    @Override
    public void onStop() {
        super.onStop();
        disposables.clear();
        Log.d(TAG, "onStop: STOPPED");
    }
}



