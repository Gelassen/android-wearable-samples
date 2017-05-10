package com.coderbunker.mylibrary;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

public abstract class BaseActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    protected GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(Apps.TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(Apps.TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(Apps.TAG, "onConnectionFailed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscribe();
    }

    protected abstract void subscribe();

    protected abstract void unsubscribe();
}
