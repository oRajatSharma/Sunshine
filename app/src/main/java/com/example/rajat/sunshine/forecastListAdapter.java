package com.example.rajat.sunshine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ggne0279 on 10/13/2015.
 */
public class forecastListAdapter extends ArrayAdapter<String> {

    Context context;
    List<String> forecastData;
    private static final String TAG = "ForecastListAdapter";

    public forecastListAdapter(Context context, int resource, List<String> forecastData) {
        super(context, resource, forecastData);
        this.context = context;
        this.forecastData = forecastData;
    }

    public View getView(int pos, View convertView, ViewGroup parent) {

        View listItemView;
        listItemView = convertView;

        Log.d(TAG, "getView called with pos " + pos);
        if(listItemView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            listItemView = inflater.inflate(R.layout.list_item_forecast, null);
            Log.d(TAG, "Allocating View at pos " + pos);
        }

        TextView tv = (TextView) listItemView.findViewById(R.id.list_item_forecast_textView);
        tv.setText(forecastData.get(pos));

        ImageView iv = (ImageView) listItemView.findViewById(R.id.list_item_forecast_imageView);
        iv.setImageResource(R.drawable.ic_action_name);
        return listItemView;
    }

}
