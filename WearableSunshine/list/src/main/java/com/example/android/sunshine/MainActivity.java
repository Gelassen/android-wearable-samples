package com.example.android.sunshine;


import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.example.android.sunshine.legacy.DetailsActivity;
import com.example.android.sunshine.legacy.WearableAdapter;
import com.example.android.sunshine.legacy.WeatherGridAdapter;
import com.example.android.sunshine.library.App;
import com.example.android.sunshine.library.model.WeatherData;
import com.example.android.sunshine.presenter.WeatherListPresenter;
import com.example.android.sunshine.presenter.WeatherListView;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

public class MainActivity extends com.example.android.sunshine.library.WearableActivity implements
        WeatherListView,
        WearableAdapter.Listener,
        WearableListView.ClickListener,
        DataApi.DataListener{

    private WeatherListPresenter presenter;
    private WeatherGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        presenter = new WeatherListPresenter(googleApiClient, this);

        GridViewPager wearableListView = (GridViewPager) findViewById(R.id.pager);
        adapter = new WeatherGridAdapter(getFragmentManager());
        wearableListView.setAdapter(adapter);

//        WearableAdapter adapter = new WearableAdapter(this);
//        wearableListView.setAdapter(adapter);
//        wearableListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        wearableListView.setCenterEdgeItems(true);

        presenter.processWeatherCachedData();
    }

    @Override
    public void subscribe() {
        Log.d(App.TAG, "[wearable] subscribe");
        Wearable.DataApi.addListener(googleApiClient, this);
    }

    @Override
    public void unsubscribe() {
        Log.d(App.TAG, "[wearable] unsubscribe");
        Wearable.DataApi.removeListener(googleApiClient, this);
        googleApiClient.disconnect();
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        // no op
    }

    @Override
    public void onTopEmptyRegionClick() {
        // no op
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.d(App.TAG, "[wearable] onDataChanged");
        presenter.processWeatherNewData(dataEventBuffer);
    }

    @Override
    public void showData(List<WeatherData> weatherData) {
        adapter.update(weatherData);
    }

    @Override
    public void onClick(WeatherData weatherData) {
        Log.d(App.TAG, "onClick to open detail activity");
        DetailsActivity.start(this);
    }
}
