package io.github.loopX.XAlarm.module.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Calendar;

import io.github.loopX.XAlarm.R;
import io.github.loopX.XAlarm.module.AlarmModule.AlarmAlertFullScreen;

public class AlarmScheduler {

    // Key used in intent for alarm ID
    public static final String X_ALARM_ID = "x_alarm_id";

    public static long scheduleAlarm(Context context, Alarm alarm) {

        PendingIntent pendingIntent = createPendingIntent(context, alarm);

        Calendar calenderNow = Calendar.getInstance();
        long time = getAlarmTime(calenderNow, alarm);

        setAlarm(context, time, pendingIntent);

        return time;
    }

    /**
     * Get alarm time
     * @param calendarFrom from this calendarFrom to calculate alarm
     * @param alarm alarm to get time
     * @return alarm time
     */
    private static long getAlarmTime(Calendar calendarFrom, Alarm alarm) {

        Calendar calendarAlarm = Calendar.getInstance();

        // Get alarm time
        calendarAlarm.set(Calendar.HOUR_OF_DAY, alarm.getTimeHour());
        calendarAlarm.set(Calendar.MINUTE, alarm.getTimeMinute());
        calendarAlarm.set(Calendar.SECOND, 0);
        calendarAlarm.set(Calendar.MILLISECOND, 0);

        // Get current time
        final int nowDay = calendarFrom.get(Calendar.DAY_OF_WEEK);
        final int nowHour = calendarFrom.get(Calendar.HOUR_OF_DAY);
        final int nowMinute = calendarFrom.get(Calendar.MINUTE);

        // true -> at least one day is later today or later in the week
        boolean thisWeek = false;

        // If it's later today or later in the week
        for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
            if (alarm.getRepeatingDay(dayOfWeek - 1) && dayOfWeek >= nowDay &&
                    !(dayOfWeek == nowDay && alarm.getTimeHour() < nowHour) &&
                    !(dayOfWeek == nowDay && alarm.getTimeHour() == nowHour &&
                            alarm.getTimeMinute() <= nowMinute)) {

                // Set the first later + repeating day as alarm day
                if (dayOfWeek > nowDay) {
                    calendarAlarm.add(Calendar.DATE, dayOfWeek - nowDay);
                }

                thisWeek = true;
                break;
            }
        }

        // For example, today is Friday, but repeating day is Tuesday
        if (!thisWeek) {
            for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
                if (alarm.getRepeatingDay(dayOfWeek - 1) && dayOfWeek <= nowDay) {
                    calendarAlarm.add(Calendar.DATE, (7 - nowDay) + dayOfWeek);
                    break;
                }
            }
        }

        return calendarAlarm.getTimeInMillis();
    }

    /**
     * Set Alarm via AlarmManager
     *
     * Beginning with API 19 (KITKAT) alarm delivery is inexact: the OS will shift
     * alarms in order to minimize wakeups and battery use. There are new APIs to
     * support applications which need strict delivery guarantees
     *
     * @param context
     * @param time
     * @param pendingIntent
     */
    private static void setAlarm(Context context, long time, PendingIntent pendingIntent) {
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
    }

    /**
     * Cancel Alarm via AlarmManager
     * @param context
     * @param alarm alarm instance to cancela
     */
    public static void cancelAlarm(Context context, Alarm alarm) {
        PendingIntent pIntent = createPendingIntent(context, alarm);
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    private static PendingIntent createPendingIntent(Context context, Alarm alarm) {
        Intent intent = new Intent(context, AlarmWakeReceiver.class);
        intent.putExtra(X_ALARM_ID, alarm.getId());

        return PendingIntent
                .getBroadcast(
                        context,
                        (int)Math.abs(alarm.getId().getLeastSignificantBits()),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static String formatToast(Context context, long timeInMillis) {
        long delta = timeInMillis - System.currentTimeMillis();
        long hours = delta / (1000 * 60 * 60);
        long minutes = delta / (1000 * 60) % 60;
        long days = hours / 24;
        hours = hours % 24;

        String daySeq = (days == 0) ? "" :
                (days == 1) ? context.getString(R.string.day) :
                        context.getString(R.string.days, Long.toString(days));

        String minSeq = (minutes == 0) ? "" :
                (minutes == 1) ? context.getString(R.string.minute) :
                        context.getString(R.string.minutes, Long.toString(minutes));

        String hourSeq = (hours == 0) ? "" :
                (hours == 1) ? context.getString(R.string.hour) :
                        context.getString(R.string.hours, Long.toString(hours));

        boolean dispDays = days > 0;
        boolean dispHour = hours > 0;
        boolean dispMinute = minutes > 0;

        int index = (dispDays ? 1 : 0) |
                (dispHour ? 2 : 0) |
                (dispMinute ? 4 : 0);

        String[] formats = context.getResources().getStringArray(R.array.alarm_set);
        return String.format(formats[index], daySeq, hourSeq, minSeq);
    }

}
