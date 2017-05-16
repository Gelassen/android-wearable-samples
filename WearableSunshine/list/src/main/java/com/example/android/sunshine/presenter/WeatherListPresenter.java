package com.example.android.sunshine.presenter;


import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.sunshine.library.App;
import com.example.android.sunshine.library.Params;
import com.example.android.sunshine.library.model.WeatherConverter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Wearable;

public class WeatherListPresenter {
    private GoogleApiClient googleApiClient;
    private WeatherListView view;

    public WeatherListPresenter(GoogleApiClient googleApiClient, WeatherListView view) {
        this.googleApiClient = googleApiClient;
        this.view = view;
    }

    public void processWeatherCachedData() {
        PendingResult<DataItemBuffer> dataItems = Wearable.DataApi.getDataItems(googleApiClient);
        dataItems.setResultCallback(new ResultCallback<DataItemBuffer>() {
            @Override
            public void onResult(@NonNull DataItemBuffer dataItems) {
                Log.d(App.TAG, "processWeatherCachedData:onResult: " + dataItems.getCount());
                if (!dataItems.getStatus().isSuccess()) {
                    Log.d(App.TAG, "Request to data in cache is not successful");
                    return;
                }
                processResult(dataItems);
            }

            private void processResult(DataItemBuffer dataItems) {
                for (DataItem dataItem : dataItems) {
                    boolean isWeatherData = Params.WEATHER.equals(dataItem.getUri().getPath());
                    if (isWeatherData) {
                        // process weather data
                        view.showData(
                                new WeatherConverter()
                                        .fromJsonAsList(
                                                DataMap.fromByteArray(dataItem.getData())
                                                        .getString(Params.SYNC_WEATHER_LIST)
                                        )
                        );
                    }
                }
            }
        });
    }

}
