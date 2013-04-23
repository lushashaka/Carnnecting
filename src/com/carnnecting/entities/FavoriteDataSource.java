package com.carnnecting.entities;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class FavoriteDataSource {
	// Database fields
	private SQLiteDatabase db;
	private CarnnectingSQLiteOpenHelper dbHelper;
	  
	// Projection
	String[] allColumns = {
			CarnnectingContract.Favorite.COLUMN_NAME_USER_ID,
			CarnnectingContract.Favorite.COLUMN_NAME_EVENT_ID,
	};
	  
	public FavoriteDataSource (Context context) {
		dbHelper = new CarnnectingSQLiteOpenHelper(context);
	}
	  
	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() throws SQLException {
		db.close();
		dbHelper.close();
	}

	public boolean createFavorite(int userId, int eventId) {
		ContentValues values = new ContentValues();
		values.put(CarnnectingContract.Favorite.COLUMN_NAME_USER_ID, userId);
		values.put(CarnnectingContract.Favorite.COLUMN_NAME_EVENT_ID, eventId);
		boolean ret = false;
		if (db.insert(CarnnectingContract.Favorite.TABLE_NAME, null, values) != -1)
			ret = true;
		CarnnectingContract.setNowDatabaseLastUpdateTimestamp();
		return ret;
	}
	
	public boolean deleteFavorite(int userId, int eventId) {
		String where = CarnnectingContract.Favorite.COLUMN_NAME_USER_ID +" = ? "+ " AND " +
				       CarnnectingContract.Favorite.COLUMN_NAME_EVENT_ID +" = ?";
		String[] whereArgs = {userId+"", eventId+""};
		boolean ret = false;
		if (db.delete(CarnnectingContract.Favorite.TABLE_NAME, where, whereArgs) == 1)	// Should alwyas be a valid delete
			ret = true;
		CarnnectingContract.setNowDatabaseLastUpdateTimestamp();
		return ret;
	}
	
	public ArrayList<Integer> getFavoriteEventIdsByUserId(int userId) {
		ArrayList<Integer> favoriteEventIds = new ArrayList<Integer>();
		Cursor cursor = db.rawQuery(
				  "SELECT "+CarnnectingContract.Favorite.COLUMN_NAME_EVENT_ID + " FROM " + 
						  CarnnectingContract.Favorite.TABLE_NAME+
						  " WHERE "+CarnnectingContract.Favorite.COLUMN_NAME_USER_ID + " = " + userId, 
				  null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			favoriteEventIds.add(cursor.getInt(0));
			cursor.moveToNext();
		}
		return favoriteEventIds;
	}
}
