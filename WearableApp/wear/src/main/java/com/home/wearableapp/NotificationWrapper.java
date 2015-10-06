package com.home.wearableapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by dmitry.kazakov on 9/23/2015.
 */
public class NotificationWrapper {

    public static void notify(Context context) {
        Notification notif = new NotificationCompat.Builder(context)
                .setContentTitle("New mail from ")
                .setContentText("subj")
                .extend(new NotificationCompat.WearableExtender()
                    .setDisplayIntent(CustomNotificationActivity.getPendingIntent(context))
                    .setCustomSizePreset(Notification.WearableExtender.SIZE_MEDIUM))
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notif);
    }
}
