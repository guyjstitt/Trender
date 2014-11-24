package com.guyjstitt.trender;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 11/24/2014.
 */
public class GlobalState extends Application {

    private static GlobalState singleton;
    private List<TrendData> trendList;

    public static GlobalState getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        singleton.trendList = new ArrayList<TrendData>();
    }

    public List<TrendData> getTrendList() {
        return trendList;
    }

    public void setTrendList(List<TrendData> trendList) {
        this.trendList = trendList;
    }
}
