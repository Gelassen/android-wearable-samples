package com.example.android.sunshine.legacy;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.sunshine.R;
import com.example.android.sunshine.library.model.WeatherData;
import com.example.android.sunshine.library.utils.SunshineDateUtils;
import com.example.android.sunshine.library.utils.SunshineWeatherUtils;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.PageController> {
    private final RecyclerViewPager pager;
    private final LayoutInflater inflater;

    private List<WeatherData> dataSource = new ArrayList<>();

    public PagerAdapter(RecyclerViewPager pager, LayoutInflater inflater) {
        this.pager=pager;
        this.inflater=inflater;
    }

    @Override
    public PageController onCreateViewHolder(ViewGroup parent, int viewType) {
        PageController pageController = new PageController(inflater.inflate(R.layout.details_layout, parent, false));
        return pageController;
    }

    @Override
    public void onBindViewHolder(PageController holder, int position) {
        WeatherData weatherData = dataSource.get(position);
        holder.maxTemp.setText(String.valueOf(weatherData.getMax()));
        holder.minTemp.setText(String.valueOf(weatherData.getMin()));
        holder.date.setText(
                SunshineDateUtils.getFriendlyDateString(holder.itemView.getContext(), weatherData.getDate(), false)
        );
        holder.weatherIcon.setImageResource(
                SunshineWeatherUtils.getSmallArtResourceIdForWeatherCondition(weatherData.getWeatherId()));
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

        public PageController(View itemView) {
            super(itemView);
            maxTemp = (TextView) itemView.findViewById(R.id.temp_max);
            minTemp = (TextView) itemView.findViewById(R.id.temp_min);
            date = (TextView) itemView.findViewById(R.id.date);
            weatherIcon = (ImageButton) itemView.findViewById(R.id.weather);
        }

    }
}
