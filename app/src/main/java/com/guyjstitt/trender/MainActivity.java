package com.guyjstitt.trender;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.guyjstitt.trender.activity.RecentTrendsActivity;
import com.guyjstitt.trender.util.GetURLTask;
import com.guyjstitt.trender.util.TrendTask;
import com.parse.Parse;


public class MainActivity extends ActionBarActivity {

    //used to change query for search URL and pass to WebActivity
    public String trendName;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //keys for parse.com
        Parse.initialize(this, "zpr5CWnZst3fg7eKHxtpptFHQdRy9EFF7AYYA5Yt", "VcrrzAEH5S6yF6Sl93pGP7EcpconiqATJ0dz2ZQd");

        //placeholder button for recent trends
        final Button button = (Button) findViewById(R.id.recentTrendBtn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),RecentTrendsActivity.class);
                startActivity(intent);
            }
        });

        //get the listview and context to pass to the TrendTask async task
        ListView lv = (ListView) findViewById(android.R.id.list);
        context = getApplicationContext();
        new TrendTask(context, lv).execute();

        //on click listener for list view
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //gets the text of the item that was pressed
                String selected = ((TextView) view.findViewById(R.id.textViewTrendName)).getText().toString();
                //sets trendName to that text so it can be passed to Search URL
                trendName = selected;

                //starts async task to get top result, passes app context and trendName
                GetURLTask myTask;
                myTask = new GetURLTask(context,trendName);
                myTask.execute();
            }
        });
    }
}
