
/*
 * Copyright 2015. Daniel Ramírez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.sunshine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.view.WearableListView;
import android.support.wearable.view.WearableRecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.sunshine.library.App;
import com.example.android.sunshine.library.model.WeatherData;
import com.example.android.sunshine.library.utils.DataWearInteractor;
import com.example.android.sunshine.presenter.WeatherListPresenter;
import com.example.android.sunshine.presenter.WeatherListView;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends com.example.android.sunshine.library.WearableActivity implements
        WeatherListView,
        WearableListView.ClickListener,
        DataApi.DataListener{

    private List<WeatherData> viewItemList = new ArrayList<>();
    private Node node;

    private WeatherListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_activity);

        presenter = new WeatherListPresenter(googleApiClient, this);

        findViewById(R.id.wearable)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PendingResult<NodeApi.GetConnectedNodesResult> pr = Wearable.NodeApi.getConnectedNodes(googleApiClient);
                pr.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(@NonNull NodeApi.GetConnectedNodesResult getLocalNodeResult) {
                        Log.d(App.TAG, "onResult");
                        List<Node> nodes = getLocalNodeResult.getNodes();
                        Log.d(App.TAG, "Nodes amount: " + nodes.size());
                        boolean isThereConnectedNodes = nodes.size() != 0;
                        if (isThereConnectedNodes) {
                            node = nodes.get(0);
                        }
                    }
                });
            }
        });

        findViewById(R.id.cache)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(App.TAG, "Get data from cache: onClick");

                presenter.processWeatherCachedData("f4c00be3");
//                presenter.processWeatherCachedData(googleApiClient);
//
//                final PendingResult<DataApi.DataItemResult> dataItem = Wearable.DataApi.getDataItem(
//                        googleApiClient,
//                        DataWearInteractor.getUri(node.getId(), Params.WEATHER)
//                );
//                dataItem.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
//                    @Override
//                    public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
//                        Log.d(App.TAG, "onResult [cache] " + dataItemResult.getStatus().getStatusMessage());
//                        DataMap dataMap = DataMap.fromByteArray(dataItemResult.getDataItem().getData());
//                        String json = dataMap.getString(Params.SYNC_WEATHER_LIST);
//                        Log.d(App.TAG, "JSON: " + json);
//                    }
//                });
            }
        });

        WearableRecyclerView wearableListView = (WearableRecyclerView) findViewById(R.id.wearable_list_view);
        wearableListView.setAdapter(new ListViewAdapter(this, viewItemList));
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
        wearableListView.setAdapter(new ListViewAdapter(this, weatherData));
    }
}
