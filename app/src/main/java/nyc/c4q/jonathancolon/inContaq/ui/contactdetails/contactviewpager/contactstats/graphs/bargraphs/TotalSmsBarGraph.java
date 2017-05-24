package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.graphs.bargraphs;

import android.graphics.Color;

import com.db.chart.Tools;
import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactstats.data.WordCount;

import static com.db.chart.renderer.AxisRenderer.LabelPosition.NONE;


public class TotalSmsBarGraph {
    private static final String FILL_COLOR = "#000000";
    private static final String SENT_COLOR = "#EF7674";
    private static final String RECEIVED_COLOR = "#FDFFFC";
    private int totalWordsSent;
    private int totalWordsReceived;
    private BarChartView barChartView;
    private String[] mLabels = {"Sent", "Received"};
    private ArrayList<Sms> smsList;
    private int highestValue;

    public TotalSmsBarGraph(BarChartView barChartView, ArrayList<Sms> smsList) {
        this.barChartView = barChartView;
        this.smsList = smsList;
    }

    public void showBarGraph() {
        getBarGraphValues(smsList);
        loadGraph();
    }

    private void getBarGraphValues(ArrayList<Sms> smsList) {
        totalWordsSent = WordCount.wordCountSent(smsList);
        totalWordsReceived = WordCount.wordCountReceived(smsList);
        this.smsList = smsList;
    }

    private void loadGraph() {
        getYvalue();
        setGraphData();
        setGraphAttributes();
    }

    private void getYvalue() {
        highestValue = Math.max(totalWordsReceived, totalWordsSent);
        if (highestValue == 0) {
            highestValue = 10;
        }
    }

    private void setGraphData() {
        BarSet barSet = new BarSet();
        Bar barSent = new Bar(mLabels[0], totalWordsSent);
        Bar barReceived = new Bar(mLabels[1], totalWordsReceived);
        barReceived.setColor(Color.parseColor(RECEIVED_COLOR));
        barSent.setColor(Color.parseColor(SENT_COLOR));
        barSet.addBar(barSent);
        barSet.addBar(barReceived);

        barChartView.addData(barSet);
        barChartView.setBarSpacing(Tools.fromDpToPx(15));
        barChartView.setRoundCorners(Tools.fromDpToPx(2));
        barChartView.setBarBackgroundColor(Color.parseColor(FILL_COLOR));
    }

    private void setGraphAttributes() {
        barChartView.setXAxis(false)
                .setYAxis(false)
                .setAxisBorderValues(0, increaseByQuarter(highestValue))
                .setXLabels(NONE)
                .setYLabels(NONE);
        barChartView.show();
    }

    private int increaseByQuarter(int input) {
        return (int) Math.round(input * 1.25);
    }
}