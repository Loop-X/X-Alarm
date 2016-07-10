package io.github.loopX.XAlarm.module.Alarm;

import android.net.Uri;

import java.util.Calendar;

import io.github.loopX.XAlarm.XAlarmApp;
import io.github.loopX.XAlarm.module.UnlockTypeModule.UnlockTypeEnum;

/**
 * Representation of an Alarm within the app.
 **/
public class Alarm {

    private static final int SNOOZE_DURATION_INTEGER = (5 * 60) * 1000;

    private int     mTimeHour;
    private int     mTimeMinute;
    private int     mUnlockType;
    private int     mSnoozeHour;
    private int     mSnoozeMinute;
    private int     mSnoozeSeconds;
    private boolean mRepeatingDays[];
    private boolean mIsEnabled;
    private boolean mIsVibrate;
    private Uri     mAlarmTone;

    public Alarm() {

        Calendar calendar = Calendar.getInstance();

        mTimeHour = calendar.get(Calendar.HOUR_OF_DAY);
        mTimeMinute = calendar.get(Calendar.MINUTE);

        mSnoozeHour = 0;
        mSnoozeMinute = 0;
        mSnoozeSeconds = 0;

        mUnlockType = UnlockTypeEnum.Type.getID();

        // By default, alarm repeats everyday
        mRepeatingDays = new boolean[]{ true, true, true, true, true, true, true };
        mAlarmTone = Uri.parse(XAlarmApp.getResourcePath() + "/raw/ringtone_0");
        mIsEnabled = true;
        mIsVibrate = true;

    }

    /*
    Setters and Getters
     */

    public int getmTimeHour() {
        return mTimeHour;
    }

    public void setmTimeHour(int mTimeHour) {
        this.mTimeHour = mTimeHour;
    }

    public int getmTimeMinute() {
        return mTimeMinute;
    }

    public void setmTimeMinute(int mTimeMinute) {
        this.mTimeMinute = mTimeMinute;
    }

    public int getmUnlockType() {
        return mUnlockType;
    }

    public void setmUnlockType(int mUnlockType) {
        this.mUnlockType = mUnlockType;
    }

    public int getmSnoozeHour() {
        return mSnoozeHour;
    }

    public void setmSnoozeHour(int mSnoozeHour) {
        this.mSnoozeHour = mSnoozeHour;
    }

    public int getmSnoozeMinute() {
        return mSnoozeMinute;
    }

    public void setmSnoozeMinute(int mSnoozeMinute) {
        this.mSnoozeMinute = mSnoozeMinute;
    }

    public int getmSnoozeSeconds() {
        return mSnoozeSeconds;
    }

    public void setmSnoozeSeconds(int mSnoozeSeconds) {
        this.mSnoozeSeconds = mSnoozeSeconds;
    }

    public boolean[] getmRepeatingDays() {
        return mRepeatingDays;
    }

    public void setmRepeatingDays(boolean[] mRepeatingDays) {
        this.mRepeatingDays = mRepeatingDays;
    }

    public boolean ismIsEnabled() {
        return mIsEnabled;
    }

    public void setmIsEnabled(boolean mIsEnabled) {
        this.mIsEnabled = mIsEnabled;
    }

    public boolean ismIsVibrate() {
        return mIsVibrate;
    }

    public void setmIsVibrate(boolean mIsVibrate) {
        this.mIsVibrate = mIsVibrate;
    }

    public Uri getmAlarmTone() {
        return mAlarmTone;
    }

    public void setmAlarmTone(Uri mAlarmTone) {
        this.mAlarmTone = mAlarmTone;
    }
}
