package com.guyjstitt.trender;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.guyjstitt.trender.activity.RecentTrendsActivity;
import com.guyjstitt.trender.util.GetURLTask;
import com.guyjstitt.trender.util.TrendTask;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;


public class MainActivity extends ActionBarActivity {

    //used to change query for search URL and pass to WebActivity
    public String trendName;
    public Context context;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //keys for parse.com
        Parse.initialize(this, "zpr5CWnZst3fg7eKHxtpptFHQdRy9EFF7AYYA5Yt", "VcrrzAEH5S6yF6Sl93pGP7EcpconiqATJ0dz2ZQd");
        //setup twitter login
        ParseTwitterUtils.initialize("ij16iXvFm1oxxss88Scw6JgCy", "T1QcwJ3d1niOp6M0NxZHgIaSFq0d67Iyp7OcmdYYyN8X4E7gOG");

        //System.out.println("This is my username " + userName);

        ParseTwitterUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Twitter login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Twitter!");
                } else {
                    Log.d("MyApp", "User logged in through Twitter!");
                    userName = user.getUsername();
                    Log.d("SAMPLE", userName);

                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.action_refresh:
                        // refresh code
                        //get the listview and context to pass to the TrendTask async task
                        ListView lv = (ListView) findViewById(android.R.id.list);
                        context = getApplicationContext();
                        new TrendTask(context, lv).execute();
                        return true;

                    case R.id.action_history:
                        // history code
                        Intent intent = new Intent(getBaseContext(), RecentTrendsActivity.class );
                        intent.putExtra("currentUserName",userName);
                        startActivity(intent);
                        return true;
                }

                return false;
            }
        });

        //keys for parse.com
        Parse.initialize(this, "zpr5CWnZst3fg7eKHxtpptFHQdRy9EFF7AYYA5Yt", "VcrrzAEH5S6yF6Sl93pGP7EcpconiqATJ0dz2ZQd");

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
                myTask = new GetURLTask(context,trendName,userName);
                myTask.execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
