package com.meghrajswami.android.weatherapp.component.screen;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.meghrajswami.android.weatherapp.R;
import com.meghrajswami.android.weatherapp.component.adapter.MyListCursorAdapter;
import com.meghrajswami.android.weatherapp.component.custom.DividerItemDecoration;
import com.meghrajswami.android.weatherapp.model.WeatherResponse.CityWeather;
import com.meghrajswami.android.weatherapp.repo.CityWeatherLocalDS;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class WeatherListFragment extends Fragment {

    //ids for menu items
    private static final int MENU_SELECT_CITIES = Menu.FIRST;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    RecyclerView recyclerView;
    private OnListFragmentInteractionListener mListener;
    //    private WeatherListRecyclerViewAdapter adapter;
    private MyListCursorAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WeatherListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static WeatherListFragment newInstance(int columnCount) {
        WeatherListFragment fragment = new WeatherListFragment();
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
        View view = inflater.inflate(R.layout.fragment_weather_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.addItemDecoration(
                        new DividerItemDecoration(getActivity(),
                                DividerItemDecoration.VERTICAL_LIST));
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
//            adapter = new WeatherListRecyclerViewAdapter(selectedCities, mListener);
            CityWeatherLocalDS cityWeatherLocalDS = new CityWeatherLocalDS(getActivity());
            adapter = new MyListCursorAdapter(getActivity(), cityWeatherLocalDS.getAllSelectedItemsCursor());

            recyclerView.setAdapter(adapter);
//            new AllSelectedCityWeatherAsyncTask()
//                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_select_cities:
                mListener.onMenuSelectCitySelected();
                break;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(CityWeather item);

        void onMenuSelectCitySelected();
    }

    private class AllSelectedCityWeatherAsyncTask extends
            AsyncTask<Void, Void, List<CityWeather>> {

        @Override
        protected List<CityWeather> doInBackground(Void... voids) {
            CityWeatherLocalDS cityWeatherLocalDS = new CityWeatherLocalDS(getActivity());
            return cityWeatherLocalDS.getAllSelectedCityWeather();
        }

        protected void onPostExecute(List<CityWeather> list) {
            if (isAdded()) {
//                wait = false;
//                centreWait(false);
                if (list != null && !list.isEmpty()) {
//                    adapter.clear();
//                    adapter.addItems(list);
                }
            }
        }
    }
}
