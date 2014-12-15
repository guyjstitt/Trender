package com.guyjstitt.trender.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.guyjstitt.trender.R;
import com.guyjstitt.trender.fragment.WebFragment;

/**
 * Created by gstitt on 11/15/14.
 */
public class WebActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);

        Intent intent = getIntent();
        intent.getExtras();
        String topUrl = intent.getStringExtra("lucky_url");
        System.out.println(topUrl);

        Toolbar toolbar = (Toolbar) findViewById(R.id.webviewtoolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = new Bundle();
        bundle.putString("topUrl", topUrl);

        WebFragment frag = new WebFragment();
        frag.setArguments(bundle);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.animation5, R.anim.animation6, 0, 0);
        transaction.add(R.id.webLayout, frag, "Web Fragment");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition( R.anim.animation, R.anim.animation2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.animation3, R.anim.animation4);
            return true;
        }
        return true;
    }
}
