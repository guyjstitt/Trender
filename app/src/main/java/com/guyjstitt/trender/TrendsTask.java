package com.guyjstitt.trender;

/**
 * Created by gstitt on 11/14/14.
 */
/*
public class TrendsTask extends AsyncTask<String, Void, List<Topic>>  {
    private Context context;
    private static final String CONSUMER_KEY = "ij16iXvFm1oxxss88Scw6JgCy" ;
    private static final String CONSUMER_SECRET = "T1QcwJ3d1niOp6M0NxZHgIaSFq0d67Iyp7OcmdYYyN8X4E7gOG";
    private static final String ACCESS_KEY = "1882820096-7IQ3Bqq5qpcvIeZPtp2U7WUjUu2WrtUUH8njQX6";
    private static final String ACCESS_SECRET = "M3DhHLwlduQzKRY1lmNMir8EIQq9wGn2jBfiVbHz9tddc";

    public TrendsTask(Context context){
        this.context=context;
    }

    @Override
    protected List<Topic> doInBackground(String... params) {
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
        return null;
    }

    @Override
    protected void onPostExecute(List<Topic> result)
    {
        super.onPostExecute(result);
        TrendAdapter adpt = new TrendAdapter(getApplicationContext(), result);
        adpt.setTrendList(result);

    }
}

*/