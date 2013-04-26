package com.carnnecting.entities;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class UserDataSource {
	// Database fields
	private SQLiteDatabase db;
	private CarnnectingSQLiteOpenHelper dbHelper;

	public UserDataSource (Context context) {
		// dbHelper = new CarnnectingSQLiteOpenHelper(context);
		dbHelper = CarnnectingContract.getCarnnectingSQLiteOpenHelper(context);
	}
	  
	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() throws SQLException {
		// db.close();
		// According to http://stackoverflow.com/questions/7930139/android-database-locked. It is only a file handle
		// and will be recycled once the application finishes.
		// dbHelper.close();
	}

	public boolean createUser(String fbName) {
		// Check if the fbName is unique
		Cursor cursor = db.rawQuery("SELECT * FROM "+CarnnectingContract.User.TABLE_NAME + " WHERE " + 
				CarnnectingContract.User.COLUMN_NAME_FB_LOGIN+" = '"+fbName.trim()+"'"
				, null);
		if (cursor != null && cursor.getCount() > 0) return false;
		
		ContentValues values = new ContentValues();
		values.put(CarnnectingContract.User.COLUMN_NAME_FB_LOGIN, fbName);
		
		boolean ret = false;
		if (db.insert(CarnnectingContract.User.TABLE_NAME, null, values) != -1)
			ret = true;
		
		CarnnectingContract.setNowDatabaseLastUpdateTimestamp();
		return ret;
	}
	
	public int getUserIdByFbName(String fbName) {
		Cursor cursor = db.rawQuery("SELECT * FROM "+CarnnectingContract.User.TABLE_NAME + " WHERE " + 
				CarnnectingContract.User.COLUMN_NAME_FB_LOGIN+" = '"+fbName.trim()+"'"
				, null);
		
		if (cursor!= null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			return cursor.getInt(0);
		}
		
		return -1;
	}
}
