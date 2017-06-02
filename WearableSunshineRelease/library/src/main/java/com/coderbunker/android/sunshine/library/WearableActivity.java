package com.coderbunker.android.sunshine.library;


import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

public abstract class WearableActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    protected GoogleApiClient googleApiClient;
    protected Tracker tracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

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
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(App.TAG, "[wearable] onConnected");
        subscribe();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(App.TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(App.TAG, "onConnectionFailed");
        Log.d(App.TAG, "Failed message: " + connectionResult.getErrorMessage());
        Log.d(App.TAG, "Failed code: " + connectionResult.getErrorCode());
        Log.d(App.TAG, "With resolution: " + connectionResult.hasResolution());
        // TODO properly handle error codes {@see https://developers.google.com/android/reference/com/google/android/gms/common/ConnectionResult#SERVICE_VERSION_UPDATE_REQUIRED}
    }

    public abstract void subscribe();

    public abstract void unsubscribe();
}
