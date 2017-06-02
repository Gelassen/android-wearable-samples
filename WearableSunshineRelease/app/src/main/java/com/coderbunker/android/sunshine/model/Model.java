package com.coderbunker.android.sunshine.model;


import android.util.Log;

import com.coderbunker.android.sunshine.App;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Model {

    private static final String API_SHARED_RESOURCE = "msg";

    public Observable<Set<Node>> getAvailableNodes(final GoogleApiClient googleApiClient) {
        return Observable.create(
                new Observable.OnSubscribe<Set<Node>>() {

            @Override
            public void call(Subscriber<? super Set<Node>> subscriber) {
                Log.d(App.TAG, "getAvailableNodes:begin");
                subscriber.onStart();
                CapabilityApi.GetCapabilityResult result = Wearable.CapabilityApi.getCapability(
                        googleApiClient, API_SHARED_RESOURCE, CapabilityApi.FILTER_REACHABLE).await();

                Log.d(App.TAG, "get nodes " + result.getCapability().getNodes());

                subscriber.onNext(result.getCapability().getNodes());
                subscriber.onCompleted();
                Log.d(App.TAG, "getAvailableNodes:end");
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
