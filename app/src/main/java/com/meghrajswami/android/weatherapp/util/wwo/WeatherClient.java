package com.meghrajswami.android.weatherapp.util.wwo;

import android.util.Log;

import com.meghrajswami.android.weatherapp.model.WeatherResponse;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class contains WWO (World Weather Online) networking related code
 */
public class WeatherClient {

    private static final String TAG = "WeatherClient";
    private String endPoint;

    public WeatherClient(String endPoint) {
        this.endPoint = endPoint;
    }

    public WeatherResponse request(String query) {
        String url = endPoint + query;
        try {
            return new WeatherResponse(getInputStream(url));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String getInputStream(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                BufferedReader r = new BufferedReader(
                        new InputStreamReader(url.openConnection().getInputStream()));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                //don't process data returned if response code is other than 200
                //and show error msg in logs
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("raw", total.toString());
                    return total.toString();
                } else {
                    Log.e(TAG, total.toString());
                }
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
