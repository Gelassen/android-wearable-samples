package com.coderbunker.android.sunshine.library.model;


import android.os.Parcel;
import android.os.Parcelable;

public class WeatherData implements Parcelable {

    private float min;
    private float max;
    private int weatherId;
    private float degrees;
    private float wind;
    private float pressure;
    private long date;
    private int humidity;
    private int conditionId;

    public WeatherData() {
    }

    protected WeatherData(Parcel in) {
        min = in.readFloat();
        max = in.readFloat();
        weatherId = in.readInt();
        degrees = in.readFloat();
        wind = in.readFloat();
        pressure = in.readFloat();
        date = in.readLong();
        humidity = in.readInt();
        conditionId = in.readInt();
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public float getDegrees() {
        return degrees;
    }

    public void setDegrees(float degrees) {
        this.degrees = degrees;
    }

    public float getWind() {
        return wind;
    }

    public void setWind(float wind) {
        this.wind = wind;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(min);
        dest.writeFloat(max);
        dest.writeInt(weatherId);
        dest.writeFloat(degrees);
        dest.writeFloat(wind);
        dest.writeFloat(pressure);
        dest.writeLong(date);
        dest.writeInt(humidity);
        dest.writeInt(conditionId);
    }

    public static final Creator<WeatherData> CREATOR = new Creator<WeatherData>() {
        @Override
        public WeatherData createFromParcel(Parcel in) {
            return new WeatherData(in);
        }

        @Override
        public WeatherData[] newArray(int size) {
            return new WeatherData[size];
        }
    };
}
