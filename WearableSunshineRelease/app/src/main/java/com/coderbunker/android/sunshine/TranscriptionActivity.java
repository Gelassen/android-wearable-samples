package com.coderbunker.android.sunshine;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;

public class TranscriptionActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        MessageApi.MessageListener {

    private static final String VOICE_TRANSCRIPTION_MESSAGE_PATH = "msg";

    private GoogleApiClient mGoogleApiClient;
    private String transcriptionNodeId = null;

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

//    private void setupVoiceTranscription() {
//        Log.d(App.TAG, "setupVoiceTranscription");
//        Executor executor = Executors.newSingleThreadExecutor();
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                CapabilityApi.GetCapabilityResult result =
//                        Wearable.CapabilityApi.getCapability(
//                                mGoogleApiClient, VOICE_TRANSCRIPTION_CAPABILITY_NAME,
//                                CapabilityApi.FILTER_REACHABLE).await();
//
//                Log.d(App.TAG, "Nodes: " + result.getCapability().getNodes().size());
//
//                updateTranscriptionCapability(result.getCapability());
//
//                CapabilityApi.CapabilityListener capabilityListener =
//                        new CapabilityApi.CapabilityListener() {
//                            @Override
//                            public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
//                                updateTranscriptionCapability(capabilityInfo);
//                            }
//                        };
//
//                Wearable.CapabilityApi.addCapabilityListener(
//                        mGoogleApiClient,
//                        capabilityListener,
//                        VOICE_TRANSCRIPTION_CAPABILITY_NAME);
//            }
//        });
//    }

    private void updateTranscriptionCapability(CapabilityInfo capabilityInfo) {
        Set<Node> connectedNodes = capabilityInfo.getNodes();
        transcriptionNodeId = pickBestNodeId(connectedNodes);
    }

    private String pickBestNodeId(Set<Node> nodes) {
        Log.d(App.TAG, "Amount of nodes: " + nodes.size());
        String bestNodeId = null;
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        Log.d(App.TAG, "BestNodeId: " + bestNodeId);
        return bestNodeId;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(App.TAG, "onConnected");
//        setupVoiceTranscription();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(App.TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(App.TAG, "onConnectionFailed");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(App.TAG, "onMessageReceived");
        if (messageEvent.getPath().equals(VOICE_TRANSCRIPTION_MESSAGE_PATH)) {
            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startIntent.putExtra("VOICE_DATA", messageEvent.getData());
            startActivity(startIntent);
        }

    }

//    private void requestTranscription(byte[] voiceData) {
//        Log.d(App.TAG, "requestTranscription for id: " + transcriptionNodeId);
//        if (transcriptionNodeId != null) {
//            Wearable.MessageApi
//                    .sendMessage(mGoogleApiClient, transcriptionNodeId, VOICE_TRANSCRIPTION_MESSAGE_PATH, voiceData)
//                    .setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
//                        @Override
//                        public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
//                            if (!sendMessageResult.getStatus().isSuccess()) {
//                                // Failed to send message
//                                Log.d(App.TAG, "Failed to send message");
//                            } else {
//                                Log.d(App.TAG, "Success to send message");
//                            }
//                        }
//                    }
//            );
//        } else {
//            // Unable to retrieve node with transcription capability
//            Log.d(App.TAG, "Unable to retrieve node with transcription capability");
//        }
//    }

}
