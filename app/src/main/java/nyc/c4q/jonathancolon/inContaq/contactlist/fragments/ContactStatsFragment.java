package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.db.chart.animation.Animation;
import com.db.chart.view.LineChartView;

import org.parceler.Parcels;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.notifications.ContactNotificationService;
import nyc.c4q.jonathancolon.inContaq.contactlist.graphs.MonthlyGraph;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.SmsHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.WordCount;


public class ContactStatsFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner dateSpinner;
    private ArrayAdapter<CharSequence> spinnerArrayAdapter;

    private ContactNotificationService mContactNotificationService;

    private Button monthlyBtn, weeklyBtn, dailyBtn;
    private TextView smsStatsTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_stats, container, false);

        initViews(view);
        if (savedInstanceState == null) {
            showMonthlyGraphFragment();
        }

        ArrayList<Sms> smsList = SmsHelper.getAllSms(getContext(), unwrapParcelledContact());
        String averageWordCountSent = String.valueOf(WordCount.getAverageWordCountSent(smsList));
        String averageWordCountReceived = String.valueOf(WordCount.getAverageWordCountReceived(smsList));

        smsStatsTV.setText("Averge words sent: " + averageWordCountSent + "\n" +
        "Average words received: " + averageWordCountReceived);
        return view;
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
            default:
                break;
        }
    }

}
