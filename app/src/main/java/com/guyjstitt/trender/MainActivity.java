package com.guyjstitt.trender;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class MainActivity extends ListActivity {

    private static final String CONSUMER_KEY = "ij16iXvFm1oxxss88Scw6JgCy" ;
    private static final String CONSUMER_SECRET = "T1QcwJ3d1niOp6M0NxZHgIaSFq0d67Iyp7OcmdYYyN8X4E7gOG";
    private static final String ACCESS_KEY = "1882820096-7IQ3Bqq5qpcvIeZPtp2U7WUjUu2WrtUUH8njQX6";
    private static final String ACCESS_SECRET = "M3DhHLwlduQzKRY1lmNMir8EIQq9wGn2jBfiVbHz9tddc";

    //google key
    private static final String GOOGLE_ACCESS = "AIzaSyAS5Sg2lv4s6uepR_DYfahGuUt3J8v7OUk";
    private static final String GOOGLE_API = "https://www.googleapis.com/customsearch/v1";
    private static final String GOOGLE_ENGINE_ID = "005134152895831399260:rhg-e3atmi8";
    //Json node names
    private static final String TAG_NAME = "name";

    //ArrayList
    public ArrayList<HashMap<String,String>> trendList;
    public ArrayList<HashMap<String,String>> searchList;
    public String query;
    public String key;
    //used to change query for search URL and pass to WebActivity
    public String trendName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.initialize(this, "zpr5CWnZst3fg7eKHxtpptFHQdRy9EFF7AYYA5Yt", "VcrrzAEH5S6yF6Sl93pGP7EcpconiqATJ0dz2ZQd");

        final Button button = (Button) findViewById(R.id.recentTrendBtn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),RecentTrendsActivity.class);
                startActivity(intent);
            }
        });

        //initialize Array list Hash maps and list view
        trendList = new ArrayList<HashMap<String,String>>();
        searchList = new ArrayList<HashMap<String,String>>();
        final ListView lv = getListView();

        //executes task to populate list view
        new TrendsTask().execute();

        //on click listener for list view
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //gets the text of the item that was pressed
                String selected = ((TextView) view.findViewById(R.id.textViewTrendName)).getText().toString();
                //sets trendName to that text so it can be passed to Search URL
                trendName = selected;

                //starts async task to get top result
                GetURLTask myTask;
                myTask = new GetURLTask();
                myTask.execute();
            }
        });


    }

    //subclass that gets top result and starts web view and passes the top result url
    class GetURLTask extends AsyncTask<Void, Void, Void> {

        GetURLTask myTask;
        InputStream inputStream = null;
        String result = "";
        String query = "?q=";
        String key = "&key=";
        String sId = "&cx=";
        String luckyResult ="";

        public void onCancel() {
            myTask.cancel(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String encodedTrendName = null;
            try {
                encodedTrendName = URLEncoder.encode(trendName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String url_select = GOOGLE_API + query + encodedTrendName + sId + GOOGLE_ENGINE_ID + key + GOOGLE_ACCESS;
            System.out.println(url_select);

            URL url = null;
            try {
                url = new URL(url_select);
                System.out.println("this is the url "+ url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                conn.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String output;
            System.out.println("Output from Server .... \n");
            String link = null;
            HashMap<String,String> searchResult = null;
            searchList.clear();
            try {
                while ((output = br.readLine()) != null) {
                    if(output.contains("\"link\": \"")){
                        link=output.substring(output.indexOf("\"link\": \"")+("\"link\": \"").length(), output.indexOf("\","));
                        searchResult = new HashMap<String, String>();
                        searchResult.put(TAG_NAME,link);
                        searchList.add(searchResult);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            HashMap<String, String> topResult = searchList.get(0);
            luckyResult = topResult.get("name");

            conn.disconnect();

            return null;
        }

        protected void onPostExecute(Void result) {

            //save the item clicked on to Parse
            ParseObject trendObject = new ParseObject("TrendObject");
            trendObject.put("trendName", trendName);
            trendObject.put("url", luckyResult);
            trendObject.put("recent","true");
            trendObject.saveInBackground();

            Intent intent = new Intent(MainActivity.this, WebActivity.class);
            Bundle extras = new Bundle();
            intent.putExtra("lucky_url", luckyResult);
            startActivity(intent);
        }
    }

    class TrendsTask extends AsyncTask<Void, Void, Void> {

        private ArrayAdapter<String> adapter;
        private Context mContext;

        private static final String CONSUMER_KEY = "ij16iXvFm1oxxss88Scw6JgCy" ;
        private static final String CONSUMER_SECRET = "T1QcwJ3d1niOp6M0NxZHgIaSFq0d67Iyp7OcmdYYyN8X4E7gOG";
        private static final String ACCESS_KEY = "1882820096-7IQ3Bqq5qpcvIeZPtp2U7WUjUu2WrtUUH8njQX6";
        private static final String ACCESS_SECRET = "M3DhHLwlduQzKRY1lmNMir8EIQq9wGn2jBfiVbHz9tddc";

        @Override
        protected void onPreExecute() {

        }

        //Task to get the latest trends
        @Override
        protected Void doInBackground(Void... params) {
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

            //loop through trends and store in trendList
            for (int i = 0; i < trends.getTrends().length; i++) {
                String name = trends.getTrends()[i].getName();
                HashMap<String,String> trend = new HashMap<String, String>();
                trend.put(TAG_NAME,name);
                trendList.add(trend);
            }

            return null;
        }

        //After getting the trends, create adapter and apply data to view_for_each_trend
        @Override
        protected void onPostExecute(Void result)
        {
            ListAdapter adapter = new SimpleAdapter(MainActivity.this,trendList,R.layout.view_for_each_trend, new String[]{TAG_NAME},new int[] {R.id.textViewTrendName});
            setListAdapter(adapter);
        }
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
}
