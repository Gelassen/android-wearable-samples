package com.coderbunker.android.sunshine.weatherpage;


import android.graphics.Typeface;
import android.support.percent.PercentFrameLayout;
import android.support.percent.PercentLayoutHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.coderbunker.android.sunshine.library.App;

public class WeatherPageDecorator {
    private float chinOffset;
    private float originOffsetInPercent;

    public WeatherPageDecorator(float chinOffset, float originOffsetInPercent) {
        this.chinOffset = chinOffset;
        this.originOffsetInPercent = originOffsetInPercent;
    }

    public void applyOffset(View view, PercentFrameLayout.LayoutParams params) {
        PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
        Log.d(App.TAG, "WeatherPageOffset: " + info.topMarginPercent);
        Log.d(App.TAG, "Origin WeatherPageOffset: " + originOffsetInPercent);
        Log.d(App.TAG, "ChinOffset: " + chinOffset);
        info.topMarginPercent = originOffsetInPercent - (chinOffset / 2);
        view.setLayoutParams(params);
    }

    public void applyTypeface(TextView textView, Typeface typeface) {
        textView.setTypeface(typeface);
    }

}
