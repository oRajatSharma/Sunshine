package com.example.rajat.sunshine;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private forecastListAdapter mForecastAdapter;
    private static final String TAG="MainActivityFragment";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        String[] forecastArray = {
                "Today - Sunny",
                "Wed - Sunny11",
                "Thu - Sunny12",
                "Fri - Sunny3",
                "Sat - Sunny4",
                "Sun - Sunny5",
                "Mon - Sunny6",
                "Tomorrow - Windy",
                "Wed - Sunny1",
                "Thu - Sunny2",
                "Fri - Sunny3",
                "Sat - Sunny4",
                "Sun - Sunny5",
                "Mon - Sunny6"
        };

        List<String> weekForecast = new ArrayList<>(Arrays.asList(forecastArray));

        mForecastAdapter = new forecastListAdapter(getActivity(),
                R.layout.list_item_forecast,
                weekForecast);

        ListView forecastList = (ListView) getActivity().findViewById(R.id.listview_forecast);
        forecastList.setAdapter(mForecastAdapter);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
