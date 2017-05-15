package com.example.android.sunshine.library.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.android.sunshine.library.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherConverter implements IConverter<ContentValues, WeatherData> {

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_CONDITION_ID = 3;

    private static final String JSON = "json";

    @Override
    public WeatherData convert(ContentValues input) {
        WeatherData weatherData = new WeatherData();
        weatherData.setDate(input.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE));
        weatherData.setHumidity(input.getAsInteger(WeatherContract.WeatherEntry.COLUMN_HUMIDITY));
        weatherData.setDegrees(input.getAsFloat(WeatherContract.WeatherEntry.COLUMN_DEGREES));
        weatherData.setMax(input.getAsFloat(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP));
        weatherData.setMin(input.getAsFloat(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP));
        weatherData.setPressure(input.getAsFloat(WeatherContract.WeatherEntry.COLUMN_PRESSURE));
        weatherData.setWeatherId(input.getAsInteger(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID));
        weatherData.setWind(input.getAsFloat(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED));
        return weatherData;
    }

    @Override
    public List<WeatherData> convert(List<ContentValues> input) {
        List<WeatherData> result = new ArrayList<>();
        for (ContentValues contentValues : input) {
            result.add(convert(contentValues));
        }
        return result;
    }

    @Override
    public JSONObject toJson(WeatherData input) {
        JSONObject json = new JSONObject();
        try {
            json.put(WeatherContract.WeatherEntry.COLUMN_DATE, input.getDate());
            json.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, input.getHumidity());
            json.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, input.getDegrees());
            json.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, input.getMax());
            json.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, input.getMin());
            json.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, input.getPressure());
            json.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, input.getWeatherId());
            json.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, input.getWind());
        } catch (JSONException e) {
            Log.e(App.TAG, "Failed to convert from json", e);
        }
        return json;
    }

    @Override
    public JSONObject toJsonAsList(List<WeatherData> input) {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (WeatherData data : input) {
                jsonArray.put(toJson(data));
            }
            json.put(JSON, jsonArray);
        } catch (JSONException e) {
            Log.e(App.TAG, "Failed to create json object", e);
        }
        return json;
    }

    @Override
    public WeatherData fromJson(JSONObject json) {
        WeatherData weatherData = new WeatherData();
        try {
            weatherData.setDate(json.getLong(WeatherContract.WeatherEntry.COLUMN_DATE));
            weatherData.setHumidity(json.getInt(WeatherContract.WeatherEntry.COLUMN_HUMIDITY));
            weatherData.setDegrees((float) json.getDouble(WeatherContract.WeatherEntry.COLUMN_DEGREES));
            weatherData.setMax((float) json.getDouble(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP));
            weatherData.setMin((float) json.getDouble(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP));
            weatherData.setPressure((float) json.getDouble(WeatherContract.WeatherEntry.COLUMN_PRESSURE));
            weatherData.setWeatherId(json.getInt(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID));
            weatherData.setWind((float) json.getDouble(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED));
        } catch (JSONException e) {
            Log.e(App.TAG, "Failed to obtain data from json", e);
        }
        return weatherData;
    }

    @Override
    public List<WeatherData> fromJsonAsList(String json) {
        List<WeatherData> result = new ArrayList<>();
        try {
            result = fromJsonAsList(new JSONObject(json));
        } catch (JSONException e) {
            Log.e(App.TAG, "Failed to obtain json", e);
        }
        return result;
    }

    @Override
    public List<WeatherData> fromJsonAsList(JSONObject json) {
        List<WeatherData> result = new ArrayList<>();
        try {
            JSONArray jsonArray = json.getJSONArray(JSON);
            final int size = jsonArray.length();
            for (int idx = 0; idx < size; idx++) {
                result.add(
                        fromJson(jsonArray.getJSONObject(idx))
                );
            }
        } catch (JSONException e) {
            Log.e(App.TAG, "Failed to obtain json", e);
        }
        return result;
    }

    @Override
    public WeatherData fromCursor(Cursor cursor) {
        WeatherData result = new WeatherData();
        result.setDate(cursor.getLong(INDEX_WEATHER_DATE));
        result.setMax(cursor.getLong(INDEX_WEATHER_MAX_TEMP));
        result.setMin(cursor.getLong(INDEX_WEATHER_MIN_TEMP));
        result.setWeatherId(cursor.getInt(INDEX_WEATHER_CONDITION_ID));
        return result;
    }

    @Override
    public List<WeatherData> fromCursorAsList(Cursor cursor) {
        Log.d(App.TAG, "[device] cursor count: " + cursor.getCount());
        List<WeatherData> result = new ArrayList<>();
        cursor.moveToFirst();
        do {
            WeatherData item = fromCursor(cursor);
            result.add(item);
        } while (cursor.moveToNext());
        Log.d(App.TAG, "[device] result: " + result.size());
        return result;
    }

}
