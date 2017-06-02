package com.coderbunker.android.sunshine.legacy;


import android.util.Log;

import com.coderbunker.android.sunshine.library.App;
import com.coderbunker.android.sunshine.library.BaseWearableListenerService;
import com.coderbunker.android.sunshine.library.model.WeatherData;
import com.coderbunker.android.sunshine.library.utils.DataWearInteractor;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

public class EventListenerService extends BaseWearableListenerService implements
        DataApi.DataListener {

    private DataWearInteractor eventInteractor;

    @Override
    public void onCreate() {
        super.onCreate();
        eventInteractor = new DataWearInteractor();
    }

    @Override
    public void subscribe() {
        Log.d(App.TAG, "EventListenerService: subscribe on events");
        Wearable.DataApi.addListener(googleApiClient, this);
    }

    @Override
    public void unsubscribe() {
        Log.d(App.TAG, "EventListenerService: unsubscribe on events");
        Wearable.DataApi.removeListener(googleApiClient, this);
        googleApiClient.disconnect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        super.onDataChanged(dataEventBuffer);
        Log.d(App.TAG, "EventListenerService: on data changed");

        final List<DataEvent> dataEvents = FreezableUtils.freezeIterable(dataEventBuffer);
        for (DataEvent dataEvent : dataEvents) {
            List<WeatherData> data = eventInteractor.processEvent(dataEvent);
            Log.d(App.TAG, "Cache data on wearable side: " + data.size());

        }
        // TODO check if there is a way to cache data on wearable side
        // TODO check what is a limit for keeping data in RAM
    }
}
