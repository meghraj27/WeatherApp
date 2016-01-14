package com.meghrajswami.android.weatherapp.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.meghrajswami.android.weatherapp.R;
import com.meghrajswami.android.weatherapp.component.screen.SelectCityFragment.OnListFragmentInteractionListener;
import com.meghrajswami.android.weatherapp.model.WeatherResponse.CityWeather;

import java.util.List;

/**
 * Adapter For SelectCityRecyclerView.
 * It holds CityWeather objects(fo convenience) but we will show only city names, user should
 * feel that he is selecting/unselecting cities only, not cityweather
 */
public class SelectCityRecyclerViewAdapter extends RecyclerView.Adapter<SelectCityRecyclerViewAdapter.ViewHolder> {

    private final List<CityWeather> items;
    private final OnListFragmentInteractionListener mListener;

    public SelectCityRecyclerViewAdapter(List<CityWeather> items, OnListFragmentInteractionListener listener) {
        this.items = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_select_city_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.item = items.get(position);
//        holder.mIdView.setText(String.valueOf(position));
        holder.mContentView.setText(holder.item.getCity());
        holder.mContentView.setChecked(holder.item.isSelected());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onCityToggleSelection(holder.item);
                    //just for visual, it won't update item from db
                    items.get(position).setSelected(!holder.item.isSelected());
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(CityWeather item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void addItems(List<CityWeather> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //        public final TextView mIdView;
        public final CheckBox mContentView;
        public CityWeather item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (CheckBox) view.findViewById(R.id.checkboxCitySelect);
//            mContentView.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
