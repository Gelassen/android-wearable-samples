package com.coderbunker.android.sunshine.sync;


import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.coderbunker.android.sunshine.library.App;

public class SyncService extends JobService {

    private static final int PERIODIC_JOB_ID = 1000;
    private static final long TEN_SECONDS_PERIOD = 10 * 1000;

    public static void scheduleJob(Context context, JobInfo jobInfo) {
        Log.d(App.TAG, "scheduleJob");
        ComponentName componentName = new ComponentName(context, SyncService.class);
        JobInfo test = new JobInfo.Builder(PERIODIC_JOB_ID, componentName)
                .setPeriodic(TEN_SECONDS_PERIOD)
                .build();

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(test);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
//        Log.d(App.TAG, "Start and finish scheduled job");
//        scheduleJob(this, null);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
