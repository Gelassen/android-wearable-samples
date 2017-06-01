package com.example.android.sunshine.presenter;


import com.example.android.sunshine.library.model.WeatherData;

import java.util.List;

public interface WeatherListView  {
    void showData(List<WeatherData> weatherData);
}
