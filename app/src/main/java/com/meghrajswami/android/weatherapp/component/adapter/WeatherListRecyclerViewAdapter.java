package com.meghrajswami.android.weatherapp.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meghrajswami.android.weatherapp.R;
import com.meghrajswami.android.weatherapp.component.screen.WeatherListFragment.OnListFragmentInteractionListener;
import com.meghrajswami.android.weatherapp.model.WeatherResponse.CityWeather;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class WeatherListRecyclerViewAdapter
        extends RecyclerView.Adapter<WeatherListRecyclerViewAdapter.ViewHolder> {

    private List<CityWeather> items = new ArrayList<CityWeather>();
//    private final OnListFragmentInteractionListener mListener;

    public WeatherListRecyclerViewAdapter(List<CityWeather> cityWeatherList,
                                          OnListFragmentInteractionListener listener) {
        this.items.addAll(cityWeatherList);
//        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_weather_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = items.get(position);

        holder.textViewCity.setText(String.valueOf(holder.item.getCity()));
//        holder.textViewSyncTime.setText(String.valueOf(holder.item.getObservation_time()));
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

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.item);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Adds an item to the adapter and notify about change in dataset
     * @param item
     */
    public void addItem(CityWeather item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    /**
     * Adds all items to the adapter and notify about change in dataset.
     * Order of items remains same.
     * @param items
     */
    public void addItems(List<CityWeather> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

        @Override
        public String toString() {
            return super.toString() + " '" + textViewCity.getText() + "'";
        }
    }
}
