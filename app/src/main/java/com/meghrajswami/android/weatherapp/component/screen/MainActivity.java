package com.meghrajswami.android.weatherapp.component.screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.meghrajswami.android.weatherapp.Config;
import com.meghrajswami.android.weatherapp.R;
import com.meghrajswami.android.weatherapp.component.custom.WaitingDialog;
import com.meghrajswami.android.weatherapp.component.service.WeatherService;
import com.meghrajswami.android.weatherapp.model.WeatherResponse.CityWeather;
import com.meghrajswami.android.weatherapp.repo.CityWeatherLocalDS;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements WeatherListFragment.OnListFragmentInteractionListener,
        SelectCityFragment.OnListFragmentInteractionListener {
    private final String TAG = "MainActivity";
    private final String STATE_CHANGED = "state_changed";

    static boolean active = false;
    private boolean backFromSelectCity = false;
    private boolean initializationCompleted = false;
    public WaitingDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //tasks which should not execute upon screen orientation change etc
        if (savedInstanceState == null) {
            //initialization happens in background, so it won't affect others
            new InitializeLocalDbAyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            //update underlying weather data
            syncCityWeatherData();
            handler.post(checkInitializationTask);
        } else {
            new IsAnyCitySelectedAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }


    private void changeScreens(boolean isAnyCitySelected) {
        if (isAnyCitySelected)
            openFragment(WeatherListFragment.newInstance(1), Config.ANIMATION_BOTTOM_TO_UP);
        else
            openFragment(SelectCityFragment.newInstance(1), Config.ANIMATION_BOTTOM_TO_UP);
    }

    private void syncCityWeatherData() {
        SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME, Context.MODE_PRIVATE);
        long lastSyncTimestamp = settings.getLong(Config.PREFS_KEY_LAST_SYNC_TIMESTAMP, 0);
        Log.d(TAG, "time diff " + (System.currentTimeMillis() - lastSyncTimestamp) / 1000);
        if ((System.currentTimeMillis() - lastSyncTimestamp) > Config.CAN_SYNC_AFTER_MILLIS) {
            Intent serviceIntent = new Intent(this, WeatherService.class);
            startService(serviceIntent);
        }
    }

    //method implemented from WeatherListFragment.OnListFragmentInteractionListener
    @Override
    public void onListFragmentInteraction(CityWeather item) {

    }

    @Override
    public void onMenuSelectCitySelected() {
        openFragment(SelectCityFragment.newInstance(1), Config.ANIMATION_UP_TO_BOTTOM);
    }

    //method implemented from SelectCityFragment.OnListFragmentInteractionListener
    @Override
    public void onCityToggleSelection(CityWeather item) {
        //item contains old value for "selected" key, socheck if previously "false"
        if (!item.isSelected()) {
            Intent intent = new Intent(this, WeatherService.class);
            intent.putExtra(WeatherService.ARG_CITY, item.getCity());
            startService(intent);
        }
        new ToggleCityAsyncTask()
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, item.getCity());
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    private Handler handler = new Handler();
    private final Runnable checkInitializationTask = new Runnable() {
        public void run() {
            try {
                // TODO : isRefreshing should be attached to your data request status
                if (!initializationCompleted) {
                    // re run the verification after 1 second
                    handler.postDelayed(this, 1000);
                } else {
                    // stop the animation after the initialization is done
                    if (waitingDialog != null)
                        waitingDialog.dismiss();
                    //no need to assign it to a variable, to cancel it while activity is finished,
                    //because it will respond very fast.
                    new IsAnyCitySelectedAsyncTask()
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        active = false;
        if (waitingDialog != null) {
            waitingDialog.dismiss();
//			waitingDialog = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_CHANGED, true);
    }

    protected void waitDialog(String message) {
        if (waitingDialog == null) {
            waitingDialog = new WaitingDialog(this, message);
        } else {
            waitingDialog.dismiss();
            waitingDialog.setMessage(message);
        }
        waitingDialog.show();
    }

    @Override
    public void onBackPressed() {
        Fragment currentfragment
                = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (currentfragment instanceof SelectCityFragment) {
            backFromSelectCity = true;
            new IsAnyCitySelectedAsyncTask()
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
//        return super.onOptionsItemSelected(item);
    }

    private class IsAnyCitySelectedAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            waitDialog("Please wait ...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            CityWeatherLocalDS cityWeatherLocalDS = new CityWeatherLocalDS(MainActivity.this);
            return cityWeatherLocalDS.isAnyCitySelected();
        }

        protected void onPostExecute(Boolean result) {
            if (active) {
                if (waitingDialog != null) {
                    waitingDialog.dismiss();
                }
                //when back from select city fragment, show weather list fragment only if
                //there is any city selected to show weather for, else, finish activity to exit.
                if (result == null)
                    return;
                if (backFromSelectCity) {
                    if (result) {
                        openFragment(WeatherListFragment.newInstance(1),
                                Config.ANIMATION_LEFT_TO_RIGHT);
                    } else finish();
                } else changeScreens(result);
            }
        }

    }

    protected void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, fragment).commit();
    }

    protected void openFragment(Fragment fragment, int animation) {

        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            switch (animation) {
                case Config.ANIMATION_RIGHT_TO_LEFT:
                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;

                case Config.ANIMATION_LEFT_TO_RIGHT:
                    ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;

                case Config.ANIMATION_BOTTOM_TO_UP:
                    ft.setCustomAnimations(R.anim.push_up_in, R.anim.push_up_out);
                    break;

                case Config.ANIMATION_UP_TO_BOTTOM:
                    ft.setCustomAnimations(R.anim.push_down_in, R.anim.push_down_out);
                    break;
            }

            ft.replace(R.id.frame_container, fragment).commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ToggleCityAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            CityWeatherLocalDS cityWeatherLocalDS = new CityWeatherLocalDS(MainActivity.this);
            //add/update city in local db;
            // use single parameter only i.e. only params[0] will contain value
            return cityWeatherLocalDS.toggleCitySelected(params[0]);
        }
    }

    private class InitializeLocalDbAyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            waitDialog("Please wait ...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            CityWeatherLocalDS cityWeatherLocalDS = new CityWeatherLocalDS(MainActivity.this);
            return cityWeatherLocalDS.initializeDb(
                    Arrays.asList(getResources().getStringArray(R.array.cities)));
        }

        @Override
        protected void onPostExecute(Boolean result) {
            initializationCompleted = true;
            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
        }
    }
}
