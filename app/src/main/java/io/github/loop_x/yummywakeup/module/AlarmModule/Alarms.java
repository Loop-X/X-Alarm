package io.github.loop_x.yummywakeup.module.AlarmModule;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.config.PreferenceKeys;
import io.github.loop_x.yummywakeup.module.AlarmModule.model.Alarm;
import io.github.loop_x.yummywakeup.module.AlarmModule.model.DaysOfWeek;

/**
 * The Alarms provider supplies info about Alarm Clock settings
 * 核心类，对Clock设置提供支持信息
 */
public class Alarms {

    private static final String TAG = "ywp.Alarms";

    // This action triggers the AlarmReceiver as well as the AlarmKlaxon. It
    // is a public action used in the manifest for receiving Alarm broadcasts
    // from the alarm manager.
    public static final String ALARM_ALERT_ACTION = "com.yummywakeup.ALARM_ALERT";

    // AlarmAlertFullScreen listens for this broadcast intent, so that other applications
    // can dismiss the alarm (after ALARM_ALERT_ACTION and before ALARM_DONE_ACTION).
    public static final String ALARM_DISMISS_ACTION = "com.yummywakeup.ALARM_DISMISS";

    // This is a private action used by the AlarmKlaxon to update the UI to
    // show the alarm has been killed.
    public static final String ALARM_KILLED = "yummy_alarm_killed";

    // Extra in the ALARM_KILLED intent to indicate to the user how long the
    // alarm played before being killed.
    public static final String ALARM_KILLED_TIMEOUT = "alarm_killed_timeout";

    // This string is used when passing an Alarm object through an intent.
    public static final String ALARM_INTENT_EXTRA = "intent.extra.alarm";

    // This extra is the raw Alarm object data. It is used in the
    // AlarmManagerService to avoid a ClassNotFoundException when filling in
    // the Intent extras.
    public static final String ALARM_RAW_DATA = "intent.extra.alarm_raw";

    // This string is used to identify the alarm id passed to SetAlarm from the
    // list of alarms.
    public static final String ALARM_ID = "alarm_id";

    private final static String DM12 = "E h:mm aa";
    private final static String DM24 = "E k:mm";

    private final static String M12 = "h:mm aa";
    // Shared with DigitalClock
    public final static String M24 = "kk:mm";

    /**
     * Creates a new Alarm and fills in the given alarm's id.
     * @param context
     * @param alarm
     * @return alarm ID
     */
    public static int addAlarm(Context context, Alarm alarm) {
        Log.d(TAG, "-----------> addAlarm");

        ContentValues values = createContentValues(alarm);
        Uri uri = context.getContentResolver().insert(
                Alarm.Columns.CONTENT_URI, values);
        alarm.id = (int) ContentUris.parseId(uri);

        Log.d(TAG, "addAlarm - Get alarm id " + alarm.id + " from ContentUris.parseId(uri)");

        long timeInMillis = calculateAlarm(alarm);
        if (alarm.enabled) {
            clearNextAlarmIfNeeded(context, timeInMillis);
        }
        setNextAlert(context);

        Log.d(TAG, "<----------- addAlarm");

        return alarm.id;
    }

    /**
     * Removes an existing Alarm by its id.  If this alarm is snoozing, disables
     * snooze.  Sets next alert.
     */
    public static void deleteAlarm(Context context, int alarmId) {
        if (alarmId == -1) return;

        ContentResolver contentResolver = context.getContentResolver();
        /* If alarm is snoozing, lose it */
        disableSnoozeAlert(context, alarmId);

        Uri uri = ContentUris.withAppendedId(Alarm.Columns.CONTENT_URI, alarmId);
        contentResolver.delete(uri, "", null);

        setNextAlert(context);
    }

    /**
     * Removes an existing Alarm.  If this alarm is snoozing, disables
     * snooze.  Sets next alert.
     */
    public static void deleteAlarm(Context context, Alarm alarm) {
        deleteAlarm(context, alarm.id);
    }

