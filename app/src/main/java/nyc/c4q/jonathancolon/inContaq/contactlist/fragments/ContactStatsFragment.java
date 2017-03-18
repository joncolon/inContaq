package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.db.chart.view.BarChartView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.utlities.graphs.bargraphs.WordCountBarGraph;
import nyc.c4q.jonathancolon.inContaq.notifications.ContactNotificationService;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.SmsHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.WordCount;



public class ContactStatsFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner dateSpinner;
    private ArrayAdapter<CharSequence> spinnerArrayAdapter;

    private ContactNotificationService mContactNotificationService;

    private Button monthlyBtn, weeklyBtn, dailyBtn;
    private TextView smsStatsTV;
    private ArrayList<Sms> smsList;
    private BarChartView mChart;

    private String[] mLabels = {"Sent", "Received"};


    int averageWordCountSent;
    int averageWordCountReceived;
    private float[][] sentValues = {{1f, 6f}, {1f, 8f}};
    private int highestValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            smsStatsTV.setText("Averge words sent: " + String.valueOf(averageWordCountSent) + "\n" +
                    "Average words received: " + String.valueOf(averageWordCountReceived) + "\n" + "Last contacted " + differenceDays + " days ago");
        }

        WordCountBarGraph wordCountBarGraph = new WordCountBarGraph(mChart, smsList);
        wordCountBarGraph.showBarGraph();



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

    private void showDailyGraphFragment() {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_frag_container, new DailyGraphFragment())
                .commit();
    }

    private void initViews(View view) {
        dateSpinner = (Spinner) view.findViewById(R.id.date_spinner);
        spinnerArrayAdapter = ArrayAdapter.createFromResource(
                view.getContext(),
                R.array.date_spinner_array,
                R.layout.date_spinner_item);
        dateSpinner = (Spinner) view.findViewById(R.id.date_spinner);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.date_spinner_dropdown_item);
        dateSpinner.setAdapter(spinnerArrayAdapter);
        dateSpinner.setOnItemSelectedListener(this);
        smsStatsTV = (TextView) view.findViewById(R.id.sms_stats);
        monthlyBtn = (Button) view.findViewById(R.id.monthly_btn);
        weeklyBtn = (Button) view.findViewById(R.id.weekly_btn);
        dailyBtn = (Button) view.findViewById(R.id.daily_btn);
        monthlyBtn.setOnClickListener(this);
        weeklyBtn.setOnClickListener(this);
        dailyBtn.setOnClickListener(this);
        mChart = (BarChartView) view.findViewById(R.id.bar_chart_word_average);
    }

    @Nullable
    private Contact unwrapParcelledContact() {
        return Parcels.unwrap(getActivity().getIntent().getParcelableExtra(ContactListActivity.PARCELLED_CONTACT));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Contact contact = unwrapParcelledContact();
        switch (String.valueOf(parent.getItemAtPosition(position))) {
            case "WEEKLY":
                // TODO: 3/8/17 if last sent text == to 7 days + last sent text date then, notification
                break;
            case "2 WEEKS":
                // TODO: 3/8/17 if last sent text == to 14 days + last sent text date then, notification
                break;
            case "3 WEEKS":
                // TODO: 3/8/17 if last sent text == to 21 days + last sent text date then, notification
                break;
            case "MONTHLY":
                // TODO: 3/8/17 if last sent text == to 30 days + last sent text date then, notification
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.monthly_btn:
                showMonthlyGraphFragment();
                break;
            case R.id.weekly_btn:
                break;
            case R.id.daily_btn:
                showDailyGraphFragment();
                break;
        }
    }
}
