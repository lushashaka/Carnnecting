package com.carnnecting.entities;

import java.sql.Timestamp;
import java.util.Date;

import android.content.Context;
import android.util.Log;

public class CarnnectingContract {
	// To prevent the contract class from being instantiated
	private CarnnectingContract() {}

	// Category table
	public static abstract class Category {
		public static final String TABLE_NAME 						= "category";
		public static final String COLUMN_NAME_ID					= "id";
		public static final String COLUMN_NAME_NAME 				= "name";
		public static final String COLUMN_NAME_DESCRIPTION 			= "description";
		public static final String COLUMN_NAME_PARENT_CAT_ID		= "parent_category_id";
	}
	
	// Event table
	public static abstract class Event {
		public static final String TABLE_NAME						= "event";
		public static final String COLUMN_NAME_ID					= "id";
		public static final String COLUMN_NAME_SUBJECT				= "subject";
		public static final String COLUMN_NAME_START_TIME			= "start_time";
		public static final String COLUMN_NAME_END_TIME				= "end_time";
		public static final String COLUMN_NAME_LOCATION				= "location";
		public static final String COLUMN_NAME_HOST					= "host";
		public static final String COLUMN_NAME_DESCRIPTION			= "description";
		public static final String COLUMN_NAME_CATEGORY_ID			= "category_id";
	}
	
	// Favorite table
	public static abstract class Favorite {
		public static final String TABLE_NAME						= "favorite";
		public static final String COLUMN_NAME_USER_ID				= "user_id";
		public static final String COLUMN_NAME_EVENT_ID				= "event_id";
	}
	
	// RSVP table
	public static abstract class RSVP {
		public static final String TABLE_NAME						= "rsvp";
		public static final String COLUMN_NAME_USER_ID				= "user_id";
		public static final String COLUMN_NAME_EVENT_ID				= "event_id";
	}
	
	// Subscribe table
	public static abstract class Subscribe {
		public static final String TABLE_NAME						= "subscribe";
		public static final String COLUMN_NAME_USER_ID				= "user_id";
		public static final String COLUMN_NAME_CATEGORY_ID			= "category_id";
	}
		
	// User table
	public static abstract class User {
		public static final String TABLE_NAME						= "user";
		public static final String COLUMN_NAME_ID					= "id";
		public static final String COLUMN_NAME_FB_LOGIN				= "fb_login";
	}
	
	// Read_event table
	public static abstract class ReadEvent {
		public static final String TABLE_NAME						= "read_event";
		public static final String COLUMN_NAME_USER_ID				= "user_id";
		public static final String COLUMN_NAME_EVENT_ID				= "event_id";
	}
	
	/*
	 * The timestamp when the database was last changed (CUD of CRUD).
	 * We maintain this to minimize access to database. That is, if database was not changed since last load, we don't need to reload.
	 * 
	 * Note that I don't know where to put this. Here is the best place so far.
	 */
	private static Long databaseLastUpdateTimestamp = null;
	public static synchronized Long getDatabaseLastUpdateTimestamp() {
		// FIXME: Is the code robust enough even given the possibility that android will destroy random objects w/o notice?
		if (databaseLastUpdateTimestamp == null) {
			// In case Android mythically destroy the static object...?
			databaseLastUpdateTimestamp = new Long(new Date().getTime());
		}
		
		return new Long(databaseLastUpdateTimestamp.longValue());
	}
	
	public static synchronized void setNowDatabaseLastUpdateTimestamp() {
		databaseLastUpdateTimestamp = new Long(new Date().getTime());
	}
	
	// The only SQLiteOpenHelper in the system. I don't know where to put it but here;
	private static CarnnectingSQLiteOpenHelper dbHelper = null;
	public static synchronized CarnnectingSQLiteOpenHelper getCarnnectingSQLiteOpenHelper(Context context) { // Should be got via getApplication()
		if (dbHelper == null)
			dbHelper = new CarnnectingSQLiteOpenHelper(context);
		return dbHelper;
	}
}
