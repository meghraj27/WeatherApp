package com.meghrajswami.android.weatherapp.component.screen;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meghrajswami.android.weatherapp.R;
import com.meghrajswami.android.weatherapp.component.adapter.SelectCityRecyclerViewAdapter;
import com.meghrajswami.android.weatherapp.component.custom.DividerItemDecoration;
import com.meghrajswami.android.weatherapp.model.WeatherResponse.CityWeather;
import com.meghrajswami.android.weatherapp.repo.CityWeatherLocalDS;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SelectCityFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private SelectCityRecyclerViewAdapter adapter;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SelectCityFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SelectCityFragment newInstance(int columnCount) {
        SelectCityFragment fragment = new SelectCityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.addItemDecoration(
                        new DividerItemDecoration(getActivity(),
                                DividerItemDecoration.VERTICAL_LIST));
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(
                        new GridLayoutManager(context, mColumnCount));
            }
            adapter = new SelectCityRecyclerViewAdapter(new ArrayList<CityWeather>(), mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new SelectCityAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCityToggleSelection(CityWeather item);
    }

    private class SelectCityAsyncTask extends
            AsyncTask<Void, Void, List<CityWeather>> {

        @Override
        protected List<CityWeather> doInBackground(Void... voids) {
            CityWeatherLocalDS cityWeatherLocalDS = new CityWeatherLocalDS(getActivity());
            return cityWeatherLocalDS.getAllCityWeather();
        }

        protected void onPostExecute(List<CityWeather> list) {
            if (isAdded()) {
                if (list != null && !list.isEmpty()) {
                    adapter.clear();
                    adapter.addItems(list);
                }
            }
        }
    }


}
