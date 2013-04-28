package com.carnnecting.entities;

import java.util.ArrayList;
import java.util.HashSet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ReadEventDataSource {
	// Database fields
	private SQLiteDatabase db;
	private CarnnectingSQLiteOpenHelper dbHelper;
	  
	// Projection
	String[] allColumns = {
			CarnnectingContract.ReadEvent.COLUMN_NAME_USER_ID,
			CarnnectingContract.ReadEvent.COLUMN_NAME_EVENT_ID,
	};
	  
	public ReadEventDataSource (Context context) {
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

	public boolean createReadEvent(int userId, int eventId) {
		ContentValues values = new ContentValues();
		values.put(CarnnectingContract.ReadEvent.COLUMN_NAME_USER_ID, userId);
		values.put(CarnnectingContract.ReadEvent.COLUMN_NAME_EVENT_ID, eventId);
		boolean ret = false;
		if (db.insert(CarnnectingContract.ReadEvent.TABLE_NAME, null, values) != -1)
			ret = true;
		CarnnectingContract.setNowDatabaseLastUpdateTimestamp();
		return ret;
	}
	
	public boolean deleteReadEvent(int userId, int eventId) {
		String where = CarnnectingContract.ReadEvent.COLUMN_NAME_USER_ID +" = ? "+ " AND " +
				       CarnnectingContract.ReadEvent.COLUMN_NAME_EVENT_ID +" = ?";
		String[] whereArgs = {userId+"", eventId+""};
		boolean ret = false;
		if (db.delete(CarnnectingContract.ReadEvent.TABLE_NAME, where, whereArgs) == 1)	// Should alwyas be a valid delete
			ret = true;
		CarnnectingContract.setNowDatabaseLastUpdateTimestamp();
		return ret;
	}
	
	public HashSet<Integer> getReadEventIdsByUserId(int userId) {
		HashSet<Integer> readEventIds = new HashSet<Integer>();
		Cursor cursor = db.rawQuery(
				  "SELECT "+CarnnectingContract.ReadEvent.COLUMN_NAME_EVENT_ID + " FROM " + 
						  CarnnectingContract.ReadEvent.TABLE_NAME+
						  " WHERE "+CarnnectingContract.ReadEvent.COLUMN_NAME_USER_ID + " = " + userId, 
				  null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			readEventIds.add(cursor.getInt(0));
			cursor.moveToNext();
		}
		return readEventIds;
	}
}
