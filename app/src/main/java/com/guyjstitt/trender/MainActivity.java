package com.guyjstitt.trender;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.guyjstitt.trender.activity.RecentTrendsActivity;
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
    private String screenName;
    private  ParseUser user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //keys for parse.com
        Parse.initialize(this, "zpr5CWnZst3fg7eKHxtpptFHQdRy9EFF7AYYA5Yt", "VcrrzAEH5S6yF6Sl93pGP7EcpconiqATJ0dz2ZQd");
        //setup twitter login
        ParseTwitterUtils.initialize("ij16iXvFm1oxxss88Scw6JgCy", "T1QcwJ3d1niOp6M0NxZHgIaSFq0d67Iyp7OcmdYYyN8X4E7gOG");
        //final String user = ParseTwitterUtils.getTwitter().getScreenName();
        ParseUser thisUser = ParseUser.getCurrentUser();


        if(thisUser == null) {
            ParseTwitterUtils.logIn(this, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException err) {
                    if (user == null) {
                        Log.d("MyApp", "Uh oh. The user cancelled the Twitter login.");
                    } else if (user.isNew()) {
                        Log.d("MyApp", "User signed up and logged in through Twitter!");
                    } else {
                        Log.d("MyApp", "User logged in through Twitter!");
                        screenName = ParseTwitterUtils.getTwitter().getScreenName();
                        TextView mScreenName = (TextView) findViewById(R.id.screenName);
                        mScreenName.setText(screenName);
                    }
                }
            });
        } else {
            Log.d("MyApp", "This use is already logged in!");
            screenName = ParseTwitterUtils.getTwitter().getScreenName();
            TextView mScreenName = (TextView) findViewById(R.id.screenName);
            mScreenName.setText("Hello " + screenName);
        }

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
                        new TrendTask(context, lv, screenName).execute();
                        return true;

                    case R.id.action_history:
                        // history code
                        Intent intent = new Intent(getBaseContext(), RecentTrendsActivity.class );
                        intent.putExtra("currentUserName",screenName);

                        Bundle bndlanimation =
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
                        startActivity(intent, bndlanimation);
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
        new TrendTask(context, lv, screenName).execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
