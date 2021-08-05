package com.ahmedmoner.weather_api_http_ok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.koushikdutta.ion.Ion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DailyWeatherAdapter extends ArrayAdapter<WeatherModel> {
    private Context context;
    private List <WeatherModel>weatherModelList;


    public DailyWeatherAdapter(@NonNull Context context,    List<WeatherModel> weatherModelList) {
        super(context, 0, weatherModelList);
        this.context = context;
        this.weatherModelList = weatherModelList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_weather,parent,false);

        TextView tvDate =convertView.findViewById(R.id.tv_date);
        TextView tvTemp =convertView.findViewById(R.id.tv_temp);
        ImageView iconWeather =convertView.findViewById(R.id.iconWeather);

        WeatherModel  weatherModel = weatherModelList.get(position);


        tvTemp.setText(weatherModel.getTemp()+"°C");

        Ion.with(context)
                .load("http://api.openweathermap.org/img/w/"+weatherModel.getIcon()+".png")
                .intoImageView(iconWeather);

        Date date = new Date(weatherModel.getDate()*1000);
        DateFormat dateFormat = new SimpleDateFormat("EEE,  MMM , yy, ", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone(weatherModel.getTimeZomne()));
        tvDate.setText(dateFormat.format(date));


        return convertView;
    }

}
