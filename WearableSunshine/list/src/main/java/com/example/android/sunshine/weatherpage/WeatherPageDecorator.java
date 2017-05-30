package com.example.android.sunshine.weatherpage;


import android.support.percent.PercentFrameLayout;
import android.support.percent.PercentLayoutHelper;
import android.view.View;

public class WeatherPageDecorator {
    private float chinOffset;

    public WeatherPageDecorator(float chinOffset) {
        this.chinOffset = chinOffset;
    }

    public void apply(View view, PercentFrameLayout.LayoutParams params) {
        PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
        info.topMarginPercent -= (chinOffset / 2);
        view.setLayoutParams(params);
    }
}
