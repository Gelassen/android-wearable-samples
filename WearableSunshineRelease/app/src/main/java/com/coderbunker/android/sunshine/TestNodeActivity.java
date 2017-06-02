package com.coderbunker.android.sunshine;


import android.util.Log;

import com.coderbunker.android.sunshine.library.BaseActivity;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TestNodeActivity extends BaseActivity implements
        MessageApi.MessageListener {

    private static final String VOICE_TRANSCRIPTION_CAPABILITY_NAME = "msg";

    @Override
    public void subscribe() {
        Log.d(App.TAG, "subscribe");
        Wearable.MessageApi.addListener(googleApiClient, this);
        setupVoiceTranscription();
    }

    @Override
    public void unsubscribe() {
        Log.d(App.TAG, "unsubscribe");
        Wearable.MessageApi.removeListener(googleApiClient, this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(App.TAG, "onMessageReceived");
    }

    private void setupVoiceTranscription() {
        Log.d(App.TAG, "setupVoiceTranscription");
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                CapabilityApi.GetCapabilityResult result =
                        Wearable.CapabilityApi.getCapability(
                                googleApiClient, VOICE_TRANSCRIPTION_CAPABILITY_NAME,
                                CapabilityApi.FILTER_REACHABLE).await();

                Log.d(App.TAG, "Nodes: " + result.getCapability().getNodes().size());

//                updateTranscriptionCapability(result.getCapability());

                CapabilityApi.CapabilityListener capabilityListener =
                        new CapabilityApi.CapabilityListener() {
                            @Override
                            public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
//                                updateTranscriptionCapability(capabilityInfo);
                                Log.d(App.TAG, "onCapabilityChanged");
                            }
                        };

                Wearable.CapabilityApi.addCapabilityListener(
                        googleApiClient,
                        capabilityListener,
                        VOICE_TRANSCRIPTION_CAPABILITY_NAME);
            }
        });
    }
}
