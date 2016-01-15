package com.meghrajswami.android.weatherapp.component.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meghrajswami.android.weatherapp.R;
import com.meghrajswami.android.weatherapp.model.WeatherResponse.CityWeather;
import com.meghrajswami.android.weatherapp.repo.WeatherSQLiteHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Cursor recyclerview adapter for weather list item
 */
public class WeatherListCursorAdapter extends CursorRecyclerViewAdapter<WeatherListCursorAdapter.ViewHolder> {

    private Context context;

    public WeatherListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_weather_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.item = WeatherSQLiteHelper.fetchItemFromCursor(cursor);

        holder.textViewCity.setText(String.valueOf(holder.item.getCity()));
//        holder.textViewSyncTime.setText(context.getResources().getString(R.string.sync_time,
//                new Date(holder.item.getUpdatedAt()).getTime()));

        holder.textViewObservationTime.setText(context.getResources().getString(
                R.string.string_observation_time, holder.utcToLocal(holder.item.getObservation_time())));
        holder.textViewTemp.setText(context.getResources().getString(
                R.string.string_temp_c, holder.item.getTemp_C()));
        holder.textViewFeelsLike.setText(context.getResources().getString(
                R.string.string_feelslike_c, holder.item.getFeelsLikeC()));
        holder.textViewWeatherDesc.setText(context.getResources().getString(
                R.string.string_weather_desc, holder.item.getWeatherDesc()));
        holder.textViewWindspeed.setText(context.getResources().getString(
                R.string.string_windspeed, holder.item.getWindspeedKmph()));
        holder.textViewWinddir.setText(context.getResources().getString(
                R.string.string_winddir, holder.item.getWinddirDegree()));
        holder.textViewPrecip.setText(context.getResources().getString(
                R.string.string_precip, holder.item.getPrecipMM()));
        holder.textViewHumidity.setText(context.getResources().getString(
                R.string.string_humidity, holder.item.getHumidity()));
        holder.textViewVisibility.setText(context.getResources().getString(
                R.string.string_visibility, holder.item.getVisibility()));
        holder.textViewPressure.setText(context.getResources().getString(
                R.string.string_pressure, holder.item.getPressure()));
        holder.textViewCloudCover.setText(context.getResources().getString(
                R.string.string_cloud_cover, holder.item.getCloudcover()));

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewCity;
        //        public final TextView textViewSyncTime;
        public final TextView textViewObservationTime;
        public final TextView textViewTemp;
        public final TextView textViewFeelsLike;
        public final TextView textViewWeatherDesc;
        public final TextView textViewWindspeed;
        public final TextView textViewWinddir;
        public final TextView textViewPrecip;
        public final TextView textViewHumidity;
        public final TextView textViewVisibility;
        public final TextView textViewPressure;
        public final TextView textViewCloudCover;
        public CityWeather item;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewCity = (TextView) view.findViewById(R.id.textViewCity);
//            textViewSyncTime = (TextView) view.findViewById(R.id.textViewSyncTime);
            textViewObservationTime = (TextView) view.findViewById(R.id.textViewObservationTime);
            textViewTemp = (TextView) view.findViewById(R.id.textViewTemp);
            textViewFeelsLike = (TextView) view.findViewById(R.id.textViewFeelsLike);
            textViewWeatherDesc = (TextView) view.findViewById(R.id.textViewWeatherDesc);
            textViewWindspeed = (TextView) view.findViewById(R.id.textViewWindspeed);
            textViewWinddir = (TextView) view.findViewById(R.id.textViewWinddir);
            textViewPrecip = (TextView) view.findViewById(R.id.textViewPrecip);
            textViewHumidity = (TextView) view.findViewById(R.id.textViewHumidity);
            textViewVisibility = (TextView) view.findViewById(R.id.textViewVisibility);
            textViewPressure = (TextView) view.findViewById(R.id.textViewPressure);
            textViewCloudCover = (TextView) view.findViewById(R.id.textViewCloudCover);
        }

        public String utcToLocal(String utcString) {
            if (utcString == null || utcString.equals(""))
                return null;
            SimpleDateFormat sdf = new SimpleDateFormat("KK:mm aa", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date utcDate = sdf.parse(utcString);
                sdf.setTimeZone(TimeZone.getDefault());
                return sdf.format(utcDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}