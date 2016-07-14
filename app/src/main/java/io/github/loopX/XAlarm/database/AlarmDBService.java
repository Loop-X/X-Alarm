package io.github.loopX.XAlarm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.UUID;

import io.github.loopX.XAlarm.module.Alarm.Alarm;
import io.github.loopX.XAlarm.database.AlarmDBSchema.AlarmTable;

/**
 * This class is service level for alarm. It provides functions to get/update/delete
 * alarm in DB
 */
public class AlarmDBService {

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public AlarmDBService(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new AlarmDBHelper(mContext).getWritableDatabase();
    }

    /**
     * Gets an alarm instance from DB via alarm id
     * @param id alarm id
     * @return alarm
     */
    public Alarm getAlarm(UUID id) {

        AlarmCursorWrapper cursor = new AlarmCursorWrapper(mDatabase.query(
                AlarmTable.NAME,
                null, // gets all columns
                AlarmTable.Columns.UUID + " = ?", // cause
                new String[]{id.toString()}, // args
                null,
                null,
                AlarmTable.Columns.HOUR + ", " + AlarmTable.Columns.MINUTE // orderby
        ));


        try {
            if (cursor.getCount() == 0) {
                return null;
            } else {
                cursor.moveToFirst();
                return cursor.getAlarm();
            }
        } finally {
            cursor.close();
        }

    }

    /**
     * Update alarm in DB
     * @param alarm alarm to update
     */
    public void updateAlarm(Alarm alarm) {

        ContentValues values = createContentValues(alarm);

        mDatabase.update(AlarmTable.NAME, values,
                AlarmTable.Columns.UUID + " = ?",
                new String[]{alarm.getId().toString()});
    }

    /**
     * Deletes alarm from DB
     * @param alarm alarm instance to remove
     */
    public void deleteAlarm(Alarm alarm) {
        mDatabase.delete(AlarmTable.NAME,
                AlarmTable.Columns.UUID + " = ?",
                new String[] { alarm.getId().toString() });
    }

    /**
     * Transforms alarm instance to content value format
     * @param alarm alarm to transform
     * @return
     */
    private static ContentValues createContentValues(Alarm alarm) {

        ContentValues values = new ContentValues();

        values.put(AlarmTable.Columns.UUID, alarm.getId().toString());
        values.put(AlarmTable.Columns.HOUR, alarm.getTimeHour());
        values.put(AlarmTable.Columns.MINUTE, alarm.getTimeMinute());
        values.put(AlarmTable.Columns.UNLOCK_TYPE, alarm.getUnlockType());
        values.put(AlarmTable.Columns.TONE, alarm.getAlarmTone().toString());
        String repeatingDays = "";
        for (int i = 0; i < 7; ++i) {
            repeatingDays += alarm.getRepeatingDay(i) + ",";
        }
        values.put(AlarmTable.Columns.DAYS, repeatingDays);
        values.put(AlarmTable.Columns.VIBRATE, alarm.isVibrate() ? 1 : 0);
        values.put(AlarmTable.Columns.ENABLED, alarm.isEnabled() ? 1 : 0);

        return values;

    }
}
