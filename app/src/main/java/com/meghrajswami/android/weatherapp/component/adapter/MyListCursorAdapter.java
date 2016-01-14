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

/**
 * Created by megh on 1/11/2016.
 */
public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder> {

    public MyListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
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
        holder.textViewSyncTime.setText(String.valueOf(holder.item.getObservation_time()));
        holder.textViewObservationTime.setText(String.valueOf(holder.item.getObservation_time()));
        holder.textViewTemp.setText(String.valueOf(holder.item.getTemp_C()));
        holder.textViewFeelsLike.setText(String.valueOf(holder.item.getFeelsLikeC()));
        holder.textViewWeatherDesc.setText(String.valueOf(holder.item.getWeatherDesc()));
        holder.textViewWindspeed.setText(String.valueOf(holder.item.getWindspeedKmph()));
        holder.textViewWinddir.setText(String.valueOf(holder.item.getWinddirDegree()));
        holder.textViewPrecip.setText(String.valueOf(holder.item.getPrecipMM()));
        holder.textViewHumidity.setText(String.valueOf(holder.item.getHumidity()));
        holder.textViewVisibility.setText(String.valueOf(holder.item.getVisibility()));
        holder.textViewPressure.setText(String.valueOf(holder.item.getPressure()));
        holder.textViewCloudCover.setText(String.valueOf(holder.item.getCloudcover()));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewCity;
        public final TextView textViewSyncTime;
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
            textViewSyncTime = (TextView) view.findViewById(R.id.textViewSyncTime);
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
    }

}