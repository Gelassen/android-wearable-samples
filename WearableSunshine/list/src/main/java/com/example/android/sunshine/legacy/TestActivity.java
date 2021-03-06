package com.example.android.sunshine.legacy;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.example.android.sunshine.R;
import com.example.android.sunshine.library.App;
import com.example.android.sunshine.library.BaseActivity;
import com.example.android.sunshine.library.WearableActivity;
import com.example.android.sunshine.presenter.WeatherListPresenter;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.Set;

public class TestActivity extends WearableActivity implements
        MessageApi.MessageListener {

    private static final String ADVERT_CHANNEL = "msg";

    private Node node;

    private WeatherListPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        Log.d(App.TAG, "onCreate");

        presenter = new WeatherListPresenter(googleApiClient, null);

        findViewById(R.id.send_node).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(App.TAG, "Send node");
//                processMessageSending(node.getId());
                presenter.getNodes(v.getContext(), googleApiClient);
            }
        });

        findViewById(R.id.find_node).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                createListener();
                Log.d(App.TAG, "Discover nodes");
                PendingResult<NodeApi.GetConnectedNodesResult> pr = Wearable.NodeApi.getConnectedNodes(googleApiClient);
                pr.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(@NonNull NodeApi.GetConnectedNodesResult getLocalNodeResult) {
                        Log.d(App.TAG, "onResult");
                        List<Node> nodes = getLocalNodeResult.getNodes();
                        Log.d(App.TAG, "Nodes amount: " + nodes.size());
                        boolean isThereNodes = nodes.size() != 0;
                        if (isThereNodes) {
                            node = nodes.get(0);
                        }
                    }
                });
            }
        });

        findViewById(R.id.node_emmit)
                .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(App.TAG, "Find emmit nodes");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(App.TAG, "getAvailableNodes inner call");
                        CapabilityApi.GetCapabilityResult result =
                                Wearable.CapabilityApi.getCapability(
                                        googleApiClient, TestActivity.this.getString(R.string.resource_weather_update),
                                        CapabilityApi.FILTER_REACHABLE).await();

                        Set<Node> nodes = result.getCapability().getNodes();
                        Log.d(com.example.android.sunshine.library.App.TAG, "[find] Nodes: " + nodes.size());
                    }
                }).start();
            }

        });
    }

    @Override
    public void subscribe() {
        Wearable.MessageApi.addListener(googleApiClient, this);
        createListener();
    }

    @Override
    public void unsubscribe() {
        Wearable.MessageApi.removeListener(googleApiClient, this);
        googleApiClient.disconnect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(com.example.android.sunshine.library.App.TAG, "onMessageReceived");
    }

    private void createListener() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(App.TAG, "Run listener");
                findNodes();
            }
        }).start();
    }

    public void findNodes() {
        Log.d(com.example.android.sunshine.library.App.TAG, "findNodes");
        CapabilityApi.GetCapabilityResult result =
                Wearable.CapabilityApi.getCapability(
                        googleApiClient, getString(R.string.resource_weather_update),
                        CapabilityApi.FILTER_REACHABLE).await();

        Set<Node> nodes = result.getCapability().getNodes();
        Log.d(com.example.android.sunshine.library.App.TAG, "[find] Nodes: " + nodes.size());
        findBestNode(nodes);

        CapabilityApi.CapabilityListener capabilityListener =
                new CapabilityApi.CapabilityListener() {
                    @Override
                    public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
                        findBestNode(capabilityInfo.getNodes());
                    }
                };

        Wearable.CapabilityApi.addCapabilityListener(
                googleApiClient,
                capabilityListener,
                ADVERT_CHANNEL);

    }

    private void findBestNode(Set<Node> connectedNodes) {
        for (Node node : connectedNodes) {
            this.node = node;
        }
    }

    public void processMessageSending(String nodeId) {
        byte[] msg = "message to sent".getBytes();
        Wearable.MessageApi
                .sendMessage(googleApiClient, nodeId, ADVERT_CHANNEL, msg)
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
