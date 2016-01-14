package com.meghrajswami.android.weatherapp.repo;

import android.content.Context;

import com.meghrajswami.android.weatherapp.Config;
import com.meghrajswami.android.weatherapp.model.WeatherResponse;
import com.meghrajswami.android.weatherapp.util.wwo.RequestParams;
import com.meghrajswami.android.weatherapp.util.wwo.WeatherClient;

/**
 * This class is for application requirement specific queries to reduce
 * non relevant code in activity/fragment classes
 */
public class WeatherDataStore {

    private Context context;
    private WeatherClient localWeatherClient;

    public WeatherDataStore(Context context) {
        this.context = context;
        localWeatherClient = new WeatherClient(Config.WWO_API_LOCAL_WEATHER_ENDPOINT);
    }

    public WeatherResponse getWeather(String city) {
        RequestParams requestParams = new RequestParams(Config.WWO_API_KEY);
        requestParams.setQ(city);
        return localWeatherClient.request(requestParams.getQueryString());
    }

}
