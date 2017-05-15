package com.example.android.sunshine.library.utils;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.sunshine.library.App;
import com.example.android.sunshine.library.Params;
import com.example.android.sunshine.library.model.WeatherConverter;
import com.example.android.sunshine.library.model.WeatherData;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataWearInteractor {

    public void syncData(GoogleApiClient googleApiClient, JSONObject json) {
        Log.d(App.TAG, "[device] syncData");
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create(Params.WEATHER);
        dataMapRequest.setUrgent(); // TODO remove me
        dataMapRequest.getDataMap()
                .putString(Params.SYNC_WEATHER_LIST, json.toString());

        PutDataRequest putDataRequest = dataMapRequest.asPutDataRequest();
        Wearable.DataApi.putDataItem(googleApiClient, putDataRequest)
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
                Log.d(App.TAG, "Sync data with wearable: status: "
                        + dataItemResult.getStatus().getStatusMessage() + " "
                            + dataItemResult.getStatus().getStatusCode());
            }
        });
    }

    public List<WeatherData> processEvent(DataEvent dataEvent) {
        List<WeatherData> result = new ArrayList<>();
        Log.d(App.TAG, "[wearable] destroy to process new data");
        try {
            if (Params.WEATHER.equals(dataEvent.getDataItem().getUri().getPath())
                    && dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = PutDataMapRequest
                        .createFromDataMapItem(DataMapItem.fromDataItem(dataEvent.getDataItem()))
                        .getDataMap();
                WeatherConverter weatherConverter = new WeatherConverter();
                result = weatherConverter.fromJsonAsList(new JSONObject(dataMap.getString(Params.SYNC_WEATHER_LIST)));
                Log.d(App.TAG, "[wearable] process result: " + result);
            }
        } catch (JSONException e) {
            Log.e(App.TAG, "Failed to obtain json", e);
        }
        Log.d(App.TAG, "[wearable] get result: " + result.size());
        return result;
    }

    // TODO move to specific class like DataNode resource
    public static Uri getUri(final String nodeId, final String resource) {
        final String uriTemplate = "wear://%s%s";
        return Uri.parse(String.format(uriTemplate, nodeId, resource));
    }
}
