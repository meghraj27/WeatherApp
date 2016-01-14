package com.meghrajswami.android.weatherapp.repo;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.meghrajswami.android.weatherapp.Config;
import com.meghrajswami.android.weatherapp.model.WeatherResponse;
import com.meghrajswami.android.weatherapp.model.WeatherResponse.CityWeather;

import java.util.List;

/**
 * This Class is for Application specific query code to manage local(sqlite)
 * data related to CityWeather object
 */
public class CityWeatherLocalDS {

    private static final String TAG = "CityWeatherLocalDS";

    private WeatherSQLiteHelper dbHelper;

    public CityWeatherLocalDS(Context context) {
        dbHelper = new WeatherSQLiteHelper(context, Config.SQLITE_DATABASE_NAME,
                null, Config.SQLITE_DATABASE_VERSION);
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * If city values count is different than row count in db than slowly it will sync the data
     * It will run slow only when number of values in string-array resource is changed.
     * If only name is changed in string-array resource and number of items same than use
     * new version of local db so that it update city names
     *
     * @param cities
     * @return
     */
    public boolean initializeDb(List<String> cities) {
        if (cities == null || cities.isEmpty()) {
            return false;
        }
        //check if sync required or not
        if (dbHelper.numberOfRows() == cities.size()) {
            return false;
        }
        Log.d(TAG, "item count in string -array resoure and local db not same," +
                " syncing ... ");
        List<CityWeather> allOriginalItems = dbHelper.getAllItems();
        for (String city : cities) {
            //insert city record in db if not exist
            if (dbHelper.getItem(city) == null) {
                Log.d(TAG, "Inserting " + city + " into Local Db");
                putCityWeather(
                        new CityWeather(city, false));
            }
        }
        //remove city record from db if city don't exist in 'cities',
        // i.e. remove item from db when city name doesn't exist in
        // string-array resource
        for (CityWeather item : allOriginalItems) {
            if (!cities.contains(item.getCity())) {
                Log.d(TAG, "Deleting " + item.getCity() + "from Local Db");
                dbHelper.deleteItem(item.getCity());
            }
        }

        return false;
    }

    public Cursor getAllSelectedItemsCursor() {
        return dbHelper.getAllSelectedItemsCursor();
    }


    public CityWeather getCityWeather(String city) {
        try {
            return dbHelper.getItem(city);
        } finally {
            close();
        }
    }

    public List<CityWeather> getAllCityWeather() {
        try {
            return dbHelper.getAllItems();
        } finally {
            close();
        }
    }

    public List<CityWeather> getAllSelectedCityWeather() {
        try {
            return dbHelper.getAllSelectedItems();
        } finally {
            close();
        }
    }

    public boolean putCityWeather(CityWeather item) {
        try {
            if (dbHelper.getItem(item.getCity()) != null)
                return dbHelper.updateItem(item.getCity(), item);
            else
                return dbHelper.insertItem(item);
        } finally {
            close();
        }
    }

    public boolean putCityWeather(String city, WeatherResponse weatherResponse) {
        if (weatherResponse.getData() != null
                && weatherResponse.getData().getCurrent_condition() != null
                && !weatherResponse.getData().getCurrent_condition().isEmpty()
                && weatherResponse.getData().getCurrent_condition().get(0) != null) {
            WeatherResponse.CityWeather cityWeather
                    = weatherResponse.getData().getCurrent_condition().get(0);
            cityWeather.setCity(city);
            cityWeather.setSelected(true);
            //set current time in millisecs as "updated at" time
            cityWeather.setUpdatedAt(System.currentTimeMillis());
            return putCityWeather(cityWeather);
        }
        return false;
    }

    public boolean isAnyCitySelected() {
        try {
            return dbHelper.isAnyItemSelected();
        } finally {
            close();
        }
    }

    public boolean toggleCitySelected(String city) {
        CityWeather cityWeatherFromDB = dbHelper.getItem(city);
        if (cityWeatherFromDB != null) {
            cityWeatherFromDB.setSelected(!cityWeatherFromDB.isSelected());
            return dbHelper.updateItem(cityWeatherFromDB.getCity(), cityWeatherFromDB);
        }
        return false;
    }


}
