package com.coderbunker.mylibrary;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static android.support.v4.app.NotificationCompat.*;

public class NotificationHelper {

    private static final String EXTRA_EVENT_ID = "1000";

    public void fireNotification(Context context, Class clazz) {
        int notificationId = 001;
        Intent viewIntent = new Intent(context, clazz);
        int eventId = 1000;
        viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(context, 0, viewIntent, 0);

        Builder notificationBuilder =
                new Builder(context)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle("eventTitle")
                        .setContentText("eventLocation")
                        .setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // Issue the notification with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
