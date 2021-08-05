package com.ahmedmoner.weather_api_http_ok;

public class WeatherModel {
    private long date;
    private String timeZomne;
    private Double temp;
    private String icon;

    public WeatherModel(long date, String timeZomne, Double temp, String icon) {
        this.date = date;
        this.timeZomne = timeZomne;
        this.temp = temp;
        this.icon = icon;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTimeZomne() {
        return timeZomne;
    }

    public void setTimeZomne(String timeZomne) {
        this.timeZomne = timeZomne;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
