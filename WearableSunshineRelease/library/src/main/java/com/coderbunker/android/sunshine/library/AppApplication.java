package com.coderbunker.android.sunshine.library;


import android.app.Application;

import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class AppApplication extends Application {

    private static GoogleAnalytics analytics;
    private static Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();

        analytics = GoogleAnalytics.getInstance(this);
        Thread.UncaughtExceptionHandler myHandler = new ExceptionReporter(
                getDefaultTracker(),
                Thread.getDefaultUncaughtExceptionHandler(),
                this);

        // Make myHandler the new default uncaught exception handler.
        Thread.setDefaultUncaughtExceptionHandler(myHandler);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (tracker == null) {
            tracker = analytics.newTracker(R.xml.global_tracking);
            tracker.enableAutoActivityTracking(true);
        }
        return tracker;
    }

}
