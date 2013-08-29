package com.banhong.wifi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;

public class DB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "WIFITimer.db";
    public static final String KeyName = "Name";
    public static final String KeyValue = "Value";
    public static final String Table_Name = "TimeTable";
    public static final String Switch = "Switch";
    public static final String StartHour = "starthour";
    public static final String StartMinute = "startminute";
    public static final String EndHour = "endhour";
    public static final String EndMinute = "endminute";
    public static final int DATABASE_VERSION = 1;
    private static final String Table_creat = "CREATE TABLE IF NOT EXISTS " + Table_Name + "(" + KeyName + " INTEGER primary key, " + KeyValue + " INTEGER)";
    private SQLiteDatabase wifi_db;

    public DB(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        wifi_db = this.getWritableDatabase();

        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(Table_creat);
        wifi_db = db;
        insert(db, Switch, 0);
        insert(db, StartHour, 0);
        insert(db, StartMinute, 0);
        insert(db, EndHour, 0);
        insert(db, EndMinute, 0);
    }

    private long insert(SQLiteDatabase db, String name, int value) {
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();
        cv.put(KeyName, name);
        cv.put(KeyValue, value);
        return db.insert(Table_Name, null, cv);

    }

    private long insert(SQLiteDatabase db, String name, boolean value) {
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();
        cv.put(KeyName, name);
        cv.put(KeyValue, value);
        return db.insert(Table_Name, null, cv);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public void setTime(int Hour, int Minute, boolean isStarttime) {
        if (isStarttime) {
            insert(wifi_db, StartHour, Hour);
            insert(wifi_db, StartMinute, Minute);
        }
        else {
            insert(wifi_db, EndHour, Hour);
            insert(wifi_db, EndMinute, Minute);
        }
    }

    public void setSwitch(boolean state) {
        insert(wifi_db, Switch, state);
    }

    public boolean getServiceState() {
        boolean state = false;
        String[] columns = new String[] { KeyValue };
        String selection = KeyName + "=?";
        String[] StateArgs = new String[] { "" + Switch };
        Cursor Cursor = wifi_db.query(Table_Name, columns, selection, StateArgs, null, null, null);
        while (Cursor.moveToNext()) {
            if (Cursor.getInt(0) == 1)
                state = true;
            else
                state = false;
        }
        return state;
    }

    public Time getStartTime() {
        int hour = 0;
        int min = 0;
        Time time = new Time();
        String[] columns = new String[] { KeyValue };
        String selection = KeyName + "=?";
        String[] HourArgs = new String[] { "" + StartHour };
        String[] MinArgs = new String[] { "" + StartMinute };
        Cursor CursorHour = wifi_db.query(Table_Name, columns, selection, HourArgs, null, null, null);
        Cursor CursorMin = wifi_db.query(Table_Name, columns, selection, MinArgs, null, null, null);
        while (CursorHour.moveToNext()) {
            hour = CursorHour.getInt(0);
        }
        while (CursorMin.moveToNext()) {
            min = CursorMin.getInt(0);
        }
        time.set(0, min, hour, 0, 0, 0);
        return time;
    }

    public Time getEndTime() {
        int hour = 0;
        int min = 0;
        Time time = new Time();
        String[] columns = new String[] { KeyValue };
        String selection = KeyName + "=?";
        String[] HourArgs = new String[] { "" + EndHour };
        String[] MinArgs = new String[] { "" + EndMinute };
        Cursor CursorHour = wifi_db.query(Table_Name, columns, selection, HourArgs, null, null, null);
        Cursor CursorMin = wifi_db.query(Table_Name, columns, selection, MinArgs, null, null, null);
        while (CursorHour.moveToNext()) {
            hour = CursorHour.getInt(0);
        }
        while (CursorMin.moveToNext()) {
            min = CursorMin.getInt(0);
        }
        time.set(0, min, hour, 0, 0, 0);
        return time;
    }
}
