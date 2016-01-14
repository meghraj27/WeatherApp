package com.meghrajswami.android.weatherapp.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.meghrajswami.android.weatherapp.model.WeatherResponse.CityWeather;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is for managing "weather" table in sqlite database
 */
public class CityWeatherSQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "CityWeatherSQLiteHelper";

    private static CityWeatherSQLiteHelper mInstance = null;

    //CityWeather object related fields, which will be persisted
    //to sqlite db table
    private static final String KEY_CITY = "city";
    private static final String KEY_SELECTED = "selected";
    private static final String KEY_UPDATED_AT = "updatedAt";
    private static final String KEY_OBSERVATION_TIME = "observation_time";
    private static final String KEY_TEMP_C = "temp_C";
    private static final String KEY_TEMP_F = "temp_F";
    private static final String KEY_FEELS_LIKE_C = "FeelsLikeC";
    private static final String KEY_FEELS_LIKE_F = "FeelsLikeF";
    private static final String KEY_WEATHER_CODE = "weatherCode";
    private static final String KEY_WEATHER_ICON_URL = "weatherIconUrl";
    private static final String KEY_WEATHER_DESC = "weatherDesc";
    private static final String KEY_WIND_SPEED_MILES = "windspeedMiles";
    private static final String KEY_WIND_SPEED_KMPH = "windspeedKmph";
    private static final String KEY_WIND_DIR_DEGREE = "winddirDegree";
    private static final String KEY_WIND_DIR_16_POINT = "winddir16Point";
    private static final String KEY_PRECIP_MM = "precipMM";
    private static final String KEY_PRECIP_INCHES = "precipInches";
    private static final String KEY_HUMIDITY = "humidity";
    private static final String KEY_VISIBILITY = "visibility";
    private static final String KEY_VISIBILITY_MILES = "visibilityMiles";
    private static final String KEY_PRESSURE = "pressure";
    private static final String KEY_PRESSURE_INCHES = "pressureInches";
    private static final String KEY_CLOUD_COVER = "cloudcover";

    private static final int DATABASE_VERSION = 1;
    private static final String WEATHER_TABLE_NAME = "city_weather";
    private static final String WEATHER_TABLE_CREATE =
            "CREATE TABLE " + WEATHER_TABLE_NAME + " (" +
                    KEY_CITY + " TEXT, " +
                    KEY_SELECTED + " INTEGER, " +
                    KEY_UPDATED_AT + " INTEGER, " +
                    KEY_OBSERVATION_TIME + " TEXT, " +
                    KEY_TEMP_C + " INTEGER, " +
                    KEY_TEMP_F + " INTEGER, " +
                    KEY_FEELS_LIKE_C + " INTEGER, " +
                    KEY_FEELS_LIKE_F + " INTEGER, " +
                    KEY_WEATHER_CODE + " INTEGER, " +
                    KEY_WEATHER_ICON_URL + " TEXT, " +
                    KEY_WEATHER_DESC + " TEXT, " +
                    KEY_WIND_SPEED_MILES + " TEXT, " +
                    KEY_WIND_SPEED_KMPH + " TEXT, " +
                    KEY_WIND_DIR_DEGREE + " INTEGER, " +
                    KEY_WIND_DIR_16_POINT + " TEXT, " +
                    KEY_PRECIP_MM + " INTEGER, " +
                    KEY_PRECIP_INCHES + " INTEGER, " +
                    KEY_HUMIDITY + " REAL, " +
                    KEY_VISIBILITY + " INTEGER, " +
                    KEY_VISIBILITY_MILES + " INTEGER, " +
                    KEY_PRESSURE + " INTEGER, " +
                    KEY_PRESSURE_INCHES + " REAL, " +
                    KEY_CLOUD_COVER + " REAL);";
    private static final String SET_INDEX_CITY =
            "CREATE INDEX " + WEATHER_TABLE_NAME + "_" + KEY_CITY + "_idx" +
                    " ON " + WEATHER_TABLE_NAME + "(" + KEY_CITY + ");";
    private static final String SET_INDEX_SELECTED =
            "CREATE INDEX " + WEATHER_TABLE_NAME + "_" + KEY_SELECTED + "_idx" +
                    " ON " + WEATHER_TABLE_NAME + "(" + KEY_SELECTED + ");";
    private String[] columnNames = {KEY_CITY,
            KEY_SELECTED,
            KEY_UPDATED_AT,
            KEY_OBSERVATION_TIME,
            KEY_TEMP_C,
            KEY_TEMP_F,
            KEY_FEELS_LIKE_C,
            KEY_FEELS_LIKE_F,
            KEY_WEATHER_CODE,
            KEY_WEATHER_ICON_URL,
            KEY_WEATHER_DESC,
            KEY_WIND_SPEED_MILES,
            KEY_WIND_SPEED_KMPH,
            KEY_WIND_DIR_DEGREE,
            KEY_WIND_DIR_16_POINT,
            KEY_PRECIP_MM,
            KEY_PRECIP_INCHES,
            KEY_HUMIDITY,
            KEY_VISIBILITY,
            KEY_VISIBILITY_MILES,
            KEY_PRESSURE,
            KEY_PRESSURE_INCHES,
            KEY_CLOUD_COVER};

    private CityWeatherSQLiteHelper(Context context, String name,
                                    SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static CityWeatherSQLiteHelper getInstance(Context context, String name,
                                                  SQLiteDatabase.CursorFactory factory, int version) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (mInstance == null) {
            mInstance = new CityWeatherSQLiteHelper(context.getApplicationContext(), name,
                    factory, version);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WEATHER_TABLE_CREATE);
        db.execSQL(SET_INDEX_CITY);
        db.execSQL(SET_INDEX_SELECTED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + WEATHER_TABLE_NAME);
        onCreate(db);
    }


    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        //we want all the columns, so use '*' (asterisk) with rawQuery
        //to simplify the code
        Cursor res = db.rawQuery("select * from " + WEATHER_TABLE_NAME
                + " where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, WEATHER_TABLE_NAME);
        return numRows;
    }

    public int numberOfRowsSelected() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, WEATHER_TABLE_NAME,
                "WHERE " + KEY_SELECTED + "=?", new String[]{"1"});
        return numRows;
    }


    /**
     * Returns first CityWeather object if exist, else null
     *
     * @param city city name of the object saved
     * @return CityWeather object, if exist, else null
     */
    public CityWeather getItem(String city) {
        CityWeather cityWeather = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from " + WEATHER_TABLE_NAME +
                        " WHERE " + KEY_CITY + "=?",
                new String[]{city});
        cursor.moveToFirst();

        //loop not required because we want only first row if any, else null
        if (!cursor.isAfterLast()) {
            cityWeather = fetchItemFromCursor(cursor);
        }
        cursor.close();
        return cityWeather;
    }

    public List<CityWeather> getItems(String[] cities) {
        if (cities == null)
            return null;
        List<CityWeather> items = new ArrayList<CityWeather>();
        String citiesCommaSeparated = "";
        for (String city : cities) {
            if (citiesCommaSeparated.equals(""))
                citiesCommaSeparated += city;
            else
                citiesCommaSeparated += ", " + city;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from " + WEATHER_TABLE_NAME +
                        " WHERE " + KEY_CITY + " IN (?)",
                new String[]{citiesCommaSeparated});
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            items.add(fetchItemFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return items;
    }

    public boolean insertItem(CityWeather item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(WEATHER_TABLE_NAME, null, createContentValues(item));
        return true;
    }

    public boolean updateItem(String city, CityWeather item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(WEATHER_TABLE_NAME, createContentValues(item),
                KEY_CITY + " = ? ", new String[]{city});
        return true;
    }

    public Integer deleteItem(String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(WEATHER_TABLE_NAME,
                KEY_CITY + " = ? ",
                new String[]{city});
    }

    /**
     * It will return all the items from database as
     * List<CityWeather>. It should be used only if you
     * are sure that lesser number of rows are present in database
     *
     * @return return all the items as a List
     */
    public List<CityWeather> getAllItems() {
        List<CityWeather> items = new ArrayList<CityWeather>();
        SQLiteDatabase db = this.getReadableDatabase();
        //we want all the columns, so use '*' (asterisk) with rawQuery
        //to simplify the code
        Cursor cursor = db.rawQuery("select * from " + WEATHER_TABLE_NAME, null);
//        Cursor cursor = db.query(WEATHER_TABLE_NAME, columnNames,
//                  null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            items.add(fetchItemFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return items;
    }

    /**
     * It will return all the selected CityWeather items from database as
     * List<CityWeather>. It should be used only if you
     * are sure that lesser number of rows are present in database
     *
     * @return return all the items as a List
     */
    public List<CityWeather> getAllSelectedItems() {
        List<CityWeather> items = new ArrayList<CityWeather>();
        SQLiteDatabase db = this.getReadableDatabase();
        //we want all the columns, so use '*' (asterisk) with rawQuery
        //to simplify the code
        Cursor cursor = db.rawQuery("select * from " + WEATHER_TABLE_NAME +
                " WHERE " + KEY_SELECTED + " != ?", new String[]{"0"});
//        Cursor cursor = db.query(WEATHER_TABLE_NAME, columnNames,
//                  null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            items.add(fetchItemFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return items;
    }

    public Cursor getAllSelectedItemsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        //we want all the columns, so use '*' (asterisk) with rawQuery
        //to simplify the code
        return db.rawQuery("select * from " + WEATHER_TABLE_NAME +
                " WHERE " + KEY_SELECTED + " != ?", new String[]{"0"});
//        Cursor cursor = db.query(WEATHER_TABLE_NAME, columnNames,
//                  null, null, null, null, null);
    }


    /**
     * It will return true if even a single item is selected
     * to show weather info
     *
     * @return return true or false
     */
    public boolean isAnyItemSelected() {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from " + WEATHER_TABLE_NAME +
//                " WHERE " + KEY_SELECTED + " != ?", new String[]{"0"});
            cursor = db.query(WEATHER_TABLE_NAME, new String[]{KEY_SELECTED},
                    KEY_SELECTED + " = ?", new String[]{"1"},
                    null, null, "1");
            Log.d("Cursor count", "" + cursor.getCount());
            if (cursor.getCount() > 0)
                return true;
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    /**
     * It create CityWeather object from current row in cursor.
     *
     * @param cursor the cursor which is holding current row
     * @return CityWeather object created from current row
     */
    public static CityWeather fetchItemFromCursor(Cursor cursor) {
        // TODO: 1/11/2016 finding column index by string is slow,
        // replace with some int based solution
        CityWeather cityWeather = new CityWeather();
        cityWeather.setCity(cursor.getString(cursor.getColumnIndex(KEY_CITY)));
        //int to boolean
        cityWeather.setSelected(cursor.getInt(cursor.getColumnIndex(KEY_SELECTED)) > 0);
        cityWeather.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(KEY_UPDATED_AT)));
        cityWeather.setObservation_time(cursor.getString(cursor.getColumnIndex(KEY_OBSERVATION_TIME)));
        cityWeather.setTemp_C(cursor.getInt(cursor.getColumnIndex(KEY_TEMP_C)));
        cityWeather.setTemp_F(cursor.getInt(cursor.getColumnIndex(KEY_TEMP_F)));
        cityWeather.setFeelsLikeC(cursor.getInt(cursor.getColumnIndex(KEY_FEELS_LIKE_C)));
        cityWeather.setFeelsLikeF(cursor.getInt(cursor.getColumnIndex(KEY_FEELS_LIKE_F)));
        cityWeather.setWeatherCode(cursor.getInt(cursor.getColumnIndex(KEY_WEATHER_CODE)));
        cityWeather.setWeatherIconUrl(cursor.getString(cursor.getColumnIndex(KEY_WEATHER_ICON_URL)));
        cityWeather.setWeatherDesc(cursor.getString(cursor.getColumnIndex(KEY_WEATHER_DESC)));
        cityWeather.setWindspeedMiles(cursor.getInt(cursor.getColumnIndex(KEY_WIND_SPEED_MILES)));
        cityWeather.setWindspeedKmph(cursor.getInt(cursor.getColumnIndex(KEY_WIND_SPEED_KMPH)));
        cityWeather.setWinddirDegree(cursor.getInt(cursor.getColumnIndex(KEY_WIND_DIR_DEGREE)));
        cityWeather.setWinddir16Point(cursor.getString(cursor.getColumnIndex(KEY_WIND_DIR_16_POINT)));
        cityWeather.setPrecipMM(cursor.getInt(cursor.getColumnIndex(KEY_PRECIP_MM)));
        cityWeather.setPrecipInches(cursor.getFloat(cursor.getColumnIndex(KEY_PRECIP_INCHES)));
        cityWeather.setHumidity(cursor.getFloat(cursor.getColumnIndex(KEY_HUMIDITY)));
        cityWeather.setVisibility(cursor.getInt(cursor.getColumnIndex(KEY_VISIBILITY)));
        cityWeather.setVisibilityMiles(cursor.getInt(cursor.getColumnIndex(KEY_VISIBILITY_MILES)));
        cityWeather.setPressure(cursor.getInt(cursor.getColumnIndex(KEY_PRESSURE)));
        cityWeather.setPrecipInches(cursor.getFloat(cursor.getColumnIndex(KEY_PRESSURE_INCHES)));
        cityWeather.setCloudcover(cursor.getInt(cursor.getColumnIndex(KEY_CLOUD_COVER)));

        return cityWeather;
    }

    /**
     * It creates ContentValues object from CityWeather object
     * by fetching it's fields data one by one. It can be used to
     * insert and update item.
     *
     * @param item source object
     * @return ContentValues object
     */
    private ContentValues createContentValues(CityWeather item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CITY, item.getCity());
        //boolean should be saved as int in sqlite
        contentValues.put(KEY_SELECTED, item.isSelected() ? 1 : 0);
        contentValues.put(KEY_UPDATED_AT, item.getUpdatedAt());
        contentValues.put(KEY_OBSERVATION_TIME, item.getObservation_time());
        contentValues.put(KEY_TEMP_C, item.getTemp_C());
        contentValues.put(KEY_TEMP_F, item.getTemp_F());
        contentValues.put(KEY_FEELS_LIKE_C, item.getFeelsLikeC());
        contentValues.put(KEY_FEELS_LIKE_F, item.getFeelsLikeF());
        contentValues.put(KEY_WEATHER_CODE, item.getWeatherCode());
        contentValues.put(KEY_WEATHER_ICON_URL, item.getWeatherIconUrl());
        contentValues.put(KEY_WEATHER_DESC, item.getWeatherDesc());
        contentValues.put(KEY_WIND_SPEED_MILES, item.getWindspeedMiles());
        contentValues.put(KEY_WIND_SPEED_KMPH, item.getWindspeedKmph());
        contentValues.put(KEY_WIND_DIR_DEGREE, item.getWinddirDegree());
        contentValues.put(KEY_WIND_DIR_16_POINT, item.getWinddir16Point());
        contentValues.put(KEY_PRECIP_MM, item.getPrecipMM());
        contentValues.put(KEY_PRECIP_INCHES, item.getPrecipInches());
        contentValues.put(KEY_HUMIDITY, item.getHumidity());
        contentValues.put(KEY_VISIBILITY, item.getVisibility());
        contentValues.put(KEY_VISIBILITY_MILES, item.getVisibilityMiles());
        contentValues.put(KEY_PRESSURE, item.getPressure());
        contentValues.put(KEY_PRESSURE_INCHES, item.getPressureInches());
        contentValues.put(KEY_CLOUD_COVER, item.getCloudcover());

        return contentValues;
    }
}
