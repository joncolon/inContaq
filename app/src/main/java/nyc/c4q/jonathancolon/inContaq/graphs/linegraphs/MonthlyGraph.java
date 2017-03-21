package nyc.c4q.jonathancolon.inContaq.graphs.linegraphs;

import android.content.Context;
import android.view.animation.BounceInterpolator;

import com.db.chart.Tools;
import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.utlities.sms.model.Sms;

import static android.graphics.Color.parseColor;
import static com.db.chart.renderer.AxisRenderer.LabelPosition.NONE;
import static com.db.chart.renderer.AxisRenderer.LabelPosition.OUTSIDE;


public class MonthlyGraph {

    private static final String SENT_COLOR = "#EF7674";
    private static final String LABEL_COLOR = "#FDFFFC";
    private static final String RECEIVED_COLOR = "#FDFFFC";
    private String[] xAxisLabels;
    private Context context;
    private LineChartView lineGraph;
    private ArrayList<Sms> smsList;
    private MonthlyGraphHelper monthlyGraphHelper;

    public MonthlyGraph(Context context, LineChartView lineGraph, ArrayList<Sms> smsList) {
        this.context = context;
        this.lineGraph = lineGraph;
        this.smsList = smsList;
    }

    public void showMonthlyGraph() {
        loadGraph();
    }

    synchronized private void loadGraph() {
        setGraphData();
        setGraphAttributes(monthlyGraphHelper.getYValue());
        animateGraph();
    }

    private void setGraphData() {
        monthlyGraphHelper = new MonthlyGraphHelper(context, smsList);
        float[] receivedValues = monthlyGraphHelper.getReceivedValue();
        float[] sentValues = monthlyGraphHelper.getSentValues();
        xAxisLabels = monthlyGraphHelper.getXAxisLabels();

        prepareReceivedLineSet(receivedValues);
        prepareSentLineSet(sentValues);
    }

    private void setGraphAttributes(int maxYvalue) {
        lineGraph.setBorderSpacing(Tools.fromDpToPx(2))
                .setAxisBorderValues(0, 100)
                .setYLabels(NONE)
                .setXLabels(OUTSIDE)
                .setFontSize(24)
                .setAxisLabelsSpacing(15f)
                .setLabelsColor(parseColor(LABEL_COLOR))
                .setXAxis(false)
                .setYAxis(false);
    }

    private void prepareReceivedLineSet(float[] receivedValues) {
        LineSet dataReceivedValues = new LineSet(xAxisLabels, receivedValues);
        dataReceivedValues.setColor(parseColor(RECEIVED_COLOR))
                .setDotsColor(parseColor(RECEIVED_COLOR))
                .setThickness(4)
                .beginAt(0);
        lineGraph.addData(dataReceivedValues);
    }

    private void prepareSentLineSet(float[] sentValues) {
        LineSet dataSentValues = new LineSet(xAxisLabels, sentValues);
        dataSentValues.setColor(parseColor(SENT_COLOR))
                .setDotsColor(parseColor(SENT_COLOR))
                .setDashed(new float[]{1f, 1f})
                .setThickness(4)
                .beginAt(0);
        lineGraph.addData(dataSentValues);
    }

    private void animateGraph() {
        Animation anim = new Animation().setEasing(new BounceInterpolator());
        lineGraph.show(anim);
    }
}
