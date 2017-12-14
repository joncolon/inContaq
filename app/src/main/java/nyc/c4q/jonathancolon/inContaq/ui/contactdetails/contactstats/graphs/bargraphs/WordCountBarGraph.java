package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.bargraphs;

import android.graphics.Color;

import com.db.chart.Tools;
import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data.WordCount;

import static com.db.chart.renderer.AxisRenderer.LabelPosition.NONE;


public class WordCountBarGraph extends BarGraph {
    private static final String FILL_COLOR = "#000000";
    private static final String SENT_COLOR = "#EF7674";
    private static final String RECEIVED_COLOR = "#FDFFFC";
    private int averageWordCountSent;
    private int averageWordCountReceived;
    private BarChartView barChartView;
    private String[] mLabels = {"Sent", "Received"};
    private int highestValue;

    public WordCountBarGraph(BarChartView barChartView) {
        this.barChartView = barChartView;
    }

    @Override
    public void showBarGraph(ArrayList<Sms> smsList, WordCount wordCount) {
        getValues(smsList, wordCount);
        buildGraph();
        barChartView.show();

    }

    @Override
    void getValues(ArrayList<Sms> smsList, WordCount wordCount) {
        averageWordCountSent = wordCount.averageWordCountSent(smsList);
        averageWordCountReceived = wordCount.averageWordCountReceived(smsList);
    }

    @Override
    void buildGraph() {
        findHighestYValue();
        configureGraphData();
        configureGraphAttributes();
    }

    @Override
    void findHighestYValue() {
        highestValue = Math.max(averageWordCountReceived, averageWordCountSent);
        if (highestValue == 0) {
            highestValue = 10;
        }
    }

    @Override
    void configureGraphData() {
        BarSet barSet = new BarSet();
        Bar barSent = new Bar(mLabels[0], averageWordCountSent);
        Bar barReceived = new Bar(mLabels[1], averageWordCountReceived);
        barReceived.setColor(Color.parseColor(RECEIVED_COLOR));
        barSent.setColor(Color.parseColor(SENT_COLOR));
        barSet.addBar(barSent);
        barSet.addBar(barReceived);

        barChartView.addData(barSet);
        barChartView.setBarSpacing(Tools.fromDpToPx(15));
        barChartView.setRoundCorners(Tools.fromDpToPx(2));
        barChartView.setBarBackgroundColor(Color.parseColor(FILL_COLOR));
    }

    @Override
    void configureGraphAttributes() {
        barChartView.setBarSpacing(Tools.fromDpToPx(15));
        barChartView.setRoundCorners(Tools.fromDpToPx(2));
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
