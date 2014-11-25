package com.guyjstitt.trender.util;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.guyjstitt.trender.activity.WebActivity;
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

/**
 * Created by gstitt on 11/24/14.
 */
//subclass that gets top result and starts web view and passes the top result url
public class GetURLTask extends AsyncTask<Void, Void, Void> {

    //Json node names
    private static final String TAG_NAME = "name";

    //google key
    private static final String GOOGLE_ACCESS = "AIzaSyAS5Sg2lv4s6uepR_DYfahGuUt3J8v7OUk";
    private static final String GOOGLE_API = "https://www.googleapis.com/customsearch/v1";
    private static final String GOOGLE_ENGINE_ID = "005134152895831399260:rhg-e3atmi8";

    //ArrayList
    public ArrayList<HashMap<String,String>> searchList;
    //used to get the trendname and context
    public String mTrendName;
    public Context mContext;

    //set vars for GetURLTask async task
    GetURLTask myTask;
    InputStream inputStream = null;
    String result = "";
    String query = "?q=";
    String key = "&key=";
    String sId = "&cx=";
    String luckyResult ="";

    //constructor to get the context and trend name that was clicked on
    public GetURLTask(Context context, String trendName) {
        mContext = context;
        mTrendName = trendName;
    }

    //TODO implement checking for cancelling the task
    public void onCancel() {
        myTask.cancel(true);
    }

    @Override
    protected Void doInBackground(Void... params) {
        //create search list
        searchList = new ArrayList<HashMap<String,String>>();
        //variable to hold trend name that can be passed in a url
        String encodedTrendName = null;
        try {
            encodedTrendName = URLEncoder.encode(mTrendName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //creates the URL by building the URL string
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

        //clears any previous information before populating the search list again
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
        //get the top result that is indexed at 0
        HashMap<String, String> topResult = searchList.get(0);
        luckyResult = topResult.get("name");

        conn.disconnect();

        return null;
    }

    protected void onPostExecute(Void result) {

        //save the item clicked on to Parse
        ParseObject trendObject = new ParseObject("TrendObject");
        trendObject.put("trendName", mTrendName);
        trendObject.put("url", luckyResult);
        trendObject.put("recent","true");
        trendObject.saveInBackground();

        Intent intent = new Intent(mContext, WebActivity.class);
        Bundle extras = new Bundle();
        //really don't know of this is okay
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("lucky_url", luckyResult);
        (mContext).startActivity(intent);
    }
}