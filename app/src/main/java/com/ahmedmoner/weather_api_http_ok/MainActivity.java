package com.ahmedmoner.weather_api_http_ok;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
//https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&exclude=minutely,current,alerts,hourly,minutely,&appid=e394f585f9362d38fe17ea316916094c
//todo http://api.openweathermap.org/data/2.5/weather?q=cairo&&units=metric&appid=e394f585f9362d38fe17ea316916094c
//https://github.com/koush/ion
//https://openweathermap.org/api/one-call-api

public class MainActivity extends AppCompatActivity {

    private static  final String API_KEY = "e394f585f9362d38fe17ea316916094c";
    Button buttonSerarch;
    EditText et_city_name;
    ImageView iconWeather;
    TextView tv_temp, tv_city;
    ListView lvDailyWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        buttonSerarch= findViewById(R.id.btn_search);
        et_city_name= findViewById(R.id. et_city_name);
        iconWeather= findViewById(R.id.iconWeather );
        tv_temp= findViewById(R.id.temp);
        tv_city= findViewById(R.id.citytemp);
        lvDailyWeather=findViewById(R.id.lvDailyWeather);

        buttonSerarch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = et_city_name.getText().toString();
                if (city.isEmpty()){
                    Toast.makeText(MainActivity.this, "enter citiy name ", Toast.LENGTH_SHORT).show();
                }else {
                    //todo
                    loadWeatherByCityName(city);
                }
            }
        });

    }
    private void loadWeatherByCityName(String city) {
        String Url="http://api.openweathermap.org/data/2.5/weather?q="+city+"&&units=metric&appid="+API_KEY;
        Ion.with(this)
                .load(Url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                      if (e!=null){
                           e.printStackTrace();;
                          Toast.makeText(MainActivity.this, "error server", Toast.LENGTH_SHORT).show();
                      }else {
                          JsonObject main =result.get("main").getAsJsonObject();
                          double temp = main.get("temp").getAsDouble();
                          tv_temp.setText(temp+" °C");

                          JsonObject sys =result.get("sys").getAsJsonObject();
                          String country = sys.get("country").getAsString();
                          tv_city.setText(city+"," + "  "+country);

                          JsonArray weather =result.get("weather").getAsJsonArray();

                          String icon = weather.get(0).getAsJsonObject().get("icon").getAsString();
                          loadIcon(icon);

                          JsonObject coord = result.get("coord").getAsJsonObject();
                          double lon = coord.get("lon").getAsDouble();
                          double lat = coord.get("lat").getAsDouble();
                          loadDailyForecast( lon,  lat);

                      }


                    }

                });
}

    private void loadDailyForecast(double lon, double lat) {

        String Url= "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=minutely,current,alerts,hourly,minutely,&appid="+API_KEY;
        Ion.with(this)
                .load(Url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e!=null){
                            e.printStackTrace();;
                            Toast.makeText(MainActivity.this, "error server", Toast.LENGTH_SHORT).show();
                        }else {
                            List<WeatherModel> weatherModels = new ArrayList<>();

                            String timeZome = result.get("timezone").getAsString();

                            JsonArray daily =result.get("daily").getAsJsonArray();
                            for (int i = 1; i < daily.size(); i++) {
                                Long date = daily.get(i).getAsJsonObject().get("dt").getAsLong();

                                Double temp = daily.get(i).getAsJsonObject().get("temp").getAsJsonObject().get("day").getAsDouble();

                                String  icon = daily.get(i).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").getAsString();

                                weatherModels.add(new WeatherModel(date,timeZome,temp,icon));
                            }

                            //attach adapter to list view
                            DailyWeatherAdapter dailyWeatherAdapter = new DailyWeatherAdapter(MainActivity.this,weatherModels);
                            lvDailyWeather.setAdapter(dailyWeatherAdapter);


                        }


                    }

                });
    }

    private void loadIcon(String icon) {
        Ion.with(this)
                .load("http://api.openweathermap.org/img/w/"+icon+".png")
                .intoImageView(iconWeather);
    }
}