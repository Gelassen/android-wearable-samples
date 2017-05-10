package com.coderbunker.myapplication;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.coderbunker.mylibrary.BaseActivity;
import com.coderbunker.mylibrary.ReadFromDeviceTask;
import com.coderbunker.mylibrary.SendToWearableTask;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.ChannelApi;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.coderbunker.myapplication.App.TAG;

public class ChannelActivity extends BaseActivity implements ChannelApi.ChannelListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(App.TAG, "Thread: " + Thread.currentThread().getId());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);
        // TODO
    }

    @Override
    public void onChannelOpened(final Channel channel) {
        Log.d(TAG, "onChannelOpened");
        Log.d(TAG, "onChannelOpened: A new channel to this device was just opened.\n" +
                "From Node ID" + channel.getNodeId() + "\n" +
                "Path: " + channel.getPath());
        Log.d(TAG, "Thread id: " + Thread.currentThread().getId());

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new ReadFromDeviceTask(mGoogleApiClient, channel));
    }

    @Override
    public void onChannelClosed(final Channel channel, final int closeReason, final int appSpecificErrorCode) {
        Log.d(TAG, "onChannelClosed: " + closeReason + " " + appSpecificErrorCode);
    }

    @Override
    public void onInputClosed(final Channel channel, final int closeReason, final int appSpecificErrorCode) {
        Log.d(TAG, "onInputClosed");
    }

    @Override
    public void onOutputClosed(final Channel channel, final int closeReason, final int appSpecificErrorCode) {
        Log.d(TAG, "onOutputClosed");
    }

    @Override
    protected void subscribe() {
        Wearable.ChannelApi.addListener(mGoogleApiClient, this);
    }

    @Override
    protected void unsubscribe() {
        Wearable.ChannelApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }
}

