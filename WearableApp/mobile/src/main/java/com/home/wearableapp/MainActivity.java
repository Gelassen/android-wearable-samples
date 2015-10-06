package com.home.wearableapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(42, getBigStyle());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Notification getBigStyle() {
        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
        bigStyle.bigText("Specify the 'big view' content to display the long event description that may not fit the normal content text.");

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                getResources(), R.mipmap.ic_launcher))
                        .setContentTitle("Hello master!")
                        .setContentText("Where am I?")
                        .setStyle(bigStyle);
        return notificationBuilder.build();
    }

    public Notification getWearableOnly() {
        Intent actionIntent = new Intent(this, MainActivity.class);
        PendingIntent actionPendingIntent =
                PendingIntent.getActivity(this, 0, actionIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.mipmap.ic_launcher,
                        "Label", actionPendingIntent)
                        .build();

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Title")
                        .setContentText("Content")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .extend(new NotificationCompat.WearableExtender().addAction(action))
                        .build();
        return notification;
    }

    public Notification getSampleNotification() {
        Intent wearable_intent = new Intent(this, MainActivity.class);
        PendingIntent wearable_pending_intent = PendingIntent.getActivity(this, 0, wearable_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action wearable_action = new NotificationCompat.Action.Builder(
                android.R.drawable.ic_dialog_email,
                "Email",
                wearable_pending_intent).build();

        NotificationCompat.Builder notification_builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Wake up!")
                .setContentText("Hello Dima!")
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
//                    .setContentIntent(pending_intent)
//                    .addAction(android.R.drawable.ic_dialog_info, "Action", twitter_intent)
                .extend(new NotificationCompat.WearableExtender().addAction(wearable_action));

        return notification_builder.build();
    }

    public Notification getMapNotification() {
        Intent viewIntent = new Intent(this, MainActivity.class);
        viewIntent.putExtra("EXTRA_EVENT_ID", 100);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);


        // Build an intent for an action to view a map
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode("Shanghai"));
        mapIntent.setData(geoUri);
        PendingIntent mapPendingIntent =
                PendingIntent.getActivity(this, 0, mapIntent, 0);

//        Intent wearable_intent = new Intent(this, MainActivity.class);
//        PendingIntent wearable_pending_intent = PendingIntent.getActivity(this, 0, wearable_intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Action wearable_action = new NotificationCompat.Action.Builder(
//                android.R.drawable.ic_dialog_email,
//                "Email",
//                wearable_pending_intent).build();

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentTitle("Event")
                        .setContentText("Location")
                        .setContentIntent(viewPendingIntent)
                        .addAction(R.mipmap.ic_launcher,
                                "Map", mapPendingIntent);
//        .extend(new NotificationCompat.WearableExtender().addAction(wearable_action));
        return notificationBuilder.build();
    }
}
