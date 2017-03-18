package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.faceapi.LogHelper;


public class DetectionLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection_log);
        Log.d("jjjjj", "onCreate: " + this.getClass().getSimpleName());

        LogAdapter logAdapter = new LogAdapter();
        ListView listView = (ListView) findViewById(R.id.log);
        listView.setAdapter(logAdapter);
    }

    // The adapter of the ListView which contains the detection log.
    private class LogAdapter extends BaseAdapter {
        // The detection log.
        List<String> log;

        LogAdapter() {
            log = LogHelper.getDetectionLog();
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public int getCount() {
            return log.size();
        }

        @Override
        public Object getItem(int position) {
            return log.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.item_log, parent, false);
            }
            convertView.setId(position);

            ((TextView)convertView.findViewById(R.id.log)).setText(log.get(position));

            return convertView;
        }
    }
}
