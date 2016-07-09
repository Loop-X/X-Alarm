package io.github.loopX.XAlarm.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import io.github.loopX.XAlarm.MainActivity;
import io.github.loopX.XAlarm.R;
import io.github.loopX.XAlarm.module.AlarmModule.AlarmAlertFullScreen;
import io.github.loopX.XAlarm.module.AlarmModule.AlarmAlertWakeLock;
import io.github.loopX.XAlarm.module.AlarmModule.Alarms;
import io.github.loopX.XAlarm.module.AlarmModule.model.Alarm;


public class AlarmReceiver extends BroadcastReceiver {

    /** If the alarm is older than STALE_WINDOW, ignore.  It
        is probably the result of a time or timezone change */
    private final static int STALE_WINDOW = 30 * 60 * 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Alarms.ALARM_KILLED.equals(intent.getAction())) {
            // The alarm has been killed, update the notification
            updateNotification(context, (Alarm)
                    intent.getParcelableExtra(Alarms.ALARM_INTENT_EXTRA),
                    intent.getIntExtra(Alarms.ALARM_KILLED_TIMEOUT, -1));
            return;
        } else if (!Alarms.ALARM_ALERT_ACTION.equals(intent.getAction())) {
            // Unknown intent, bail.
            return;
        }

        Alarm alarm = null;

        // Grab the alarm from the intent. Since the remote AlarmManagerService
        // fills in the Intent to add some extra data, it must unparcel the
        // Alarm object. It throws a ClassNotFoundException when unparcelling.
        // To avoid this, do the marshalling ourselves.
        final byte[] data = intent.getByteArrayExtra(Alarms.ALARM_RAW_DATA);
        if (data != null) {
            Parcel in = Parcel.obtain();
            in.unmarshall(data, 0, data.length);
            in.setDataPosition(0);
            alarm = Alarm.CREATOR.createFromParcel(in);
        }

        if (alarm == null) {
            Log.v("yummywakeup", "Failed to parse the alarm from the intent");
            // Make sure we set the next alert if needed.
            Alarms.setNextAlert(context);
            return;
        }
        
        // Disable the snooze alert if this alarm is the snooze.
        Alarms.disableSnoozeAlert(context, alarm.id);
        // Disable this alarm if it does not repeat.
        if (!alarm.daysOfWeek.isRepeatSet()) {
            Alarms.enableAlarm(context, alarm.id, false);
        } else {
            // Enable the next alert if there is one. The above call to
            // enableAlarm will call setNextAlert so avoid calling it twice.
            Alarms.setNextAlert(context);
        }

        // Intentionally verbose: always log the alarm time to provide useful
        // information in bug reports.
        long now = System.currentTimeMillis();

        // Always verbose to track down time change problems.
        if (now > alarm.time + STALE_WINDOW) {
            Log.v("yummywakeup", "Ignoring stale alarm");
            return;
        }

        /**
         * 唤醒屏幕
         */
        // Maintain a cpu wake lock until the AlarmAlert and AlarmKlaxon can
        // pick it up.
        AlarmAlertWakeLock.acquireCpuWakeLock(context);

        /**
         * Close all dialogs and window shade
         */
        Intent closeDialogs = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeDialogs);

        /**
         * Play the alarm alert and vibrate the device.
         */
        Intent playAlarm = new Intent(context, AlarmKlaxon.class);//Intent(Alarms.ALARM_ALERT_ACTION); will be cause crash
        playAlarm.putExtra(Alarms.ALARM_INTENT_EXTRA, alarm);
        context.startService(playAlarm);

        /**
         * Sets notification
         */
        String label = alarm.getLabelOrDefault(context);

        Intent showUnlockIntent = new Intent(context, AlarmAlertFullScreen.class);
        showUnlockIntent.putExtra(Alarms.ALARM_INTENT_EXTRA, alarm);
        showUnlockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_NO_USER_ACTION);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        alarm.id,
                        showUnlockIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Yummy Wake Up!")
                        .setContentText(label)
                        .setContentIntent(resultPendingIntent)
                        .setFullScreenIntent(resultPendingIntent, true)
                        .setOngoing(true)
                        .setDefaults(NotificationCompat.DEFAULT_LIGHTS);

        NotificationManager mNotifyMgr = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Uses the alarm id to easily identify the correct notification.
        mNotifyMgr.notify(alarm.id, mBuilder.build());
    }

    /**
     * Update Notification. When click, go to MainActivity to reset alarm
     * @param context
     * @param alarm   Alarm already been killed
     * @param timeout How long alarm already played before being killed
     */
    private void updateNotification(Context context, Alarm alarm, int timeout) {

        // If the alarm is null, just cancel the notification.
        if (alarm == null) {
            Log.v("yummywakeup", "Cannot update notification for killer callback");
            return;
        }

        // Go mainActivity when clicked.
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntentintent = PendingIntent.getActivity(
                context,
                alarm.id,
                intent,
                0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Yummy Wake Up!")
                        .setContentText(context.getString(R.string.alarm_alert_alert_silenced, timeout))
                        .setContentIntent(pendingIntentintent)
                        .setAutoCancel(true);

        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(alarm.id, mBuilder.build());
    }
}
