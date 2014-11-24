package com.guyjstitt.trender.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.guyjstitt.trender.R;

/**
 * Created by gstitt on 11/15/14.
 */
public class WebActivity extends Activity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);

        Intent intent = getIntent();
        intent.getExtras();
        String topUrl = intent.getStringExtra("lucky_url");
        System.out.println(topUrl);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.setWebViewClient(new Callback());
        webView.loadUrl(topUrl);
    }

    private class Callback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }
}
