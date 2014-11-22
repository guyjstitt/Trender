package com.guyjstitt.trender;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gstitt on 11/21/14.
 */
public class RecentTrendsActivity extends Activity {
    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ListViewAdapter adapter;
    private List<TrendData> recentTrendlist = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.recent_trends_activity);
        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();
    }
    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            // Create the array
            recentTrendlist = new ArrayList<TrendData>();
            try {

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("TrendObject");
                query.whereEqualTo("recent", "true");
                ob = query.find();
                for (ParseObject trend: ob) {

                    TrendData rt = new TrendData();
                    rt.setTrend((String) trend.get("trendName"));
                    rt.setUrl((String) trend.get("url"));
                    recentTrendlist.add(rt);
                }

            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in recent trend view
            listview = (ListView) findViewById(R.id.listView);
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(RecentTrendsActivity.this,
                    recentTrendlist);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
        }
    }

}
