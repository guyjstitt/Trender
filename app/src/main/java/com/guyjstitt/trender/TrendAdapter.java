package com.guyjstitt.trender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.guyjstitt.trender.model.TopicModel;

import java.util.List;

/**
 * Created by gstitt on 11/14/14.
 */

//CURRENTLY UNUSED
public class TrendAdapter extends ArrayAdapter<TopicModel> {
    private Context context;
    private List<TopicModel> mTrends;


    public TrendAdapter(Context context, List<TopicModel> mTrends) {
        super(context, R.layout.view_for_each_trend, mTrends);
        this.context = context;
        this.mTrends = mTrends;

    }

    //this is adding the song data to the list_view_for_each_song
    //returns the converted view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_for_each_trend, parent, false);
        }

        final TopicModel trend = mTrends.get(position);

        TextView textViewName = (TextView)convertView.findViewById(R.id.textViewTrendName);
        textViewName.setText(trend.getName());

        return convertView;
    }

    public void setTrendList(List<TopicModel> mTrends) {

        this.mTrends = mTrends;

    }

}
