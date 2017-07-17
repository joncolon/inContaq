package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.bargraphs;

import android.graphics.Color;

import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data.WordCount;

import static com.db.chart.Tools.fromDpToPx;
import static com.db.chart.renderer.AxisRenderer.LabelPosition.NONE;


public class TotalSmsBarGraph extends BarGraph {
    private static final String FILL_COLOR = "#000000";
    private static final String SENT_COLOR = "#EF7674";
    private static final String RECEIVED_COLOR = "#FDFFFC";
    private int totalWordsSent;
    private int totalWordsReceived;
    private BarChartView barChartView;
    private String[] mLabels = {"Sent", "Received"};
    private int highestValue;

    public TotalSmsBarGraph(BarChartView barChartView) {
        this.barChartView = barChartView;
    }


    public void showBarGraph(ArrayList<Sms> smsList, WordCount wordCount) {
        getValues(smsList, wordCount);
        buildGraph();
        barChartView.show();
    }

    void getValues(ArrayList<Sms> smsList, WordCount wordCount) {
        totalWordsSent = wordCount.wordCountSent(smsList);
        totalWordsReceived = wordCount.wordCountReceived(smsList);
    }

    void buildGraph() {
        findHighestYValue();
        configureGraphData();
        configureGraphAttributes();
    }

    void findHighestYValue() {
        highestValue = Math.max(totalWordsReceived, totalWordsSent);
        if (highestValue == 0) {
            highestValue = 10;
        }
    }

    void configureGraphData() {
        BarSet barSet = new BarSet();
        Bar barSent = new Bar(mLabels[0], totalWordsSent);
        Bar barReceived = new Bar(mLabels[1], totalWordsReceived);
        barReceived.setColor(Color.parseColor(RECEIVED_COLOR));
        barSent.setColor(Color.parseColor(SENT_COLOR));
        barSet.addBar(barSent);
        barSet.addBar(barReceived);

        barChartView.addData(barSet);
    }

    void configureGraphAttributes() {
        barChartView.setBarSpacing(fromDpToPx(15));
        barChartView.setRoundCorners(fromDpToPx(2));
        barChartView.setBarBackgroundColor(Color.parseColor(FILL_COLOR));

        barChartView.setXAxis(false)
                .setYAxis(false)
                .setAxisBorderValues(0, addSpaceAboveHighestYValue(highestValue))
                .setXLabels(NONE)
                .setYLabels(NONE);
    }

    @Override
    int addSpaceAboveHighestYValue(int YAxis) {
        return super.addSpaceAboveHighestYValue(YAxis);
    }
}