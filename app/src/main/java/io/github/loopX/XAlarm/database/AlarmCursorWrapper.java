package io.github.loopX.XAlarm.database;


import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;

import java.util.UUID;

import io.github.loopX.XAlarm.model.Alarm;

/**
 * This class wraps cursor to add a function which gets alarm instance
 */
public class AlarmCursorWrapper extends CursorWrapper {

    public AlarmCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Alarm getAlarm() {

        // Get all fields from DB

        String uuidString = getString(getColumnIndex(AlarmTable.Columns.UUID));
        int timeHour = getInt(getColumnIndex(AlarmTable.Columns.HOUR));
        int timeMinute = getInt(getColumnIndex(AlarmTable.Columns.MINUTE));
        int unlockType = getInt(getColumnIndex(AlarmTable.Columns.UNLOCK_TYPE));
        String[] repeatingDays = getString(getColumnIndex(AlarmTable.Columns.DAYS)).split(",");
        boolean isVibrate = (getInt(getColumnIndex(AlarmTable.Columns.VIBRATE)) != 0);
        boolean isEnabled = (getInt(getColumnIndex(AlarmTable.Columns.ENABLED)) != 0);
        Uri alarmTone = Uri.parse(getString(getColumnIndex(AlarmTable.Columns.TONE)));

        // Create a new alarm

        Alarm alarm = new Alarm(UUID.fromString(uuidString));

        alarm.setTimeHour(timeHour);
        alarm.setTimeMinute(timeMinute);
        alarm.setAlarmTone(alarmTone);
        alarm.setUnlockType(unlockType);

        for (int i = 0; i < repeatingDays.length; i++) {
            alarm.setRepeatingDay(i, !repeatingDays[i].equals("false"));
        }

        alarm.setVibrate(isVibrate);
        alarm.setEnabled(isEnabled);

        return alarm;
    }
}
