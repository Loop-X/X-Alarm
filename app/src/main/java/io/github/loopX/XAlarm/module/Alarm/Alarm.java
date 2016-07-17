package io.github.loopX.XAlarm.module.Alarm;

import android.content.Context;
import android.net.Uri;

import java.util.Calendar;
import java.util.UUID;

import io.github.loopX.XAlarm.XAlarmApp;
import io.github.loopX.XAlarm.database.AlarmDBService;
import io.github.loopX.XAlarm.module.UnlockTypeModule.UnlockTypeEnum;

/**
 * Representation of an Alarm within the app.
 **/
public class Alarm {

    private AlarmDBService alarmDBService;

    private UUID    id;
    private int     timeHour;
    private int     timeMinute;
    private int     unlockType;
    private boolean repeatingDays[];
    private boolean isEnabled;
    private boolean isVibrate;
    private Uri     alarmTone;

    public Alarm() {
        this(UUID.randomUUID());
    }

    public Alarm(UUID alarmId) {

        alarmDBService = AlarmDBService.getInstance(XAlarmApp.getAppContext());

        id = alarmId;

        Calendar calendar = Calendar.getInstance();

        timeHour = calendar.get(Calendar.HOUR_OF_DAY);
        timeMinute = calendar.get(Calendar.MINUTE);

        unlockType = UnlockTypeEnum.Type.getID();

        // By default, alarm repeats everyday
        repeatingDays = new boolean[]{ true, true, true, true, true, true, true };
        alarmTone = Uri.parse(XAlarmApp.getResourcePath() + "/raw/ringtone_0");
        isEnabled = true;
        isVibrate = true;

    }

    /*
    Functions
     */

    /**
     * Schedules the alarm via the AlarmScheduler
     * @return alarm time
     */
    public long schedule() {

        Context context = XAlarmApp.getAppContext();

        if(isEnabled) {
            AlarmScheduler.cancelAlarm(context, this);
        } else {
            isEnabled = true;
        }

        alarmDBService.updateAlarm(this);

        return AlarmScheduler.scheduleAlarm(context, this);
    }

    /**
     * Delete alarm from DB and AlarmManager
     */
    public void delete() {

        Context context = XAlarmApp.getAppContext();

        if (isEnabled()) {
            AlarmScheduler.cancelAlarm(context, this);
        }

        alarmDBService.deleteAlarm(this);
    }

    /**
     * Cancel
     */
    public void cancel() {

        Context context = XAlarmApp.getAppContext();

        setEnabled(false);
        AlarmScheduler.cancelAlarm(context, this);

        alarmDBService.updateAlarm(this);
    }

    public void onDismiss() {

        Context context = XAlarmApp.getAppContext();

        boolean updateAlarm = false;

        // Schedule the next repeating alarm if necessary
        AlarmScheduler.scheduleAlarm(context, this);

        if (updateAlarm) {
            alarmDBService.updateAlarm(this);
        }
    }

    /*
    Setters and Getters
     */

    public int getTimeHour() {
        return timeHour;
    }

    public void setTimeHour(int timeHour) {
        this.timeHour = timeHour;
    }

    public int getTimeMinute() {
        return timeMinute;
    }

    public void setTimeMinute(int timeMinute) {
        this.timeMinute = timeMinute;
    }

    public int getUnlockType() {
        return unlockType;
    }

    public void setUnlockType(int unlockType) {
        this.unlockType = unlockType;
    }

    public boolean getRepeatingDay(int dayOfWeek) {
        return repeatingDays[dayOfWeek];
    }

    public void setRepeatingDay(int dayOfWeek, boolean value) {
        this.repeatingDays[dayOfWeek] = value;
    }

    public boolean[] getRepeatingDays() {
        return repeatingDays;
    }

    public void setRepeatingDays(boolean[] repeatingDays) {
        this.repeatingDays = repeatingDays;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.isVibrate = vibrate;
    }

    public Uri getAlarmTone() {
        return alarmTone;
    }

    public void setAlarmTone(Uri alarmTone) {
        this.alarmTone = alarmTone;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
