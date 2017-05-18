package com.example.android.sunshine;


import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.support.wearable.view.WearableRecyclerView;
import android.util.Log;

import com.example.android.sunshine.library.App;
import com.example.android.sunshine.library.model.WeatherData;
import com.example.android.sunshine.library.utils.DataWearInteractor;
import com.example.android.sunshine.presenter.WeatherListPresenter;
import com.example.android.sunshine.presenter.WeatherListView;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

public class MainActivity extends com.example.android.sunshine.library.WearableActivity implements
        WeatherListView,
        WearableAdapter.Listener,
        WearableListView.ClickListener,
        DataApi.DataListener{

    private WeatherListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_activity);

        presenter = new WeatherListPresenter(googleApiClient, this);

        WearableRecyclerView wearableListView = (WearableRecyclerView) findViewById(R.id.wearable_list_view);
        wearableListView.setAdapter(new WearableAdapter(this));
        wearableListView.setCenterEdgeItems(true);

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
//        Toast.makeText(this, "Click on " + viewItemList.get(viewHolder.getLayoutPosition()).text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTopEmptyRegionClick() {

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

    @Override
    public void showData(List<WeatherData> weatherData) {
        WearableRecyclerView wearableListView = (WearableRecyclerView) findViewById(R.id.wearable_list_view);
        WearableAdapter adapter = (WearableAdapter) wearableListView.getAdapter();
        adapter.notifyDatsetChange(weatherData);
    }

    @Override
    public void onClick(WeatherData weatherData) {
        Log.d(App.TAG, "onClick to open detail activity");
        DetailsActivity.start(this);
    }
}
