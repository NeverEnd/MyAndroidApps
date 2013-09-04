package com.banhong.wifi;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract.DeletedContacts;
import android.text.format.Time;

public class DB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "WIFITimer.db";
    
    public static final String Table_Name = "TimeTable";
    public static final String Switch = "Switch";
    public static final String StartHour = "starthour";
    public static final String StartMinute = "startminute";
    public static final String EndHour = "endhour";
    public static final String EndMinute = "endminute";
    public static final int DATABASE_VERSION = 2;
    private static final String Table_creat = "CREATE TABLE IF NOT EXISTS " + Table_Name + " (" + Switch + " INTEGER primary key, " + StartHour + " INTEGER,"
                                                                                            + StartMinute + " INTEGER, "
                                                                                            + EndHour + " INTEGER, "
                                                                                            + EndMinute + " INTEGER" 
                                                                                            + ")";
    private SQLiteDatabase wifi_db;

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        wifi_db = this.getReadableDatabase();
    }
    
    
    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        super.finalize();
        wifi_db.close();
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Table_creat);


        wifi_db = db;
        ContentValues cv = new ContentValues();
        cv.put(Switch, 0);
        cv.put(StartHour, 0);
        cv.put(StartMinute, 0);
        cv.put(EndHour, 0);
        cv.put(EndMinute, 0);
        db.insert(Table_Name, null,cv);
    }

    private long dbUpdate(SQLiteDatabase db, String name, int value) {
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();
        cv.put(name, value);
        return db.update(Table_Name, cv, null, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("ALTER TABLE "+Table_Name+"  RENAME TO " + Table_Name+oldVersion);
        //SQLiteDatabase.deleteDatabase(new File(DATABASE_NAME));
        System.out.append("db.getVersion() == "+db.getVersion());
        

    }

    public void setTime(int Hour, int Minute, boolean isStarttime) {
        if (isStarttime) {
            dbUpdate(wifi_db, StartHour, Hour);
            dbUpdate(wifi_db, StartMinute, Minute);
        }
        else {
            dbUpdate(wifi_db, EndHour, Hour);
            dbUpdate(wifi_db, EndMinute, Minute);
        }
    }

    public void setSwitch(boolean state) {
        dbUpdate(wifi_db, Switch, state==true?1:0);
    }

    public int getColumnData(String columnName){
        String[] columns = new String[] { columnName };
        Cursor cursor = wifi_db.query(Table_Name, columns, null, null, null, null, null);
        while(cursor.moveToNext()){
            return cursor.getInt(0);            
        }        
        return 0;
    }
    
    public boolean getServiceState() {
        if (getColumnData(Switch) == 1)
            return true;
        else
            return false;
    }

    public Time getStartTime() {
        int hour = 0;
        int min = 0;
        Time time = new Time();

        hour = getColumnData(StartHour);
        min = getColumnData(StartMinute);        
        time.set(0, min, hour, 0, 0, 0);
        return time;
    }

    public Time getEndTime() {
        int hour = 0;
        int min = 0;
        Time time = new Time();
       
        hour = getColumnData(EndHour);
        min = getColumnData(EndMinute);
        time.set(0, min, hour, 0, 0, 0);
        return time;
    }
    
 
}
