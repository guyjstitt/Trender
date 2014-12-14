package com.guyjstitt.trender.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
    private String userInput;
    private Button b;
    private EditText userTweet;

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
    protected void onPostExecute(Void result) {
        final ListView lv = (ListView) mView;
        final ListAdapter adapter = new SimpleAdapter(mContext, trendList, R.layout.view_for_each_trend, new String[]{TAG_NAME}, new int[]{R.id.textViewTrendName}) {

            @Override
            public View getView(final int position, View convertView, final ViewGroup parent) {

                View view = super.getView(position, convertView, parent);

                Button tb = (Button) view.findViewById(R.id.tweetButton);
                Button ub = (Button) view.findViewById(R.id.urlButton);
                EditText inputTweet = (EditText) view.findViewById(R.id.tweetContent);
                final String trendName =  ((TextView)view.findViewById(R.id.textViewTrendName)).getText().toString();
                ub.setTag(trendName);
                tb.setTag(trendName);

                tb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setHasTransientState(true);

                        View parent = (View) v.getParent();
                        final Button myTweetBtn = (Button) parent.findViewById(R.id.tweetButton);
                        final Button learnMore = (Button) parent.findViewById(R.id.urlButton);
                        final Button updateBtn = (Button) parent.findViewById(R.id.updateStatus);
                        final EditText updateStatus = (EditText) parent.findViewById(R.id.tweetContent);
                        learnMore.setVisibility(View.GONE);
                        myTweetBtn.setVisibility(View.GONE);
                        updateBtn.setVisibility(View.VISIBLE);
                        updateBtn.setTag(v.getTag().toString());

                        updateBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                userInput = updateStatus.getText().toString();
                                Log.d("Text", userInput);

                                String trendNameText = (String)view.getTag();
                                System.out.println(trendNameText);

                                updateBtn.setVisibility(View.GONE);
                                updateStatus.setVisibility(View.GONE);
                                myTweetBtn.setVisibility(View.VISIBLE);
                                learnMore.setVisibility(View.VISIBLE);

                                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(updateStatus.getWindowToken(), 0);

                                TweetTask tweet;
                                tweet = new TweetTask(userInput,trendNameText);
                                tweet.execute();

                                Toast toast = Toast.makeText(mContext, "You tweeted: " + userInput + " " + trendNameText,Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                            }
                        });

                        updateStatus.setVisibility(View.VISIBLE);
                        updateStatus.requestFocus();

                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(updateStatus, InputMethodManager.SHOW_IMPLICIT);
                    }
                });

                ub.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String trendNameText = (String)view.getTag();
                        //starts async task to get top result, passes app context and trendName
                        GetURLTask myTask;
                        myTask = new GetURLTask(mContext,trendNameText,mScreenName);
                        myTask.execute();
                    }
                });
                return view;
            }
        };

        lv.setAdapter(adapter);
    }


}
