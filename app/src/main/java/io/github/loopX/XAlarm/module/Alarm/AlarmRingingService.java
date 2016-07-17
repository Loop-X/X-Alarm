package io.github.loopX.XAlarm.module.Alarm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.UUID;

public class AlarmRingingService extends Service {

    public final String TAG = this.getClass().getSimpleName();

    AlarmRingingController mController;

    public static final String ACTION_START_FOREGROUND =
            "io.github.loopX.XAlarm.AlarmRingingService.START_FOREGROUND";
    public static final String ACTION_STOP_FOREGROUND =
            "io.github.loopX.XAlarm.AlarmRingingService.STOP_FOREGROUND";
    public static final String ACTION_DISPATCH_ALARM =
            "io.github.loopX.XAlarm.AlarmRingingService.DISPATCH_ALARM";

    public static final String ALARM_ID = "x_alarm_id";
    private static final String ALARM_TIME = "x_alarm_time";

    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();

        mController = new AlarmRingingController(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            if (ACTION_DISPATCH_ALARM.equals(intent.getAction())) {
                // Get alarm instance from DB and fire it!
                mController.registerAlarm(intent);
                AlarmWakeReceiver.completeWakefulIntent(intent);
            } else if (ACTION_START_FOREGROUND.equals(intent.getAction())) {
                enableForegroundService(intent);
            } else if (ACTION_STOP_FOREGROUND.equals(intent.getAction())) {
                disableForegroundService();
            }
        }

        return START_STICKY;
    }

    /**
     * Start AlarmRingingService as foreground service to avoid alarm being killing
     * @param context
     * @param alarmId
     * @param alarmTime
     */
    public static void startForegroundService(Context context, UUID alarmId, long alarmTime) {
        Intent intent = new Intent(AlarmRingingService.ACTION_START_FOREGROUND);
        intent.setClass(context, AlarmRingingService.class);
        intent.putExtra(ALARM_ID, alarmId);
        intent.putExtra(ALARM_TIME, alarmTime);
        context.startService(intent);
    }

    /**
     * Stop AlarmRingingService from foreground
     * @param context
     */
    public static void stopForegroundService(Context context) {
        Intent serviceIntent = new Intent(AlarmRingingService.ACTION_STOP_FOREGROUND);
        serviceIntent.setClass(context, AlarmRingingService.class);
        context.startService(serviceIntent);
    }

    /**
     * Enable foreground service
     * @param intent
     */
    private void enableForegroundService(Intent intent) {
        UUID alarmId = (UUID) intent.getSerializableExtra(ALARM_ID);
        startForeground((int) alarmId.getMostSignificantBits(),
                    AlarmNotificationManager.createAlarmNotification(this, alarmId));
    }

    /**
     * Disable foreground service
     */
    private void disableForegroundService() {
        stopForeground(true);
        SharedWakeLock.getInstance(this).releasePartialWakeLock();
    }

    public class LocalBinder extends Binder {
        public AlarmRingingService getService() {
            return AlarmRingingService.this;
        }
    }


}
