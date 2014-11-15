package com.guyjstitt.trender;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by gstitt on 11/14/14.
 */

public class TrendsTask extends AsyncTask<Void, String, Void> {

    private ArrayAdapter<String> adapter;
    private Context mContext;

    private static final String CONSUMER_KEY = "ij16iXvFm1oxxss88Scw6JgCy" ;
    private static final String CONSUMER_SECRET = "T1QcwJ3d1niOp6M0NxZHgIaSFq0d67Iyp7OcmdYYyN8X4E7gOG";
    private static final String ACCESS_KEY = "1882820096-7IQ3Bqq5qpcvIeZPtp2U7WUjUu2WrtUUH8njQX6";
    private static final String ACCESS_SECRET = "M3DhHLwlduQzKRY1lmNMir8EIQq9wGn2jBfiVbHz9tddc";

    public ListView trendList;
    public TrendsTask(Context context, ListView trendList) {
        this.mContext = context;
        this.trendList = trendList;
    }

    //Before execute set adapter
    @Override
    protected void onPreExecute() {
        MainActivity mainActivity = new MainActivity();
        adapter = (ArrayAdapter<String>) trendList.getAdapter();
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

        JSONArray array = new JSONArray();
        for (int i = 0; i < trends.getTrends().length; i++) {

            //Previous code to convert JsonObject to JsonArray
            /*
                try {
                    JSONObject object = new JSONObject();
                    object.put("name", trends.getTrends()[i].getName());
                    System.out.println(trends.getTrends()[i].getName());
                    //array.put(i, object));
                    array.put(object);
                    System.out.println(array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            */
            //add the trends one by one to the the array JsonArray
            array.put(trends.getTrends()[i].getName());

        }

        //Convert JsonArray to String Array
        List<String> list = new ArrayList<String>();
        for (int i=0; i<array.length(); i++) {
            try {
                list.add( array.getString(i) );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String[] stringArray = list.toArray(new String[list.size()]);
        System.out.println(array);

            /*
            String secondTrend;
            try {
                secondTrend = array.getJSONObject(1).getString("name");
                System.out.println(secondTrend);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray arr = null;
            try {
                arr = new JSONArray(array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<String> list = new ArrayList<String>();
            for(int i = 0; i < arr.length(); i++){
                try {
                    list.add(arr.getJSONObject(i).getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            */
        // for items in stringArray call pass to publishProgress
        for (String item: stringArray) {
            publishProgress(item);
        }
        return null;
    }

    //When progress updated, add the value to the adapter
    @Override
    protected  void onProgressUpdate(String... values) {
        adapter.add(values[0]);
    }

    //Unused
    @Override
    protected void onPostExecute(Void result)
    {


    }
}
