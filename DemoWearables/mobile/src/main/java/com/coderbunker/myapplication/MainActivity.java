package com.coderbunker.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.coderbunker.mylibrary.Apps;
import com.coderbunker.mylibrary.AssetHelper;
import com.coderbunker.mylibrary.NotificationHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private int count = 0;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        findViewById(R.id.fire_notification)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NotificationHelper()
                        .fireNotification(MainActivity.this, MainActivity.class);
            }
        });

        findViewById(R.id.increase_counter)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseCounter();
            }
        });

        findViewById(R.id.sync_image)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendImage();
            }
        });

    }

    // Create a data map and put data in it
    private void increaseCounter() {
        Log.d(App.TAG, "increaseCounter");
        Log.d(App.TAG, "Google api client is connected: " + mGoogleApiClient.isConnected());

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/count");
        putDataMapReq.getDataMap().putInt(Apps.Params.COUNT_KEY, count++);
        putDataMapReq.setUrgent();
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);

        Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
                // TODO receive result
                Log.d(App.TAG, "onResult");
                Log.d(App.TAG, "putDataItem status: "
                        + dataItemResult.getStatus().toString());
            }
        });
    }

    private void sendImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_watermark);
        Asset asset = AssetHelper.createAssetFromBitmap(bitmap);
//        PutDataRequest request = PutDataRequest.create(Apps.Params.IMAGE);
//        request.putAsset(Apps.Params.IMAGE_NAME, asset);

        PutDataMapRequest dataMap = PutDataMapRequest.create(Apps.Params.IMAGE);
        dataMap.setUrgent();
        dataMap.getDataMap().putAsset(Apps.Params.IMAGE_NAME, asset);
        PutDataRequest request = dataMap.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, request);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
                Log.d(App.TAG, "onResult");
                Log.d(App.TAG, "dataMap status: "
                        + dataItemResult.getStatus().toString());
            }
        });

        Wearable.DataApi.putDataItem(mGoogleApiClient, request);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(App.TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(App.TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(App.TAG, "onConnectionFailed");
    }
}
