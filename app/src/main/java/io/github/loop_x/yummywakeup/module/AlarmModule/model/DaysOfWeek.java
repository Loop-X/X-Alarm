package io.github.loop_x.yummywakeup.module.AlarmModule.model;

import android.content.Context;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import io.github.loop_x.yummywakeup.R;

/**
 * To presents days of week by a single int.
 * 0x01: Monday    00000001  1 -> 0
 * 0x02: Tuesday   00000010  2 -> 1
 * 0x04: Wednesday 00000100  4 -> 2
 * 0x08: Thursday  00001000  8 -> 3
 * 0x10: Friday    00010000 16 -> 4
 * 0x20: Saturday  00100000 32 -> 5
 * 0x40: Sunday    01000000 64 -> 6
 */
public class DaysOfWeek {

    public final static int MONDAY    = 0;
    public final static int TUESDAY   = 1;
    public final static int WEDNESDAY = 2;
    public final static int THURSDAY  = 3;
    public final static int FRIDAY    = 4;
    public final static int SATURDAY  = 5;
    public final static int SUNDAY    = 6;

    private static int[] DAY_MAP = new int[] {
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY,
            Calendar.SUNDAY,
    };

    // Bitmask of all repeating days
    // 0000 0101 = 5 means Monday & Wednesday
    private int mDays;

    public DaysOfWeek(int days) {
        mDays = days;
    }

    /**
     * Check whether a day is set
     * @param day
     * @return
     */
    public boolean isSet(int day) {
        return ((mDays & (1 << day)) > 0);
    }

    /**
     * Set or unset a day by boolean
     * @param day ex. Monday = 1
     * @param set ture/set false/unset
     */
    public void set(int day, boolean set) {
        if (set) {
            mDays |= (1 << day);
        } else {
            mDays &= ~(1 << day);
        }
    }

    public void set(DaysOfWeek dow) {
        mDays = dow.mDays;
    }

    public int getCoded() {
        return mDays;
    }

    /**
     * Returns days of week encoded in an array of booleans.
     * @return Array of booleans to present days of week
     */
    public boolean[] getBooleanArray() {
        boolean[] ret = new boolean[7];
        for (int i = 0; i < 7; i++) {
            ret[i] = isSet(i);
        }
        return ret;
    }

    /**
     * Check whether repeat is set
     * @return true if repeat
     */
    public boolean isRepeatSet() {
        return mDays != 0;
    }

    /**
     * returns number of days from today until next alarm
     * @param c must be set to today
     */
    public int getNextAlarm(Calendar c) {
        if (mDays == 0) {
            return -1;
        }
        int today = (c.get(Calendar.DAY_OF_WEEK) + 5) % 7;

        int day = 0;
        int dayCount = 0;
        for (; dayCount < 7; dayCount++) {
            day = (today + dayCount) % 7;
            if (isSet(day)) {
                break;
            }
        }
        return dayCount;
    }

    public String toString(Context context, boolean showNever) {
        StringBuilder ret = new StringBuilder();

        // No days
        if (mDays == 0) {
            return showNever ?
                    context.getText(R.string.never).toString() : "";
        }

        // Every day
        if (mDays == 0x7f) {
            return context.getText(R.string.every_day).toString();
        }

        // Count selected days. For exemple, Wednesday & Friday 00010100.
        // So it exists two 1 in mDays. dayCount = 2.
        int dayCount = 0, days = mDays;
        while (days > 0) {
            if ((days & 1) == 1) dayCount++;
            days >>= 1;
        }

        // Full or abbreviated name of the day
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] dayList = (dayCount > 1) ?
                dfs.getShortWeekdays() :
                dfs.getWeekdays();

        // selected days
        for (int i = 0; i < 7; i++) {
            if ((mDays & (1 << i)) != 0) {
                ret.append(dayList[DAY_MAP[i]]);
                dayCount -= 1;
                if (dayCount > 0) ret.append(
                        context.getText(R.string.day_concat));
            }
        }
        return ret.toString();
    }
}
