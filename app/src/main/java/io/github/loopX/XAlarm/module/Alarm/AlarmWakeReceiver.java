package io.github.loopX.XAlarm.module.Alarm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * This class is a special BroadcastReceiver that receives the PendingIntent from AlarmManager
 * while holding the wakelock.  It forwards the intent to the AlarmRingingService to dispatch the
 * alarm. The service calls AlarmWakeReceiver.completeWakefulIntent() once it has acquired the
 * wakelock.
 */
public class AlarmWakeReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(AlarmRingingService.ACTION_DISPATCH_ALARM);
        serviceIntent.setClass(context, AlarmRingingService.class);
        serviceIntent.putExtras(intent);
        startWakefulService(context, serviceIntent);

    }

}
