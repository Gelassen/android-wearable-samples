package com.coderbunker.myapplication;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.coderbunker.mylibrary.Apps;
import com.coderbunker.mylibrary.BaseActivity;
import com.coderbunker.mylibrary.ReadFromDeviceTask;
import com.coderbunker.mylibrary.SendToWearableTask;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.ChannelApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChannelActivity extends BaseActivity implements ChannelApi.ChannelListener {

    private Node node;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        findViewById(R.id.channel_data)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(App.TAG, "Send data via channel");
                if (node == null) {
                    Log.d(App.TAG, "There is no node nearby");
                    return;
                }
                PendingResult<ChannelApi.OpenChannelResult> pr = Wearable.ChannelApi.openChannel(mGoogleApiClient, node.getId(), Apps.Params.NODE_PATH);
                pr.setResultCallback(new ResultCallback<ChannelApi.OpenChannelResult>() {
                    @Override
                    public void onResult(@NonNull ChannelApi.OpenChannelResult openChannelResult) {
                        Log.d(App.TAG, "[wearable] channel : onResult : " + openChannelResult.getStatus().getStatusMessage());
                        final Channel channel = openChannelResult.getChannel();
                        channel.getInputStream(mGoogleApiClient).setResultCallback(new ResultCallback<Channel.GetInputStreamResult>() {
                            @Override
                            public void onResult(@NonNull Channel.GetInputStreamResult getInputStreamResult) {
                                Executor executor = Executors.newSingleThreadExecutor();
                                executor.execute(new SendToWearableTask(mGoogleApiClient, channel));
                            }
                        });
                    }
                });
            }
        });

        findViewById(R.id.channel_nodes)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(App.TAG, "Discover nodes");
                PendingResult<NodeApi.GetConnectedNodesResult> pr = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
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
    }

    @Override
    protected void subscribe() {
        Wearable.ChannelApi.addListener(mGoogleApiClient, this);
    }

    @Override
    protected void unsubscribe() {
        Wearable.ChannelApi.removeListener(mGoogleApiClient, this);
    }

    @Override
    public void onChannelOpened(Channel channel) {

    }

    @Override
    public void onChannelClosed(Channel channel, int i, int i1) {

    }

    @Override
    public void onInputClosed(Channel channel, int i, int i1) {

    }

    @Override
    public void onOutputClosed(Channel channel, int i, int i1) {
        Log.d(App.TAG, "onOutputClosed");
    }
}
