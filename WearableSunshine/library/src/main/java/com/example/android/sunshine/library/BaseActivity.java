package com.example.android.sunshine.library;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

public abstract class BaseActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    protected GoogleApiClient googleApiClient;
    protected Tracker tracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppApplication application = (AppApplication) getApplication();
        tracker = application.getDefaultTracker();

        // Set screen name.
        tracker.setScreenName(BaseActivity.class.getName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        Log.d(App.TAG, "Send data to the server");

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(App.TAG, "onConnected");
        subscribe();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(App.TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(App.TAG, "onConnectionFailed");
    }

    public abstract void subscribe();

    public abstract void unsubscribe();
}
