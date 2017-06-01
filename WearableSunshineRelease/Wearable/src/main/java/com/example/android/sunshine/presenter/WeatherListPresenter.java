package com.example.android.sunshine.presenter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.sunshine.library.App;
import com.example.android.sunshine.library.Params;
import com.example.android.sunshine.library.model.WeatherConverter;
import com.example.android.sunshine.library.model.WeatherData;
import com.example.android.sunshine.library.utils.DataWearInteractor;
import com.example.android.sunshine.model.Model;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.Set;

import rx.Observer;

public class WeatherListPresenter {
    private GoogleApiClient googleApiClient;
    private WeatherListView view;

    private String channelApi;
    private Model model;
    private Set<Node> nodes;

    public WeatherListPresenter(GoogleApiClient googleApiClient, WeatherListView view) {
        this.googleApiClient = googleApiClient;
        this.view = view;
        this.model = new Model();
        this.channelApi = googleApiClient.getContext().getString(com.example.android.sunshine.library.R.string.resource_weather_update);
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
                        if (view == null) return;
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

    public void processWeatherNewData(DataEventBuffer dataEventBuffer) {
        if (view == null) return;
        DataWearInteractor eventInteractor = new DataWearInteractor();
        final List<DataEvent> dataEvents = FreezableUtils.freezeIterable(dataEventBuffer);
        for (DataEvent dataEvent : dataEvents) {
            List<WeatherData> data = eventInteractor.processEvent(dataEvent);
            if (data != null) {
                view.showData(data);
            }
        }
    }

    public void getNodes(Context context, GoogleApiClient googleApiClient) {
        Log.d(App.TAG, "[getNodes] call");
        model.getAvailableNodes(context, googleApiClient)
                .doOnEach(new Observer<Set<Node>>() {
            @Override
            public void onCompleted() {
                Log.d(App.TAG, "[doOnEach] onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(App.TAG, "[doOnEach] onError", e);
            }

            @Override
            public void onNext(Set<Node> nodes) {
                Log.d(App.TAG, "[doOnEach] onNext: " + nodes.size());
                for (Node node : nodes) {
                    sendMessage(node, "");
                }
            }
        })//;
        .subscribe(new Observer<Set<Node>>() {
            @Override
            public void onCompleted() {
                Log.d(App.TAG, "[getNodes] onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(App.TAG, "[getNodes] onError", e);
            }

            @Override
            public void onNext(Set<Node> availableNodes) {
                Log.d(App.TAG, "[getNodes] onNext " + availableNodes.size());
                nodes = availableNodes;
            }
        });
    }

    private void sendMessage(Node node, String msg) {
        Wearable.MessageApi
                .sendMessage(googleApiClient, node.getId(), channelApi, msg.getBytes())
                .setResultCallback(
                        new ResultCallback() {
                            @Override
                            public void onResult(@NonNull Result result) {
                                Log.d(com.example.android.sunshine.library.App.TAG, "Send message: "
                                        + result.getStatus().isSuccess() + " "
                                        + result.getStatus().getStatusMessage());
                                }
                            }
                    );
    }
}
