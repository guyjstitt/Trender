package com.guyjstitt.trender.util;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.guyjstitt.trender.R;

import java.util.ArrayList;
import java.util.HashMap;

import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by gstitt on 11/24/14.
 */
public class TrendTask extends AsyncTask<ArrayList<HashMap<String,String>> , Void, Void> {

    private ArrayAdapter<String> adapter;
    public ArrayList<HashMap<String,String>> trendList;
    private Context mContext;
    private View mView;
    private String mScreenName;

    private static final String CONSUMER_KEY = "ij16iXvFm1oxxss88Scw6JgCy" ;
    private static final String CONSUMER_SECRET = "T1QcwJ3d1niOp6M0NxZHgIaSFq0d67Iyp7OcmdYYyN8X4E7gOG";
    private static final String ACCESS_KEY = "1882820096-7IQ3Bqq5qpcvIeZPtp2U7WUjUu2WrtUUH8njQX6";
    private static final String ACCESS_SECRET = "M3DhHLwlduQzKRY1lmNMir8EIQq9wGn2jBfiVbHz9tddc";


    //Json node names
    private static final String TAG_NAME = "name";


    public TrendTask(Context context, View view, String screenName) {
        mContext = context;
        mView = view;
        mScreenName = screenName;
    }

    @Override
    protected void onPreExecute() {

    }

    //Task to get the latest trends
    @Override
    protected Void doInBackground(ArrayList<HashMap<String,String>>... params) {
        //System.out.println("HEY" + params[0]);

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
        trendList = new ArrayList<HashMap<String,String>>();
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
        ListAdapter adapter = new SimpleAdapter(mContext,trendList, R.layout.view_for_each_trend, new String[]{TAG_NAME},new int[] {R.id.textViewTrendName}) {
            @Override
            public View getView (final int position, final View convertView, final ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);

                Button b=(Button)v.findViewById(R.id.urlButton);
                final String trendName =  ((TextView)v.findViewById(R.id.textViewTrendName)).getText().toString();
                b.setTag(trendName);

                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Auto-generated method stub
                        System.out.println(position);
                        // TextView trendName = (TextView) view.findViewById(R.id.textViewTrendName);

                        String trendNameText = (String)view.getTag();
                        System.out.println(trendNameText);

                        //starts async task to get top result, passes app context and trendName
                        GetURLTask myTask;
                        myTask = new GetURLTask(mContext,trendNameText,"dontcare");
                        myTask.execute();

                    }
                });
                return v;
            }
        };
        ListView lv = (ListView) mView;
        lv.setAdapter(adapter);

    }
}
