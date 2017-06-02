package com.coderbunker.android.sunshine.presenter;


import com.coderbunker.android.sunshine.library.model.WeatherData;

import java.util.List;

public interface WeatherListView  {
    void showData(List<WeatherData> weatherData);
}
