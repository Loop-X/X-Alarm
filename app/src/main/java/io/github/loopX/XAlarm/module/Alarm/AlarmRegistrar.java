package io.github.loopX.XAlarm.module.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import io.github.loopX.XAlarm.database.AlarmDBService;

/**
 * This BroadcastReceiver is registered to be called for the following system intents:
 *
 * BOOT_COMPLETED, TIMEZONE_CHANGED, TIME_SET, DATE_CHANGED
 */
public class AlarmRegistrar extends BroadcastReceiver {

    private static void refreshAlarms(Context context) {
        AlarmDBService.getInstance(context).getAlarms();

        List<Alarm> alarms = AlarmDBService.getInstance(context).getAlarms();

        Alarm mAlarm = null;

        if(alarms.isEmpty()) {
            mAlarm = new Alarm();
            AlarmDBService.getInstance(context).addAlarm(mAlarm);
        } else {
            // Always force to get first alarm in list
            mAlarm = alarms.get(0);
        }

        mAlarm.cancel();
        mAlarm.schedule();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        refreshAlarms(context);
    }
}
