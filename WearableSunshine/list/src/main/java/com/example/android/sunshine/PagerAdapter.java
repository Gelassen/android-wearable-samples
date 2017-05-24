package com.example.android.sunshine;


import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.sunshine.library.model.WeatherData;
import com.example.android.sunshine.library.utils.SunshineDateUtils;
import com.example.android.sunshine.library.utils.SunshineWeatherUtils;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.PageController> {
    private final LayoutInflater inflater;

    private List<WeatherData> dataSource = new ArrayList<>();

    public PagerAdapter(LayoutInflater inflater) {
        this.inflater=inflater;
    }

    @Override
    public PageController onCreateViewHolder(ViewGroup parent, int viewType) {
        PageController pageController = new PageController(inflater.inflate(R.layout.component_item_weather, parent, false));
        return pageController;
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onBindViewHolder(PageController holder, int position) {
        WeatherData weatherData = dataSource.get(position);
        holder.maxTemp.setText(String.valueOf((int) weatherData.getMax()));
        holder.minTemp.setText(String.valueOf((int) weatherData.getMin()));
        holder.date.setText(
                SunshineDateUtils.getFriendlyDateString(holder.itemView.getContext(), weatherData.getDate(), false)
        );
        holder.weatherIcon.setImageResource(
                SunshineWeatherUtils.getSmallArtResourceIdForWeatherCondition(weatherData.getWeatherId()));
        holder.humidity.setText(
                String.format(
                        holder.itemView.getContext().getResources().getString(R.string.humidity),
                        weatherData.getHumidity()) + "%"
        );
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public void notifyDatasetChange(List<WeatherData> weatherData) {
        this.dataSource.clear();
        this.dataSource = weatherData;
        notifyDataSetChanged();
    }


    public class PageController extends RecyclerView.ViewHolder {
        private final TextView maxTemp;
        private final TextView minTemp;
        private final TextView date;
        private final ImageButton weatherIcon;
        private final TextView humidity;

        public PageController(View itemView) {
            super(itemView);
            maxTemp = (TextView) itemView.findViewById(R.id.temp_max);
            minTemp = (TextView) itemView.findViewById(R.id.temp_min);
            date = (TextView) itemView.findViewById(R.id.date);
            weatherIcon = (ImageButton) itemView.findViewById(R.id.weather);
            humidity = (TextView) itemView.findViewById(R.id.condition);
        }

    }
}
