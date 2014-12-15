package com.guyjstitt.trender.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.guyjstitt.trender.R;

/**
 * Created by gstitt on 12/15/14.
 */
public class WebFragment extends Fragment {

    private WebView webView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String topUrlFrmActivity = getArguments().getString("topUrl");

        webView = (WebView) getActivity().findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.setWebViewClient(new Callback());
        webView.loadUrl(topUrlFrmActivity);
        return inflater.inflate(R.layout.web_fragment, container, false);
    }

    private class Callback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }
}
