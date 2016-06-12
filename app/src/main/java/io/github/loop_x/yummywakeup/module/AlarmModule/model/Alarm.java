/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.loop_x.yummywakeup.module.AlarmModule.model;

import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import java.util.Calendar;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.module.UnlockTypeModule.UnlockTypeEnum;

public final class Alarm implements Parcelable {

    //////////////////////////////
    //序列化的Parcelable接口
    //////////////////////////////
    public static final Creator<Alarm> CREATOR
            = new Creator<Alarm>() {
                public Alarm createFromParcel(Parcel p) {
                    return new Alarm(p);
                }

                public Alarm[] newArray(int size) {
                    return new Alarm[size];
                }
            };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel p, int flags) {
        p.writeInt(id);
        p.writeInt(enabled ? 1 : 0);
        p.writeInt(hour);
        p.writeInt(minutes);
        p.writeInt(daysOfWeek.getCoded());
        p.writeLong(time);
        p.writeInt(vibrate ? 1 : 0);
        p.writeString(label);
        p.writeParcelable(alert, flags);
        p.writeInt(unlockType);
        p.writeInt(unlockDiffLevel);
    }
    //////////////////////////////
    // by end
    //////////////////////////////

    //////////////////////////////
    // 定义列
    //////////////////////////////
    public static class Columns implements BaseColumns {

        /**
         * The content:// 为这个表定义一个共享的Url
         */
        public static final Uri CONTENT_URI =
                Uri.parse("content://io.github.loop_x.yummywakeup/alarm");

        /**
         * Hour 0 - 23.
         */
        public static final String HOUR = "hour";

        /**
         * Minutes 0 - 59
         */
        public static final String MINUTES = "minutes";

        /**
         * Days of week coded as integer
         */
        public static final String DAYS_OF_WEEK = "daysofweek";

        /**
         * Alarm time in UTC milliseconds from the epoch.
         */
        public static final String ALARM_TIME = "alarmtime";

        /**
         * True if alarm is active
         */
        public static final String ENABLED = "enabled";

        /**
         * True if alarm should vibrate
         */
        public static final String VIBRATE = "vibrate";

        /**
         * Message to show when alarm triggers
         * Note: not currently used
         */
        public static final String MESSAGE = "message";

        /**
         * Audio alert to play when alarm triggers
         */
        public static final String ALERT = "alert";

        /**
         * Unlock Type of alarm
         */
        public static final String UNLOCK_TYPE = "unlock_type";

        /**
         * Difficult of unlock type
         */
        public static final String UNLOCK_DIFF_LEVEL  = "unlock_diff_lvl";

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER =
                HOUR + ", " + MINUTES + " ASC";

        // Used when filtering enabled alarms.
        public static final String WHERE_ENABLED = ENABLED + "=1";

        public static final String[] ALARM_QUERY_COLUMNS = {
            _ID, HOUR, MINUTES, DAYS_OF_WEEK, ALARM_TIME,
            ENABLED, VIBRATE, MESSAGE, ALERT ,UNLOCK_TYPE, UNLOCK_DIFF_LEVEL};

        /**
         * These save calls to cursor.getColumnIndexOrThrow()
         * THEY MUST BE KEPT IN SYNC WITH ABOVE QUERY COLUMNS
         */
        public static final int ALARM_ID_INDEX = 0;
        public static final int ALARM_HOUR_INDEX = 1;
        public static final int ALARM_MINUTES_INDEX = 2;
        public static final int ALARM_DAYS_OF_WEEK_INDEX = 3;
        public static final int ALARM_TIME_INDEX = 4;
        public static final int ALARM_ENABLED_INDEX = 5;
        public static final int ALARM_VIBRATE_INDEX = 6;
        public static final int ALARM_MESSAGE_INDEX = 7;
        public static final int ALARM_ALERT_INDEX = 8;
        public static final int ALARM_UNLOCK_TYPE = 9;
        public static final int ALARM_UNLOCK_DIFF_LEVEL = 10;

    }
    //////////////////////////////
    // End 每一列定义结束
    //////////////////////////////

    // 对应的公共的每一列的映射
    public int        id;
    public int        hour;
    public int        minutes;
    public DaysOfWeek daysOfWeek;
    public long       time;
    public boolean    enabled;
    public boolean    vibrate;
    public String     label;
    public Uri        alert;
    public int        unlockType;
    public int        unlockDiffLevel;

    public Alarm(Cursor c) {

        id         = c.getInt(Columns.ALARM_ID_INDEX);
        hour       = c.getInt(Columns.ALARM_HOUR_INDEX);
        minutes    = c.getInt(Columns.ALARM_MINUTES_INDEX);
        daysOfWeek = new DaysOfWeek(c.getInt(Columns.ALARM_DAYS_OF_WEEK_INDEX));
        time       = c.getLong(Columns.ALARM_TIME_INDEX);
        enabled    = c.getInt(Columns.ALARM_ENABLED_INDEX) == 1;
        vibrate    = c.getInt(Columns.ALARM_VIBRATE_INDEX) == 1;
        label      = c.getString(Columns.ALARM_MESSAGE_INDEX);
        String alertString = c.getString(Columns.ALARM_ALERT_INDEX);
        unlockType = c.getInt(Columns.ALARM_UNLOCK_TYPE);
        unlockDiffLevel = c.getInt(Columns.ALARM_UNLOCK_DIFF_LEVEL);


        if (alertString != null && alertString.length() != 0) {
            alert = Uri.parse(alertString);
        }

        // If the database alert is null or it failed to parse, use the
        // default alert.
        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        }

    }

    public Alarm(Parcel p) {
        id = p.readInt();
        enabled = p.readInt() == 1;
        hour = p.readInt();
        minutes = p.readInt();
        daysOfWeek = new DaysOfWeek(p.readInt());
        time = p.readLong();
        vibrate = p.readInt() == 1;
        label = p.readString();
        alert = (Uri) p.readParcelable(null);
        unlockType = p.readInt();
        unlockDiffLevel = p.readInt();
    }

    /**
     * Creates a default alarm at the current time
     */
    public Alarm() {
        id = -1;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        hour = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);
        vibrate = true;
        enabled = true;
        daysOfWeek = new DaysOfWeek(0x7f); // By default, it will repeat every day
        alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        unlockType = UnlockTypeEnum.Normal.getID();
        unlockDiffLevel = 1;
    }

    /**
     * Sets default label if it is not set
     */
    public String getLabelOrDefault(Context context) {
        if (label == null || label.length() == 0) {
            return context.getString(R.string.default_label);
        }
        return label;
    }
}
