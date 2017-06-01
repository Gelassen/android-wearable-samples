package com.example.android.sunshine.legacy;


import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.example.android.sunshine.R;
import com.example.android.sunshine.library.App;
import com.example.android.sunshine.library.WearableActivity;
import com.example.android.sunshine.library.model.WeatherData;
import com.example.android.sunshine.library.utils.DataWearInteractor;
import com.example.android.sunshine.presenter.WeatherListPresenter;
import com.example.android.sunshine.presenter.WeatherListView;
import com.example.android.sunshine.weatherpage.PagerAdapter;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.List;
import java.util.Locale;

public class WeatherListActivity extends WearableActivity implements
        WeatherListView,
        WearableAdapter.Listener,
        WearableListView.ClickListener,
        DataApi.DataListener {

    private WeatherListPresenter presenter;
    private PagerAdapter adapter;

    private Typeface typeface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_list_layout);

        presenter = new WeatherListPresenter(googleApiClient, this);

        AssetManager am = getApplicationContext().getAssets();
        typeface = Typeface.createFromAsset(am, String.format(Locale.US, "font/%s", "DroidSerif-Regular.ttf"));

        RecyclerViewPager pager = (RecyclerViewPager) findViewById(R.id.pager);

        pager.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        adapter = new PagerAdapter(getLayoutInflater(), typeface);
        pager.setAdapter(adapter);

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
    public void showData(List<WeatherData> weatherData) {
        Log.d(App.TAG, "showData: " + weatherData.size());
        adapter.notifyDatasetChange(weatherData);
    }

    @Override
    public void onClick(WeatherData weatherData) {
        Log.d(App.TAG, "onClick to open detail activity");
        DetailsActivity.start(this);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.d(App.TAG, "[wearable] onDataChanged");
        DataWearInteractor eventInteractor = new DataWearInteractor();
        final List<DataEvent> dataEvents = FreezableUtils.freezeIterable(dataEventBuffer);
        for (DataEvent dataEvent : dataEvents) {
            List<WeatherData> data = eventInteractor.processEvent(dataEvent);
            Log.d(App.TAG, "Cache data on wearable side: " + data.size());
            Log.d(App.TAG, "Data event: " + dataEvent.getDataItem().getUri());
        }
    }
}
