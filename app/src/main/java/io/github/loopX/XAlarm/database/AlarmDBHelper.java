package io.github.loopX.XAlarm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import io.github.loopX.XAlarm.database.AlarmDBSchema.AlarmTable;

public class AlarmDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "xalarm.db";

    public AlarmDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( "create table " + AlarmDBSchema.AlarmTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                AlarmTable.Columns.UUID + ", " +
                AlarmTable.Columns.ENABLED + ", " +
                AlarmTable.Columns.HOUR + ", " +
                AlarmTable.Columns.MINUTE + ", " +
                AlarmTable.Columns.UNLOCK_TYPE + ", " +
                AlarmTable.Columns.DAYS + ", " +
                AlarmTable.Columns.TONE + ", " +
                AlarmTable.Columns.VIBRATE + ", " +
                AlarmTable.Columns.SNOOZED + ", " +
                AlarmTable.Columns.SNOOZED_HOUR + ", " +
                AlarmTable.Columns.SNOOZED_MINUTE + ", " +
                AlarmTable.Columns.SNOOZED_SECONDS +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {

        Log.w(this.getClass().getSimpleName(),
                "Upgrading database from version " + oldVer + " to " + newVer);

        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + AlarmTable.NAME);

        // Create new table
        onCreate(db);
    }
}
