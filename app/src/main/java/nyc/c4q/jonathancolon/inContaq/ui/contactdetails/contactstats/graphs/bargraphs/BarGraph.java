package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.graphs.bargraphs;

import java.util.ArrayList;

import nyc.c4q.jonathancolon.inContaq.model.Sms;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data.WordCount;

/**
 * Created by jonathancolon on 6/22/17.
 */

abstract class BarGraph {

    public abstract void showBarGraph(ArrayList<Sms> smsList, WordCount wordCount);
    abstract void getValues(ArrayList<Sms> smsList, WordCount wordCount);
    abstract void buildGraph();
    abstract void findHighestYValue();
    abstract void configureGraphData();
    abstract void configureGraphAttributes();

    //this method to ensure empty space above the highest point in the graph.
    int addSpaceAboveHighestYValue(int YAxis) {
        return (int) Math.round(YAxis * 1.25);
    }
}
