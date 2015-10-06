package com.home.wearableapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by dmitry.kazakov on 9/23/2015.
 */
public class CustomNotificationActivity extends Activity {

    public static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, CustomNotificationActivity.class);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
