package com.example.android.sunshine;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.library.model.WeatherData;
import com.example.android.sunshine.library.utils.SunshineDateUtils;
import com.example.android.sunshine.library.utils.SunshineWeatherUtils;

import java.util.ArrayList;
import java.util.List;

public class WearableAdapter extends RecyclerView.Adapter<WearableAdapter.ViewHolder> {

    private List<WeatherData> data = new ArrayList<>();

    public void notifyDatsetChange(List<WeatherData> data) {
        this.data.clear();
        this.data = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.component_item_wearable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherData weatherData = data.get(position);
        holder.image.setImageResource(
                SunshineWeatherUtils.getSmallArtResourceIdForWeatherCondition(weatherData.getWeatherId()));
        holder.contentDate.setText(
                SunshineDateUtils.getFriendlyDateString(holder.itemView.getContext(), weatherData.getDate(), false));
        holder.contentDegrees.setText(getData(holder.itemView.getContext(), weatherData));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private String getData(Context context, WeatherData weatherData) {
        StringBuilder result = new StringBuilder();
        result.append(" ");
        result.append(weatherData.getMax());
        result.append(" ");
        result.append(weatherData.getMin());
        return result.toString();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView contentDegrees;
        private final TextView contentDate;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            contentDegrees = (TextView) itemView.findViewById(R.id.text_degree);
            contentDate = (TextView) itemView.findViewById(R.id.text_date);
        }
    }
}
