package com.meghrajswami.android.weatherapp;

/**
 * Configuration class to hold constants to use all over the application
 */
public class Config {

    public static final int ANIMATION_NONE = 0;
    public static final int ANIMATION_RIGHT_TO_LEFT = 1;
    public static final int ANIMATION_LEFT_TO_RIGHT = 2;
    public static final int ANIMATION_BOTTOM_TO_UP = 3;
    public static final int ANIMATION_UP_TO_BOTTOM = 4;

    //sqlite database configuration
    public static final String SQLITE_DATABASE_NAME = "weather";
    public static final int SQLITE_DATABASE_VERSION = 1;

    //common shared preferences across application
    public static final String PREFS_NAME = "common_prefs";
    public static final String PREFS_KEY_LAST_SYNC_TIMESTAMP = "last_sync_timestamp";
    //time in milliseconds after which if main activity is launched than
    // local data will be updated with latest data from internet
    public static final long CAN_SYNC_AFTER_MILLIS = 1000*60*15; // equal to 15 minutes

    public static final int REPEAT_AFTER_MILLIS = 1000*60*10; // Millisec * Second * Minute

    //API endpoint for LocalWeather API of WorldWeatherOnline
    public static final String WWO_API_LOCAL_WEATHER_ENDPOINT
            = "http://api.worldweatheronline.com/free/v2/weather.ashx";

    public static final String WWO_API_KEY
            = "e09a714d0204c5848564157818cbe";

//    public static final String WWO_API_KEY
//            = "f6d664b2b14a6992adc0f7f2a8a44";

    //default request parameters for LocalWeather API request
    public static final int PARAM_NUM_OF_DAYS = 1;
    public static final String PARAM_FX = "no";
    public static final String PARAM_SHOW_COMMENTS = "no";
    public static final String PARAM_FORMAT = "json";

    //name of the event name for local broadcast that is triggered upon
    // any content change in cityweather sqlite table
    public static final String EVENT_TABLE_CITY_WEATHER_CHANGED = "city_weather_changed";


}
