package com.example.android.sunshine;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;

import com.example.android.sunshine.library.App;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class Utils {

    public static boolean checkGooglePlaySevices(final Activity activity) {
        Log.d(App.TAG, "checkGooglePlaySevices:start");
        final int googlePlayServicesCheck = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        switch (googlePlayServicesCheck) {
            case ConnectionResult.SUCCESS:
                return true;
            case ConnectionResult.SERVICE_DISABLED:
            case ConnectionResult.SERVICE_INVALID:
            case ConnectionResult.SERVICE_MISSING:
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Log.d(App.TAG, "googlePlayServicesCheck: " + googlePlayServicesCheck);
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCheck, activity, 0);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        activity.finish();
                    }
                });
                dialog.show();
        }
        Log.d(App.TAG, "checkGooglePlaySevices:end");
        return false;
    }

}
