package com.guyjstitt.trender;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class MainActivity extends Activity {

    private static final String CONSUMER_KEY = "ij16iXvFm1oxxss88Scw6JgCy" ;
    private static final String CONSUMER_SECRET = "T1QcwJ3d1niOp6M0NxZHgIaSFq0d67Iyp7OcmdYYyN8X4E7gOG";
    private static final String ACCESS_KEY = "1882820096-7IQ3Bqq5qpcvIeZPtp2U7WUjUu2WrtUUH8njQX6";
    private static final String ACCESS_SECRET = "M3DhHLwlduQzKRY1lmNMir8EIQq9wGn2jBfiVbHz9tddc";

    public ListView trendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Create the
        trendList = (ListView) findViewById(R.id.listViewTrends);
        trendList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList()));
        TrendsTask task = new TrendsTask(this, trendList);
        task.execute();
        trendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,WebActivity.class);
                intent.setData(Uri.parse("www.javacodegeeks.com"));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Currently unused tweet thread
    public void tweet() {
        Runnable runnable = new Runnable() {
            public void run() {
                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true);
                cb.setOAuthConsumerKey(CONSUMER_KEY);
                cb.setOAuthConsumerSecret(CONSUMER_SECRET);
                cb.setOAuthAccessToken(ACCESS_KEY);
                cb.setOAuthAccessTokenSecret(ACCESS_SECRET);
                TwitterFactory tf = new TwitterFactory(cb.build());
                Twitter twitter = tf.getInstance();

                Status status = null;
                try {
                    status = twitter.updateStatus("tweeting from my first andriod app using twitter api");
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                System.out.println("Successfully updated the status to [" + status.getText() + "].");
            }
        };
        Thread tweetThread = new Thread(runnable);
        tweetThread.start();
    }

    //Currently unused latest trends thread
    public void latestTrends() {

        Runnable runnable = new Runnable() {
            public void run() {
                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true);
                cb.setOAuthConsumerKey(CONSUMER_KEY);
                cb.setOAuthConsumerSecret(CONSUMER_SECRET);
                cb.setOAuthAccessToken(ACCESS_KEY);
                cb.setOAuthAccessTokenSecret(ACCESS_SECRET);
                TwitterFactory tf = new TwitterFactory(cb.build());
                Twitter twitter = tf.getInstance();

                ResponseList<Location> locations = null;
                try {
                    locations = twitter.getAvailableTrends();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                Trends trends = null;
                try {
                    trends = twitter.getPlaceTrends(23424977);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }

                JSONArray array = new JSONArray();
                for (int i = 0; i < trends.getTrends().length; i++) {


                    try {
                        //JSONObject singleTrend = new JSONObject();
                        JSONObject object = new JSONObject();
                        object.put("name", trends.getTrends()[i].getName());
                        System.out.println(trends.getTrends()[i].getName());
                        //array.put(i, object));
                        array.put(object);
                        System.out.println(array);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                System.out.println(array);

                String secondTrend;
                try {
                    secondTrend = array.getJSONObject(1).getString("name");
                    System.out.println(secondTrend);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Thread trendThread = new Thread(runnable);
        trendThread.start();
    }

    public void search() {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        String keyword= "latest trends";
        intent.putExtra(SearchManager.QUERY, keyword);
        startActivity(Intent.createChooser(intent, "dialogTitle"));
    }

}
