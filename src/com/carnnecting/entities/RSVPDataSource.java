package com.carnnecting.entities;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RSVPDataSource {
	// Database fields
	private SQLiteDatabase db;
	private CarnnectingSQLiteOpenHelper dbHelper;
	  
	// Projection
	String[] allColumns = {
			CarnnectingContract.RSVP.COLUMN_NAME_USER_ID,
			CarnnectingContract.RSVP.COLUMN_NAME_EVENT_ID
	};
	  
	public RSVPDataSource (Context context) {
		dbHelper = new CarnnectingSQLiteOpenHelper(context);
	}
	  
	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() throws SQLException {
		db.close();
		dbHelper.close();
	}

	public boolean createRSVP(int userId, int eventId) {
		ContentValues values = new ContentValues();
		values.put(CarnnectingContract.RSVP.COLUMN_NAME_USER_ID, userId);
		values.put(CarnnectingContract.RSVP.COLUMN_NAME_EVENT_ID, eventId);
		boolean ret = false;
		if (db.insert(CarnnectingContract.RSVP.TABLE_NAME, null, values) != -1)
			ret = true;
		CarnnectingContract.setNowDatabaseLastUpdateTimestamp();
		return ret;
	}
	
	public boolean deleteRSVP(int userId, int eventId) {
		String where = CarnnectingContract.RSVP.COLUMN_NAME_USER_ID +" = ? "+ " AND " +
				       CarnnectingContract.RSVP.COLUMN_NAME_EVENT_ID +" = ?";
		String[] whereArgs = {userId+"", eventId+""};
		
		boolean ret = false;
		if (db.delete(CarnnectingContract.RSVP.TABLE_NAME, where, whereArgs) == 1)	// Should alwyas be a valid delete
			ret = true;
		CarnnectingContract.setNowDatabaseLastUpdateTimestamp();
		return ret;
	}
	
	public RSVP getAnRSVPByUserIdAndEventId(int userId, int eventId) {
		Cursor cursor = db.rawQuery("SELECT * FROM "+CarnnectingContract.RSVP.TABLE_NAME + " WHERE " + 
					CarnnectingContract.RSVP.COLUMN_NAME_USER_ID+" = "+userId+" AND "+ 
					CarnnectingContract.RSVP.COLUMN_NAME_EVENT_ID+" = "+eventId, null);
		if (cursor!= null && cursor.getCount() > 0) {
			// return cursorToRSVP(); FIXME: lets implement this method later
			return new RSVP(userId, eventId);
		} else 
			return null;
	}
	
	public ArrayList<Integer> getRSVPEventIdsByUserId(int userId) {
		ArrayList<Integer> RSVPEventIds = new ArrayList<Integer>();
		Cursor cursor = db.rawQuery(
				  "SELECT "+CarnnectingContract.RSVP.COLUMN_NAME_EVENT_ID + " FROM " + 
						  CarnnectingContract.RSVP.TABLE_NAME+
						  " WHERE "+CarnnectingContract.RSVP.COLUMN_NAME_USER_ID + " = " + userId, 
				  null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			RSVPEventIds.add(cursor.getInt(0));
			cursor.moveToNext();
		}
		return RSVPEventIds;
	}
}
