package com.meghrajswami.android.weatherapp.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.meghrajswami.android.weatherapp.component.service.WeatherService;

/**
 * Created by megh on 1/13/2016.
 */
public class AutoStart extends BroadcastReceiver
{
    Alarm alarm = new Alarm();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            //once sync weather data now, just after boot
            Intent serviceIntent = new Intent(context, WeatherService.class);
            context.startService(serviceIntent);

            //another sync trigger by alarm, overwrite previous alarm 
            alarm.setAlarm(context);
        }
    }
}