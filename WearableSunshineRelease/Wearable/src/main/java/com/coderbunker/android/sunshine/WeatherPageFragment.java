package com.coderbunker.android.sunshine;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.coderbunker.android.sunshine.library.model.WeatherData;
import com.coderbunker.android.sunshine.library.utils.SunshineDateUtils;
import com.coderbunker.android.sunshine.library.utils.SunshineWeatherUtils;

public class WeatherPageFragment extends Fragment {

    private static final String EXTRA_WEATHER = ".EXTRA_WEATHER";

    private TextView tempMax;
    private TextView tempMin;
    private TextView date;
    private ImageButton icon;

    public static Fragment newInstance(WeatherData weatherData) {
        WeatherPageFragment fragment = new WeatherPageFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_WEATHER, weatherData);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_layout, container, false);
        tempMax = (TextView) view.findViewById(R.id.temp_max);
        tempMin = (TextView) view.findViewById(R.id.temp_min);
        date = (TextView) view.findViewById(R.id.date);
        icon = (ImageButton) view.findViewById(R.id.weather);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        WeatherData weatherData = args.getParcelable(EXTRA_WEATHER);

        tempMin.setText(String.valueOf(weatherData.getMin()));
        tempMax.setText(String.valueOf(weatherData.getMax()));
        date.setText(SunshineDateUtils.getFriendlyDateString(view.getContext(), weatherData.getDate(), false));
        icon.setImageResource(SunshineWeatherUtils.getSmallArtResourceIdForWeatherCondition(weatherData.getWeatherId()));
    }
}
