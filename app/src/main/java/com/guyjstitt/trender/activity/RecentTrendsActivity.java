package com.guyjstitt.trender.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.guyjstitt.trender.R;
import com.guyjstitt.trender.adapter.ListViewAdapter;
import com.guyjstitt.trender.model.TrendModel;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gstitt on 11/21/14.
 */
public class RecentTrendsActivity extends ActionBarActivity {
    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ListViewAdapter adapter;
    private List<TrendModel> recentTrendlist = null;
    String mCurentUserName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.recent_trends_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.recenttoolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        overridePendingTransition( R.anim.animation, R.anim.animation2);

        //get extras
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mCurentUserName = bundle.getString("currentUserName");
        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask(mCurentUserName).execute();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition( R.anim.animation, R.anim.animation2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.animation3, R.anim.animation4);
            return true;
        }
        return true;
    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        public RemoteDataTask(String currentUserName) {
            mCurentUserName = currentUserName;
        }

        @Override
        protected Void doInBackground(Void... params) {

            // Create the array
            recentTrendlist = new ArrayList<TrendModel>();
            try {

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("TrendObject");
                query.whereEqualTo("currentUserName", mCurentUserName);

                ob = query.find();
                for (ParseObject trend: ob) {

                    TrendModel rt = new TrendModel();
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
