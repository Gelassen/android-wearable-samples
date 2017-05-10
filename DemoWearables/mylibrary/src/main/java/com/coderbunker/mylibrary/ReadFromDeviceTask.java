package com.coderbunker.mylibrary;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadFromDeviceTask implements Runnable {

    private GoogleApiClient googleApiClient;
    private Channel channel;

    public ReadFromDeviceTask(GoogleApiClient googleApiClient, Channel channel) {
        this.googleApiClient = googleApiClient;
        this.channel = channel;
    }

    @Override
    public void run() {
        Log.d(Apps.TAG, "Read data from device");
        PendingResult<Channel.GetInputStreamResult> pr = channel.getInputStream(googleApiClient);
        pr.setResultCallback(new ResultCallback<Channel.GetInputStreamResult>() {
            @Override
            public void onResult(@NonNull Channel.GetInputStreamResult getInputStreamResult) {
                Log.d(Apps.TAG, "[wearable] onResult");
                boolean isSuccess = getInputStreamResult.getStatus().isSuccess();
                if (isSuccess) {
                    String data = readData(getInputStreamResult);
                    Log.d(Apps.TAG, "[wearable] data from device " + data);
                } else {
                    Log.d(Apps.TAG, "[wearable] something goes wrong " + getInputStreamResult.getStatus().getStatusMessage());
                }
            }
        });
    }

    private String readData(Channel.GetInputStreamResult getInputStreamResult) {
        String result = "";
        BufferedReader reader = null;
        try {
            InputStreamReader iReader = new InputStreamReader(getInputStreamResult.getInputStream());
            char[] buff = new char[256];
            int i = 0;
            while ((i = iReader.read(buff)) != -1) {
                Log.d(Apps.TAG, "Response: "  + new StringBuilder().append(buff).toString());
            }

            Log.d(Apps.TAG, "Device message: " + result);
        } catch (IOException e) {
            Log.e(Apps.TAG, "Failed to read line from response", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(Apps.TAG, "Failed to close stream", e);
                }
            }
        }
        return result;
    }
}
