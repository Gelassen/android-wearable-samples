package com.example.android.sunshine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.android.sunshine.library.App;
import com.example.android.sunshine.library.WearableActivity;
import com.example.android.sunshine.library.model.WeatherData;
import com.example.android.sunshine.presenter.WeatherListPresenter;
import com.example.android.sunshine.presenter.WeatherListView;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.List;

public class WeatherPagerActivity extends WearableActivity implements
        DataApi.DataListener,
        WeatherListView {

    private WeatherListPresenter presenter;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_list_layout);
        RecyclerViewPager pager = (RecyclerViewPager) findViewById(R.id.pager);
        pager.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new PagerAdapter(getLayoutInflater());
        pager.setAdapter(adapter);

        presenter = new WeatherListPresenter(googleApiClient, this);
        presenter.processWeatherCachedData();

        presenter.getNodes(this, googleApiClient);
    }

    @Override
    public void subscribe() {
        Log.d(App.TAG, "[wearable] subscribe");
        Wearable.DataApi.addListener(googleApiClient, this);

        // request update from handset
        presenter.getNodes(this, googleApiClient);
    }

    @Override
    public void unsubscribe() {
        Log.d(App.TAG, "[wearable] unsubscribe");
        Wearable.DataApi.removeListener(googleApiClient, this);
        googleApiClient.disconnect();
    }


    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.d(App.TAG, "[wearable] onDataChanged");
        presenter.processWeatherNewData(dataEventBuffer);
    }

    @Override
    public void showData(List<WeatherData> weatherData) {
        adapter.notifyDatasetChange(weatherData);
    }

}
