package com.example.android.sunshine;


import android.content.res.AssetManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;

import com.example.android.sunshine.library.App;
import com.example.android.sunshine.library.WearableActivity;
import com.example.android.sunshine.library.model.WeatherData;
import com.example.android.sunshine.presenter.WeatherListPresenter;
import com.example.android.sunshine.presenter.WeatherListView;
import com.example.android.sunshine.weatherpage.PagerAdapter;
import com.example.android.sunshine.weatherpage.WeatherPageDecorator;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.List;
import java.util.Locale;

public class WeatherPagerActivity extends WearableActivity implements
        DataApi.DataListener,
        WeatherListView {

    private WeatherListPresenter presenter;
    private PagerAdapter adapter;

    private Typeface typeface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_list_layout);

        AssetManager am = getApplicationContext().getAssets();
        typeface = Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", "DroidSerif-Regular.ttf"));

        RecyclerViewPager pager = (RecyclerViewPager) findViewById(R.id.pager);
        pager.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new PagerAdapter(getLayoutInflater(), typeface);
        pager.setAdapter(adapter);

        presenter = new WeatherListPresenter(googleApiClient, this);
        presenter.processWeatherCachedData();

        presenter.getNodes(this, googleApiClient);

        pager.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                int bottomInset = insets.getSystemWindowInsetBottom();
//                Log.d(App.TAG, "==========");
//                Log.d(App.TAG, "==========");
//                Log.d(App.TAG, "Bottom offset: " + bottomInset);
//                Log.d(App.TAG, "==========");
//                Log.d(App.TAG, "==========");
//
                Point displaySize = new Point();
                getWindowManager().getDefaultDisplay().getRealSize(displaySize);
//
//                Log.d(App.TAG, "Device height: " + displaySize.y);
//                Log.d(App.TAG, "Bottom offset in percent" + (float) bottomInset / displaySize.y);
                adapter.setDecorator(new WeatherPageDecorator((float) bottomInset / displaySize.y));
                return insets;
            }
        });





//        holder.itemView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
//
//            @Override
//            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
//                int bottomInset = insets.getSystemWindowInsetBottom();
//                Log.d(App.TAG, "Bottom offset: " + bottomInset);
//                // TODO add margin for humidity field
//                PercentFrameLayout.LayoutParams params = (PercentFrameLayout.LayoutParams) holder.humidity.getLayoutParams();
////                PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
////                info.bottomMarginPercent = 0.1f;
//                params.bottomMargin = 68;//bottomInset * 2;
//                holder.humidity.setLayoutParams(params);
//                return insets;
//            }
//
//        });
    }

    @Override
    public void subscribe() {
        Log.d(App.TAG, "[wearable] subscribe");
        Wearable.DataApi.addListener(googleApiClient, this);

        // request update from handset
        presenter.getNodes(this, googleApiClient);
    }

    @Override
    public void unsubscribe() {
        Log.d(App.TAG, "[wearable] unsubscribe");
        Wearable.DataApi.removeListener(googleApiClient, this);
        googleApiClient.disconnect();
    }


    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.d(App.TAG, "[wearable] onDataChanged");
        presenter.processWeatherNewData(dataEventBuffer);
    }

    @Override
    public void showData(List<WeatherData> weatherData) {
        adapter.notifyDatasetChange(weatherData);
    }

}
