package com.coderbunker.mylibrary;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Channel;

import java.io.IOException;
import java.io.OutputStream;


public class SendToWearableTask implements Runnable {

    private GoogleApiClient googleApiClient;
    private Channel channel;

    public SendToWearableTask(GoogleApiClient googleApiClient, Channel channel) {
        this.googleApiClient = googleApiClient;
        this.channel = channel;
    }

    @Override
    public void run() {
        // send data
        PendingResult<Channel.GetOutputStreamResult> outstreamResults = channel.getOutputStream(googleApiClient);
        outstreamResults.setResultCallback(new ResultCallback<Channel.GetOutputStreamResult>() {
            @Override
            public void onResult(@NonNull Channel.GetOutputStreamResult getOutputStreamResult) {
                Log.d(Apps.TAG, "Get result channel");
                sendData(getOutputStreamResult.getOutputStream(), "Hello world!");
            }
        });
    }

    private void sendData(OutputStream os, String data) {
        try {
            os.write(data.getBytes());
        } catch (IOException e) {
            Log.e(Apps.TAG, "Failed to send data");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e(Apps.TAG, "Failed to close the stream");
                }
            }
        }
    }
}
