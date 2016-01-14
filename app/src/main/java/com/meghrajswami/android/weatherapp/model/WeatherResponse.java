package com.meghrajswami.android.weatherapp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * WeatherResponse pojo for serialization/deserialization
 * as well as to access it's data. Singular is being used at many places
 * just to match with the api response fields, i.e. "current_condition"
 * should be "current_conditions" because it's an array, but singular word
 * is being used in the code just to match with the api response.
 */
public class WeatherResponse {

    private static final String TAG = "WeatherResponse";

    Data data;

    public WeatherResponse(String weatherResponseString) throws JSONException {
//        Log.d("WeatherRespStr", weatherResponseString);
        try {
            JSONObject o = new JSONObject(weatherResponseString);
            data = new Data(o.optJSONObject("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Data getData() {
        return data;
    }

    public class Data {
        List<Request> request;
        List<CityWeather> current_condition;
        List<Error> error;

        Data(JSONObject o) throws JSONException {
            request = new ArrayList<Request>();
            JSONArray requestJsonArray = o.optJSONArray("request");
            for (int i = 0; i < requestJsonArray.length(); i++) {
                request.add(new Request(requestJsonArray.getJSONObject(i)));
            }
            JSONArray currentConditionJsonArray = o.optJSONArray("current_condition");
            for (int j = 0; j < currentConditionJsonArray.length(); j++) {
                current_condition = new ArrayList<CityWeather>();
                current_condition.add(
                        new CityWeather(currentConditionJsonArray.getJSONObject(j)));
            }

        }

        public List<Request> getRequest() {
            return request;
        }

        public List<CityWeather> getCurrent_condition() {
            return current_condition;
        }
    }

    public class Error {
        String msg;

        Error(JSONObject o) {
            this.msg = o.optString("msg");
        }

        public String getMsg() {
            return msg;
        }
    }

    public class Request {
        String type;
        String query;

        Request(JSONObject o) {
            type = o.optString("type");
            query = o.optString("query");
        }

        public String getType() {
            return type;
        }

        public String getQuery() {
            return query;
        }
    }

    public static class CityWeather {
        String observation_time;
        int temp_C;
        int temp_F;
        int FeelsLikeC;
        int FeelsLikeF;
        int weatherCode;
        JSONArray weatherIconUrl;
        JSONArray weatherDesc;
        int windspeedMiles;
        int windspeedKmph;
        int winddirDegree;
        String winddir16Point;
        int precipMM;
        Float precipInches;
        Float humidity;
        int visibility;
        int visibilityMiles;
        int pressure;
        Float pressureInches;
        int cloudcover;

        private String city;
        private boolean selected;
        private long updatedAt;

        public CityWeather() {

        }

        public CityWeather(String city, boolean selected) {
            this.city = city;
            this.selected = selected;
        }

        CityWeather(JSONObject o) {
            observation_time = o.optString("observation_time");
            temp_C = o.optInt("temp_C");
            temp_F = o.optInt("temp_F");
            FeelsLikeC = o.optInt("FeelsLikeC");
            FeelsLikeF = o.optInt("FeelsLikeF");
            weatherCode = o.optInt("weatherCode");
            weatherIconUrl = o.optJSONArray("weatherIconUrl");
            weatherDesc = o.optJSONArray("weatherDesc");
            windspeedMiles = o.optInt("windspeedMiles");
            windspeedKmph = o.optInt("windspeedKmph");
            winddirDegree = o.optInt("winddirDegree");
            winddir16Point = o.optString("winddir16Point");
            precipMM = o.optInt("precipMM");
            precipInches = (float) o.optDouble("precipInches");
            humidity = (float) o.optDouble("humidity");
            visibility = o.optInt("visibility");
            visibilityMiles = o.optInt("visibilityMiles");
            pressure = o.optInt("pressure");
            pressureInches = (float) o.optDouble("pressureInches");
            cloudcover = o.optInt("cloudcover");
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public void setObservation_time(String observation_time) {
            this.observation_time = observation_time;
        }

        public void setTemp_C(int temp_C) {
            this.temp_C = temp_C;
        }

        public void setTemp_F(int temp_F) {
            this.temp_F = temp_F;
        }

        public void setFeelsLikeC(int feelsLikeC) {
            FeelsLikeC = feelsLikeC;
        }

        public void setFeelsLikeF(int feelsLikeF) {
            FeelsLikeF = feelsLikeF;
        }

        public void setWeatherCode(int weatherCode) {
            this.weatherCode = weatherCode;
        }

        public void setWeatherIconUrl(String weatherIconUrl) {
            if (weatherIconUrl == null || weatherIconUrl.equals(""))
                return;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("value", weatherIconUrl);
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);
                this.weatherIconUrl = jsonArray;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setWeatherIconUrl(JSONArray weatherIconUrl) {
            this.weatherIconUrl = weatherIconUrl;
        }

        public void setWeatherDesc(String weatherDesc) {
            if (weatherDesc == null || weatherDesc.equals(""))
                return;
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("value", weatherDesc);
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);
                this.weatherDesc = jsonArray;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setWeatherDesc(JSONArray weatherDesc) {
            this.weatherDesc = weatherDesc;
        }

        public void setWindspeedMiles(int windspeedMiles) {
            this.windspeedMiles = windspeedMiles;
        }

        public void setWindspeedKmph(int windspeedKmph) {
            this.windspeedKmph = windspeedKmph;
        }

        public void setWinddirDegree(int winddirDegree) {
            this.winddirDegree = winddirDegree;
        }

        public void setWinddir16Point(String winddir16Point) {
            this.winddir16Point = winddir16Point;
        }

        public void setPrecipMM(int precipMM) {
            this.precipMM = precipMM;
        }

        public void setPrecipInches(Float precipInches) {
            this.precipInches = precipInches;
        }

        public void setHumidity(Float humidity) {
            this.humidity = humidity;
        }

        public void setVisibility(int visibility) {
            this.visibility = visibility;
        }

        public void setVisibilityMiles(int visibilityMiles) {
            this.visibilityMiles = visibilityMiles;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public void setPressureInches(Float pressureInches) {
            this.pressureInches = pressureInches;
        }

        public void setCloudcover(int cloudcover) {
            this.cloudcover = cloudcover;
        }

        public String getObservation_time() {
            return observation_time;
        }

        public int getTemp_C() {
            return temp_C;
        }

        public int getTemp_F() {
            return temp_F;
        }

        public int getFeelsLikeC() {
            return FeelsLikeC;
        }

        public int getFeelsLikeF() {
            return FeelsLikeF;
        }

        public int getWeatherCode() {
            return weatherCode;
        }

        public String getWeatherIconUrl() {
            //it's a hack, because Wwo api is giving url value within an array
            String url = null;
            if (weatherIconUrl != null && weatherIconUrl.length() != 0) {
                //we already checked for empty array, and we have trust that Wwo won't
                //change schema(index 1 will contain the url), so no need to check again
                url = weatherIconUrl.optJSONObject(0).optString("value");
            }
            return url;
        }

        /**
         * Returns weather condition in human readable string
         *
         * @return weather description in string form
         */
        public String getWeatherDesc() {
            //it's a hack, because Wwo api is giving description value within an array
            String desc = null;
            if (weatherDesc != null && weatherDesc.length() != 0) {
                //we already checked for empty array, and we have trust that
                // Wwo won't change schema(index 1 will contain the description),
                // so no need to check again
                desc = weatherDesc.optJSONObject(0).optString("value");
            }
            return desc;
        }

        public int getWindspeedMiles() {
            return windspeedMiles;
        }

        public int getWindspeedKmph() {
            return windspeedKmph;
        }

        public int getWinddirDegree() {
            return winddirDegree;
        }

        public String getWinddir16Point() {
            return winddir16Point;
        }

        public int getPrecipMM() {
            return precipMM;
        }

        public Float getPrecipInches() {
            return precipInches;
        }

        public Float getHumidity() {
            return humidity;
        }

        public int getVisibility() {
            return visibility;
        }

        public int getVisibilityMiles() {
            return visibilityMiles;
        }

        public int getPressure() {
            return pressure;
        }

        public Float getPressureInches() {
            return pressureInches;
        }

        public int getCloudcover() {
            return cloudcover;
        }

        public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }
    }

}
