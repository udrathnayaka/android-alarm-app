package com.example.android_alarm_app.Alarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android_alarm_app.Schema.AlarmDbSchema.*;

public class Database extends SQLiteOpenHelper {

    private static final int version = 1;
    private static final String DATABASE_NAME = "alarmBase.db";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    // Database table initializing
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + AlarmTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        AlarmTable.Cols.ALARMID + ", " +
                        AlarmTable.Cols.HOUR + ", " +
                        AlarmTable.Cols.MINUTE + ", " +
                        AlarmTable.Cols.REPEAT + ", " +
                        AlarmTable.Cols.DAYSOFTHEWEEK + ", " +
                        AlarmTable.Cols.ON + ", " +
                        AlarmTable.Cols.DIFFICULTY + ", " +
                        AlarmTable.Cols.ALARMTONE + ", " +
                        AlarmTable.Cols.SNOOZE + ", " +
                        AlarmTable.Cols.VIBRATE +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
