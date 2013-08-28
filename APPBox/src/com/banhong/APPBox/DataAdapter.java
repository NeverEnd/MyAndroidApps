package com.banhong.APPBox;

import java.security.KeyStore.LoadStoreParameter;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataAdapter{
	public static final String BOX_PIC_ID = "box_picID";
	public static final String BOX_TITLE = "box_title";
	public static final String ENTRY_BOX_TITLE = "entry_boxtitle";
	public static final String ENTRY_PKNAME = "entry_pkname";
	
	private static final String DATABASE_NAME = "APPBox.db";
	private static final int DATABASE_VERSION = 1;
	private static final String BOX_TABLE_NAME = "BoxList";
	private static final String ENTRY_TABLE_NAME = "EntryList";
	
	private DataBaseHelp mOpenHelper;
	
	public DataAdapter(Context context)
	{
		mOpenHelper = new DataBaseHelp(context);		
	}
	
	
	public void AddOneBox(int picId, String title)
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(BOX_PIC_ID, picId);
		values.put(BOX_TITLE, title);
		db.insert(BOX_TABLE_NAME, null, values);
		
	}
	
	public void DeleteBox(String title)
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		db.delete(BOX_TABLE_NAME, BOX_TITLE+"="+title, null);
	}
	
	public void AddOneEntry(String boxtitle, String packageName)
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ENTRY_BOX_TITLE, boxtitle);
		values.put(ENTRY_PKNAME, packageName);
		db.insert(ENTRY_TABLE_NAME, null, values);
	}
	
	public void DeleteEntry(String PackageName)
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		db.delete(ENTRY_TABLE_NAME, BOX_TITLE+"="+PackageName, null);		
	}
	
	public ArrayList<String> getAllEntrys()
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ArrayList<String> rult = new ArrayList<String>();
		Cursor cursor = db.query(ENTRY_TABLE_NAME, null, null, null, null, null, null);
		while(cursor!=null && cursor.moveToNext())
		{
			rult.add(cursor.getString(cursor.getColumnIndex(ENTRY_PKNAME)));
		}
		return rult;
	}

	public ArrayList<ContentValues> getAllBox()
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ArrayList<ContentValues> rult = new ArrayList<ContentValues>();
		
		Cursor cursor = db.query(ENTRY_TABLE_NAME, null, null, null, null, null, null);
		while(cursor!=null && cursor.moveToNext())
		{			
			ContentValues con = new ContentValues();
			con.put(BOX_TITLE, cursor.getString(cursor.getColumnIndex(BOX_TITLE)));
			con.put(BOX_PIC_ID, cursor.getInt(cursor.getColumnIndex(BOX_PIC_ID)));
			rult.add(con);
		}
		return rult;
		
	}
	
	
	private class DataBaseHelp extends SQLiteOpenHelper {
		
		public DataBaseHelp(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated+ constructor stub
		}
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE "+BOX_TABLE_NAME+ " ("+BOX_TITLE+" TEXT PRIMARY KEY,"+BOX_PIC_ID+" INTEGER"+");");
			db.execSQL("CREATE TABLE "+ENTRY_TABLE_NAME+" ("+"_id INTEGER PRIMARY KEY,"+"boxTitle TEXT,"+"pkName TEXT"+");");
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}
	
	public static class Box 
	{
		public int mpicID;
		public String mtitle;
		
		public Box(int picID, String title)
		{
			mpicID = picID;
			mtitle = title;			
		}
		
	}
}