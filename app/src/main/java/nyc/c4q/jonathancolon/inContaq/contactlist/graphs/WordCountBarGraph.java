package nyc.c4q.jonathancolon.inContaq.contactlist.graphs;

import android.graphics.Color;

import com.db.chart.Tools;
import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.utlities.sms.Sms;
import nyc.c4q.jonathancolon.inContaq.utlities.sms.WordCount;

import static com.db.chart.renderer.AxisRenderer.LabelPosition.NONE;

/**
 * Created by jonathancolon on 3/16/17.
 */

public class WordCountBarGraph {
    private static final String FILL_COLOR = "#021620";
    private static final String RED_ROSE_MADDER = "#EF7674";
    private static final String WHITE_BABY_POWDER = "#FDFFFC";

    private BarChartView barChartView;
    private String[] mLabels = {"Sent", "Received"};
    int averageWordCountSent;
    int averageWordCountReceived;
    private ArrayList<Sms> smsList;
    private int highestValue;

    public WordCountBarGraph(BarChartView barChartView, ArrayList<Sms> smsList) {
        this.barChartView = barChartView;
        this.smsList = smsList;
    }

    public void showBarGraph(){
        getBarGraphValues(smsList);
        loadGraph();
    }

    private void loadGraph() {

        getYvalue();

        // Data
        BarSet barSet = new BarSet();
        Bar barSent = new Bar(mLabels[0], averageWordCountSent);
        Bar barReceived = new Bar(mLabels[1], averageWordCountReceived);
        barReceived.setColor(Color.parseColor(WHITE_BABY_POWDER));
        barSent.setColor(Color.parseColor(RED_ROSE_MADDER));
        barSet.addBar(barSent);
        barSet.addBar(barReceived);

        barChartView.addData(barSet);
        barChartView.setBarSpacing(Tools.fromDpToPx(15));
        barChartView.setRoundCorners(Tools.fromDpToPx(2));
        barChartView.setBarBackgroundColor(Color.parseColor("#7F000000"));

        // Chart
        barChartView.setXAxis(false)
                .setYAxis(false)
                .setAxisBorderValues(0, highestValue)
                .setXLabels(NONE)
                .setYLabels(NONE);
        barChartView.show();
    }

    private void getYvalue() {
        highestValue = 10;

        if (averageWordCountReceived > averageWordCountSent){
            highestValue = (int) getRound(averageWordCountReceived);
        }
        if (averageWordCountSent > averageWordCountReceived){
            highestValue = (int) getRound(averageWordCountSent);
        }
    }

    private long getRound(int input) {
        //this rounds up and multiples the value by a quarter to customize the Y axis to the contact
        return Math.round(input * 1.25);
    }

    private void getBarGraphValues(ArrayList<Sms> smsList) {
        averageWordCountSent = WordCount.getAverageWordCountSent(smsList);
        averageWordCountReceived = WordCount.getAverageWordCountReceived(smsList);
        this.smsList = smsList;
    }

}
