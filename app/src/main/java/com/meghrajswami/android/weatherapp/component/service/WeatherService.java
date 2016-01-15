package com.meghrajswami.android.weatherapp.component.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.meghrajswami.android.weatherapp.Config;
import com.meghrajswami.android.weatherapp.component.Alarm;
import com.meghrajswami.android.weatherapp.model.WeatherResponse;
import com.meghrajswami.android.weatherapp.model.WeatherResponse.CityWeather;
import com.meghrajswami.android.weatherapp.repo.CityWeatherLocalDS;
import com.meghrajswami.android.weatherapp.repo.WeatherDataStore;

import java.util.List;

public class WeatherService extends Service {

    private static final String TAG = "WeatherService";

    public static final String ARG_CITY = "city";

    public WeatherService() {
    }

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private WeatherDataStore weatherDataStore;
    private CityWeatherLocalDS cityWeatherLocalDS;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // if we get city data via intent than update only that citi's data
            //otherwise update all selected cities
            String city = null;
            if (msg.getData() != null)
                city = msg.getData().getString(ARG_CITY);
            if (city != null && !city.equals("")) {
                updateCityWeather(city);
            } else {
                updateAllCityWeather();
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }

        private void updateCityWeather(String city) {
            CityWeather cityWeatherOld = cityWeatherLocalDS.getCityWeather(city);
            if (cityWeatherOld == null)
                return;
            //update city data only if it was not updated for last few minutes,
            //few minutes are defined in millisecs by Config.CAN_SYNC_AFTER_MILLIS
            if ((System.currentTimeMillis() - cityWeatherOld.getUpdatedAt())
                    <= Config.CAN_SYNC_AFTER_MILLIS)
                return;
            WeatherResponse weatherResponse = weatherDataStore.getWeather(city);
            if (weatherResponse == null)
                return;
            //if successfully updated than set Config.PREFS_KEY_LAST_SYNC_TIMESTAMP
            //(with current timestamp) in shared preferences also
            if (cityWeatherLocalDS.putCityWeather(city, weatherResponse))
                updateLastSyncTime();
        }

        private void updateAllCityWeather() {
            List<CityWeather> cityWeatherList = cityWeatherLocalDS.getAllSelectedCityWeather();
            if (cityWeatherList == null || cityWeatherList.isEmpty()) {
                Log.e(TAG, "No selected cities in local datastore");
                return;
            }
            //update weather data for all selected cities one by one
            for (CityWeather cityWeather : cityWeatherList) {
                //update city data only if it was not updated for last few minutes,
                //few minutes are defined in millisecs by Config.CAN_SYNC_AFTER_MILLIS
                if ((System.currentTimeMillis() - cityWeather.getUpdatedAt())
                        <= Config.CAN_SYNC_AFTER_MILLIS)
                    continue;
                String cityLocal = cityWeather.getCity();
                if (cityLocal == null || cityLocal.equals("")) {
                    Log.e(TAG, "City can't be null or empty");
                    continue;
                }
                WeatherResponse weatherResponse = weatherDataStore.getWeather(cityLocal);
                if (weatherResponse == null)
                    continue;
                //if successfully updated than set Config.PREFS_KEY_LAST_SYNC_TIMESTAMP
                //(with current timestamp) in shared preferences also
                if (cityWeatherLocalDS.putCityWeather(cityLocal, weatherResponse))
                    updateLastSyncTime();
            }
            //re-schedule the alarm, i.e.overwrite previous alarm 
            new Alarm().setAlarm(WeatherService.this);
        }

        private void updateLastSyncTime() {
            SharedPreferences sharedPref = WeatherService.this.getSharedPreferences(
                    Config.PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(Config.PREFS_KEY_LAST_SYNC_TIMESTAMP, System.currentTimeMillis());
            // no need to use apply as it is alreay running in background thread
            editor.commit();
        }
    }

    @Override
    public void onCreate() {

        weatherDataStore = new WeatherDataStore(this);
        cityWeatherLocalDS = new CityWeatherLocalDS(this);

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service starting");

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        if (intent != null && intent.hasExtra(ARG_CITY)) {
            String city = intent.getStringExtra(ARG_CITY);
            if (city != null && !city.equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString(ARG_CITY, city);
                msg.setData(bundle);
            }
        }

        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service destroyed");
    }

}
