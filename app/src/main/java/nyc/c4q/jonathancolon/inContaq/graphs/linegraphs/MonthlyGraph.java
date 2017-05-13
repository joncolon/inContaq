package nyc.c4q.jonathancolon.inContaq.graphs.linegraphs;

public class MonthlyGraph {

//    private static final String SENT_COLOR = "#EF7674";
//    private static final String LABEL_COLOR = "#FDFFFC";
//    private static final String RECEIVED_COLOR = "#FDFFFC";
//    private Context context;
//    private LineChartView lineGraph;
//
//    private String[] xAxisLabels = {
//            context.getString(R.string.jan), context.getString(R.string.feb),
//            context.getString(R.string.mar), context.getString(R.string.apr),
//            context.getString(R.string.jun), context.getString(R.string.may),
//            context.getString(R.string.jul), context.getString(R.string.aug),
//            context.getString(R.string.sep), context.getString(R.string.oct),
//            context.getString(R.string.nov), context.getString(R.string.dec)
//    };
//
//    public MonthlyGraph(Context context, LineChartView lineGraph) {
//        this.context = context;
//        this.lineGraph = lineGraph;
//    }
//
//    public void showMonthlyGraph() {
//        loadGraph();
//    }
//
//    synchronized private void loadGraph() {
//        setGraphData();
//        setGraphAttributes(getYValue());
//        animateGraph();
//    }
//
//    private void setGraphData() {
//        float[] receivedValues = smsAnalytics.getMonthlyReceived();
//        float[] sentValues = smsAnalytics.getMonthlySent();
//        prepareReceivedLineSet(receivedValues);
//        prepareSentLineSet(sentValues);
//    }
//
//    private void setGraphAttributes(int maxYvalue) {
//        lineGraph.setBorderSpacing(Tools.fromDpToPx(2))
//                .setAxisBorderValues(0, maxYvalue)
//                .setYLabels(NONE)
//                .setXLabels(OUTSIDE)
//                .setFontSize(24)
//                .setAxisLabelsSpacing(15f)
//                .setLabelsColor(parseColor(LABEL_COLOR))
//                .setXAxis(false)
//                .setYAxis(false);
//    }
//
//    private void prepareReceivedLineSet(float[] receivedValues) {
//        LineSet dataReceivedValues = new LineSet(xAxisLabels, receivedValues);
//        dataReceivedValues.setColor(parseColor(RECEIVED_COLOR))
//                .setDotsColor(parseColor(RECEIVED_COLOR))
//                .setThickness(4)
//                .beginAt(0);
//        lineGraph.addData(dataReceivedValues);
//    }
//
//    private void prepareSentLineSet(float[] sentValues) {
//        LineSet dataSentValues = new LineSet(xAxisLabels, sentValues);
//        dataSentValues.setColor(parseColor(SENT_COLOR))
//                .setDotsColor(parseColor(SENT_COLOR))
//                .setDashed(new float[]{1f, 1f})
//                .setThickness(4)
//                .beginAt(0);
//        lineGraph.addData(dataSentValues);
//    }
//
//    private void animateGraph() {
//        Animation anim = new Animation().setEasing(new BounceInterpolator());
//        lineGraph.show(anim);
//    }
//
//     int getYValue() {
//        int maxSent = findMaximumValue(monthlySent); //todo is null right now
//        int maxReceived = findMaximumValue(monthlyReceived); //todo is null right now
//        int highestValue = Math.max(maxSent, maxReceived);
//
//        if (highestValue == 0){
//            highestValue = 100;
//        }
//        return increaseByQuarter(highestValue);
//    }
//
//    private static int findMaximumValue(float[] input) {
//        float maxValue = input[0];
//        for (int i = 1; i < input.length; i++) {
//            if (input[i] > maxValue) {
//                maxValue = Math.round(input[i]);
//            }
//        }
//        return (int) maxValue;
//    }
//
//    private int increaseByQuarter(int input) {
//        return (int) Math.round(input * 1.25);
//    }

}