    /**
     * Queries all alarms
     * @return cursor over all alarms
     */
    public static Cursor getAlarmsCursor(ContentResolver contentResolver) {
        return contentResolver.query(
                Alarm.Columns.CONTENT_URI,
                Alarm.Columns.ALARM_QUERY_COLUMNS,
                null, null, Alarm.Columns.DEFAULT_SORT_ORDER);
    }

    /**
     * Gets all enabled alarms from the database.
     * @param contentResolver
     * @return
     */
    private static Cursor getFilteredAlarmsCursor(
            ContentResolver contentResolver) {
        return contentResolver.query(
                Alarm.Columns.CONTENT_URI,
                Alarm.Columns.ALARM_QUERY_COLUMNS,
                Alarm.Columns.WHERE_ENABLED,
                null, null);
    }

    private static ContentValues createContentValues(Alarm alarm) {
        ContentValues values = new ContentValues(9);
        // Set the alarm_time value if this alarm does not repeat. This will be
        // used later to disable expire alarms.
        long time = 0;
        if (!alarm.daysOfWeek.isRepeatSet()) {
            time = calculateAlarm(alarm);
        }

        values.put(Alarm.Columns.ENABLED, alarm.enabled ? 1 : 0);
        values.put(Alarm.Columns.HOUR, alarm.hour);
        values.put(Alarm.Columns.MINUTES, alarm.minutes);
        values.put(Alarm.Columns.ALARM_TIME, alarm.time);
        values.put(Alarm.Columns.DAYS_OF_WEEK, alarm.daysOfWeek.getCoded());
        values.put(Alarm.Columns.VIBRATE, alarm.vibrate);
        values.put(Alarm.Columns.MESSAGE, alarm.label);
        values.put(Alarm.Columns.UNLOCK_TYPE, alarm.unlockType);
        values.put(Alarm.Columns.ALERT, alarm.alert.toString());

        return values;
    }

    /**
     * If this alarm fires before the next snooze, clear the snooze to
     * enable this alarm.
     * @param context
     * @param alarmTime
     */
    private static void clearNextAlarmIfNeeded(Context context, long alarmTime) {
        SharedPreferences prefs =
                context.getSharedPreferences(PreferenceKeys.SHARE_PREF_NAME, 0);
        long snoozeTime = prefs.getLong(PreferenceKeys.KEY_ALARM_TIME, 0);
        if (alarmTime < snoozeTime) {
            clearAlarmPreference(context, prefs);
        }
    }

