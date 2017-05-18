package com.example.android.sunshine.legacy;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageButton;

import com.example.android.sunshine.R;
import com.example.android.sunshine.library.App;

public class DetailsActivity extends Activity {

    public static void start(Context context) {
        Intent intent = new Intent(context, DetailsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        Log.d(App.TAG, "Details activity");

        ImageButton imageButton = (ImageButton) findViewById(R.id.weather);
        imageButton.getDrawable().setLevel(1);
//        imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_storm));

//        findViewById(R.id.weather_details)
//                .setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(App.TAG, "Get weather details");
//                findViewById(R.id.circle).setBackgroundResource(R.drawable.ic_storm);
//            }
//        });
    }
}
