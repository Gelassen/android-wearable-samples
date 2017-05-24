package com.example.android.sunshine.model;


import android.content.Context;
import android.util.Log;

import com.example.android.sunshine.R;
import com.example.android.sunshine.library.App;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Model {

    private static final String ADVERT_CHANNEL = "msg";

    private Node node;

    public Observable<Set<Node>> getAvailableNodes(final Context context, final GoogleApiClient googleApiClient) {
        Log.d(App.TAG, "getAvailableNodes call");
        return Observable.create(
                new Observable.OnSubscribe<Set<Node>>() {

            @Override
            public void call(Subscriber<? super Set<Node>> subscriber) {
                Log.d(App.TAG, "getAvailableNodes inner call");
                CapabilityApi.GetCapabilityResult result =
                        Wearable.CapabilityApi.getCapability(
                                googleApiClient, context.getString(R.string.resource_weather_update),
                                CapabilityApi.FILTER_REACHABLE).await();

                Set<Node> nodes = result.getCapability().getNodes();
                Log.d(com.example.android.sunshine.library.App.TAG, "[find] Nodes: " + nodes.size());
                findBestNode(nodes);

                CapabilityApi.CapabilityListener capabilityListener =
                        new CapabilityApi.CapabilityListener() {
                            @Override
                            public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
                                findBestNode(capabilityInfo.getNodes());
                            }
                        };

                Wearable.CapabilityApi.addCapabilityListener(
                        googleApiClient,
                        capabilityListener,
                        ADVERT_CHANNEL);

                subscriber.onStart();
                Log.d(App.TAG, "get nodes " + result.getCapability().getNodes());
                subscriber.onNext(result.getCapability().getNodes());
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void findBestNode(Set<Node> connectedNodes) {
        for (Node node : connectedNodes) {
            this.node = node;
        }
    }
}
