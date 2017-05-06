package com.coderbunker.myapplication;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.coderbunker.mylibrary.Apps;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;

public class WearableActivityService extends WearableListenerService implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(App.TAG, "[WearableActivityService] onCreate()");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(App.TAG, "[WearableActivityService] onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(App.TAG, "[WearableActivityService] onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(App.TAG, "[WearableActivityService] onConnectionFailed");
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(App.TAG, "[WearableActivityService] onDataChanged");
        final ArrayList<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        for (DataEvent event : events) {
            PutDataMapRequest putDataMapRequest =
                    PutDataMapRequest.createFromDataMapItem(DataMapItem.fromDataItem(event.getDataItem()));

            String path = event.getDataItem().getUri().getPath();
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                if (Apps.Params.COUNT_KEY.equals(path)) {
                    DataMap data = putDataMapRequest.getDataMap();
                    Log.d(App.TAG, "[WearableActivityService] Data str: " );
                } else if (event.getType() == DataEvent.TYPE_DELETED) {
                    Log.d(App.TAG, "[WearableActivityService] OnDataChanged: deleted event");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(App.TAG, "[WearableActivityService] onDestroy");
    }
}