    /**
     * Return an Alarm object stored in the database with the given alarm id.
     * @param contentResolver
     * @param alarmId
     * @return Alarm object. Null if no alarm exists
     */
    public static Alarm getAlarm(ContentResolver contentResolver, int alarmId) {
        Cursor cursor = contentResolver.query(
                ContentUris.withAppendedId(Alarm.Columns.CONTENT_URI, alarmId),
                Alarm.Columns.ALARM_QUERY_COLUMNS,
                null, null, null);
        Alarm alarm = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                alarm = new Alarm(cursor);
            }
            cursor.close();
        }
        return alarm;
    }

    /**
     * Returns a list of all Alarms
     * @param contentResolver
     * @return list of Alarms
     */
    public static List<Alarm> getAllAlarms(ContentResolver contentResolver){

        List<Alarm> alarms = new ArrayList<Alarm>();

        Cursor cursor = contentResolver.query(
                Alarm.Columns.CONTENT_URI,
                Alarm.Columns.ALARM_QUERY_COLUMNS,
                null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                Alarm alarm = new Alarm(cursor);
                alarms.add(alarm);
                // A time of 0 means this alarm repeats. If the time is
                // non-zero, check if the time is before now.
            } while (cursor.moveToNext());
        }
        cursor.close();
        
        return alarms;
    }


    /**
     * A convenience method to set an alarm in the Alarms
     * content provider.
     * @return Time when the alarm will fire.
     */
    public static long setAlarm(Context context, Alarm alarm) {
        Log.d(TAG, "-----------> setAlarm");
        Log.d(TAG, "setAlarm - Alarm id passed in setAlarm() " + alarm.id);

        ContentValues values = createContentValues(alarm);
        ContentResolver resolver = context.getContentResolver();
        resolver.update(
                ContentUris.withAppendedId(Alarm.Columns.CONTENT_URI, alarm.id),
                values, null, null);

        long timeInMillis = calculateAlarm(alarm);

        if (alarm.enabled) {
            // Disable the snooze if we just changed the snoozed alarm. This
            // only does work if the snoozed alarm is the same as the given
            // alarm.
            disableSnoozeAlert(context, alarm.id);

            // Disable the snooze if this alarm fires before the snoozed alarm.
            // This works on every alarm since the user most likely intends to
            // have the modified alarm fire next.
            clearNextAlarmIfNeeded(context, timeInMillis);
        }

        setNextAlert(context);

        Log.d(TAG, "<----------- setAlarm");

        return timeInMillis;
    }

    /**
     * A convenience method to enable or disable an alarm.
     *
     * @param id             corresponds to the _id column
     * @param enabled        corresponds to the ENABLED column
     */
    public static void enableAlarm(
            final Context context, final int id, boolean enabled) {
        enableAlarmInternal(context, id, enabled);
        setNextAlert(context);
    }

    private static void enableAlarmInternal(final Context context,
            final int id, boolean enabled) {
        enableAlarmInternal(context, getAlarm(context.getContentResolver(), id),
                enabled);
    }

    /**
     *
     * @param context
     * @param alarm
     * @param enabled
     */
    private static void enableAlarmInternal(final Context context,
            final Alarm alarm, boolean enabled) {
        if (alarm == null) {
            return;
        }
        ContentResolver resolver = context.getContentResolver();

        ContentValues values = new ContentValues(2);
        values.put(Alarm.Columns.ENABLED, enabled ? 1 : 0);

        // If we are enabling the alarm, calculate alarm time since the time
        // value in Alarm may be old.
        if (enabled) {
            long time = 0;
            if (!alarm.daysOfWeek.isRepeatSet()) {
                time = calculateAlarm(alarm);
            }
            values.put(Alarm.Columns.ALARM_TIME, time);
        } else {
            // Clear the snooze if the id matches.
            disableSnoozeAlert(context, alarm.id);
        }

        resolver.update(ContentUris.withAppendedId(
                Alarm.Columns.CONTENT_URI, alarm.id), values, null, null);
    }

    /**
     *
     * @param context
     * @return
     */
    public static Alarm calculateNextAlert(final Context context) {
        Alarm alarm = null;
        long minTime = Long.MAX_VALUE;
        long currentTime = System.currentTimeMillis();
        Cursor cursor = getFilteredAlarmsCursor(context.getContentResolver());
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Alarm a = new Alarm(cursor);
                    // A time of 0 indicates this is a repeating alarm, so
                    // calculate the time to get the next alert.
                    if (a.time == 0) {
                        a.time = calculateAlarm(a);
                    } else if (a.time < currentTime) {
                        Log.v("yummywakeup", "Disabling expired alarm set for ");
                        // Expired alarm, disable it and move along.
                        enableAlarmInternal(context, a, false);
                        continue;
                    }
                    if (a.time < minTime) {
                        minTime = a.time;
                        alarm = a;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return alarm;
    }

    /**
     * Disables non-repeating alarms that have passed.  Called at
     * boot.
     */
    public static void disableExpiredAlarms(final Context context) {
        Cursor cur = getFilteredAlarmsCursor(context.getContentResolver());
        long now = System.currentTimeMillis();

        if (cur.moveToFirst()) {
            do {
                Alarm alarm = new Alarm(cur);
                // A time of 0 means this alarm repeats. If the time is
                // non-zero, check if the time is before now.
                if (alarm.time != 0 && alarm.time < now) {
                    enableAlarmInternal(context, alarm, false);
                }
            } while (cur.moveToNext());
        }
        cur.close();
    }

    /**
     * Called at system startup, on time/timezone change, and whenever
     * the user changes alarm settings. Activates alarm if set,
     * otherwise loads all alarms, activates next alert.
     */
    public static void setNextAlert(final Context context) {
        if (!enableAlarmAlert(context)) {
            Alarm alarm = calculateNextAlert(context);
            if (alarm != null) {
                enableAlert(context, alarm, alarm.time);
            } else {
                disableAlert(context);
            }
        }
    }

    /**
     * Sets alert in AlarmManger and StatusBar.  This is what will
     * actually launch the alert when the alarm triggers.
     *
     * @param alarm Alarm.
     * @param atTimeInMillis milliseconds since epoch
     */
    private static void enableAlert(Context context, final Alarm alarm,
            final long atTimeInMillis) {
        AlarmManager am = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (true) {
            Log.v("yummywakeup", "** setAlert id " + alarm.id + " atTime " + atTimeInMillis);
        }

        Intent intent = new Intent(ALARM_ALERT_ACTION);

        // XXX: This is a slight hack to avoid an exception in the remote
        // AlarmManagerService process. The AlarmManager adds extra data to
        // this Intent which causes it to inflate. Since the remote process
        // does not know about the Alarm class, it throws a
        // ClassNotFoundException.
        //
        // To avoid this, we marshall the data ourselves and then parcel a plain
        // byte[] array. The AlarmReceiver class knows to build the Alarm
        // object from the byte[] array.
        Parcel out = Parcel.obtain();
        alarm.writeToParcel(out, 0);
        out.setDataPosition(0);
        intent.putExtra(ALARM_RAW_DATA, out.marshall());

        PendingIntent sender = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        am.set(AlarmManager.RTC_WAKEUP, atTimeInMillis, sender);

        setStatusBarIcon(context, true);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(atTimeInMillis);
        String timeString = formatDayAndTime(context, c);
        saveNextAlarm(context, timeString);
    }

    /**
     * Disables alert in AlarmManger and StatusBar.
     *
     * 
     */
    static void disableAlert(Context context) {
        AlarmManager am = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(
                context, 0, new Intent(ALARM_ALERT_ACTION),
                PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(sender);
        setStatusBarIcon(context, false);
        saveNextAlarm(context, "");
    }

    /**
     *
     * @param context
     * @param id
     * @param time
     */
    public static void saveSnoozeAlert(final Context context, final int id,
                                       final long time) {

        Log.d(TAG, "-----------> saveSnoozeAlert");

        SharedPreferences prefs = context.getSharedPreferences(
                PreferenceKeys.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        if (id == -1) {
            Log.d(TAG, "saveSnoozeAlert - id is -1, goto clearAlarmPreference");

            clearAlarmPreference(context, prefs);
        } else {
            SharedPreferences.Editor ed = prefs.edit();
            ed.putInt(PreferenceKeys.KEY_ALARM_ID, id);
            ed.putLong(PreferenceKeys.KEY_ALARM_TIME, time);
            ed.apply();

            Log.d(TAG, "saveSnoozeAlert - save alarm id " + id);
            Log.d(TAG, "saveSnoozeAlert - save alarm time " + time);
        }
        // Set the next alert after updating the snooze.
        setNextAlert(context);

        Log.d(TAG, "<----------- setAlarm");
    }

    /**
     * Disable the snooze alert if the given id matches the snooze id.
     */
    public static void disableSnoozeAlert(final Context context, final int id) {
        SharedPreferences prefs = context.getSharedPreferences(
                PreferenceKeys.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        int snoozeId = prefs.getInt(PreferenceKeys.KEY_ALARM_ID, -1);
        if (snoozeId == -1) {
            // No snooze set, do nothing.
            return;
        } else if (snoozeId == id) {
            // This is the same id so clear the shared prefs.
            clearAlarmPreference(context, prefs);
        }
    }

    /**
     * Helper to remove the alarm preference. Do not use clear because that
     * will erase the clock preferences. Also clear the alarm notification in
     * the window shade.
     * @param context
     * @param prefs
     */
    private static void clearAlarmPreference(final Context context,
                                             final SharedPreferences prefs) {
        final int alarmId = prefs.getInt(PreferenceKeys.KEY_ALARM_ID, -1);
        if (alarmId != -1) {
            NotificationManager nm = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(alarmId);
        }

        final SharedPreferences.Editor ed = prefs.edit();
        ed.remove(PreferenceKeys.KEY_ALARM_ID);
        ed.remove(PreferenceKeys.KEY_ALARM_TIME);
        ed.apply();
    };

    /**
     * If there is a snooze set, enable it in AlarmManager
     * @return true if snooze is set
     */
    private static boolean enableAlarmAlert(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                PreferenceKeys.SHARE_PREF_NAME, 0);

        int id = prefs.getInt(PreferenceKeys.KEY_ALARM_ID, -1);
        if (id == -1) {
            return false;
        }
        long time = prefs.getLong(PreferenceKeys.KEY_ALARM_TIME, -1);

        // Get the alarm from the db.
        final Alarm alarm = getAlarm(context.getContentResolver(), id);
        if (alarm == null) {
            return false;
        }
        // The time in the database is either 0 (repeating) or a specific time
        // for a non-repeating alarm. Update this value so the AlarmReceiver
        // has the right time to compare.
        alarm.time = time;

        enableAlert(context, alarm, time);
        return true;
    }

    /**
     * Tells the StatusBar whether the alarm is enabled or disabled
     */
    private static void setStatusBarIcon(Context context, boolean enabled) {
        Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
        alarmChanged.putExtra("alarmSet", enabled);
        context.sendBroadcast(alarmChanged);
    }

    private static long calculateAlarm(Alarm alarm) {
        return calculateAlarm(alarm.hour, alarm.minutes, alarm.daysOfWeek)
                .getTimeInMillis();
    }

    /**
     * Given an alarm in hours and minutes, return a time suitable for
     * setting in AlarmManager.
     */
    static Calendar calculateAlarm(int hour, int minute, DaysOfWeek daysOfWeek) {

        // start with now
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);

        // if alarm is behind current time, advance one day
        if (hour < nowHour  ||
            hour == nowHour && minute <= nowMinute) {
            c.add(Calendar.DAY_OF_YEAR, 1);
        }
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        int addDays = daysOfWeek.getNextAlarm(c);
        if (addDays > 0) c.add(Calendar.DAY_OF_WEEK, addDays);
        return c;
    }

    static String formatTime(final Context context, int hour, int minute, DaysOfWeek daysOfWeek) {
        Calendar c = calculateAlarm(hour, minute, daysOfWeek);
        return formatTime(context, c);
    }

    /* used by AlarmAlert */
    public static String formatTime(final Context context, Calendar c) {
        String format = get24HourMode(context) ? M24 : M12;
        return (c == null) ? "" : (String) DateFormat.format(format, c);
    }

    /**
     * Shows day and time -- used for lock screen
     */
    private static String formatDayAndTime(final Context context, Calendar c) {
        String format = get24HourMode(context) ? DM24 : DM12;
        return (c == null) ? "" : (String) DateFormat.format(format, c);
    }

    /**
     * Save time of the next alarm, as a formatted string, into the system
     * settings so those who care can make use of it.
     */
    static void saveNextAlarm(final Context context, String timeString) {
        Settings.System.putString(context.getContentResolver(),
                                  Settings.System.NEXT_ALARM_FORMATTED,
                                  timeString);
    }

    /**
     * @return true if clock is set to 24-hour mode
     */
    public static boolean get24HourMode(final Context context) {
        return DateFormat.is24HourFormat(context);
    }

    /**
     * format "Alarm set for 2 days 7 hours and 53 minutes from
     * now"
     */
    // ToDo should be in ToastMaster. And also, many functions need to be in utils
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
