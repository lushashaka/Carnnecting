package com.carnnecting.entities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CarnnectingSQLiteOpenHelper extends SQLiteOpenHelper{
	private static final String 	DATABASE_NAME 		= "carnnecting.db";
	private static final int 		DATABASE_VERSION	= 1;
	
	// Table creation statements
	private static final String SQL_CREATE_CATEGORY = 
			"CREATE TABLE IF NOT EXISTS " + CarnnectingContract.Category.TABLE_NAME + " (" +
			CarnnectingContract.Category.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			CarnnectingContract.Category.COLUMN_NAME_NAME + " TEXT," + 
			CarnnectingContract.Category.COLUMN_NAME_DESCRIPTION + " TEXT," +
			CarnnectingContract.Category.COLUMN_NAME_PARENT_CAT_ID + " INTEGER" +
			")";
	
	private static final String SQL_CREATE_EVENT = 
			"CREATE TABLE IF NOT EXISTS " + CarnnectingContract.Event.TABLE_NAME + " (" +
			CarnnectingContract.Event.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			CarnnectingContract.Event.COLUMN_NAME_SUBJECT + " TEXT," + 
			CarnnectingContract.Event.COLUMN_NAME_START_TIME + " DATETIME," +
			CarnnectingContract.Event.COLUMN_NAME_END_TIME + " DATETIME," +
			CarnnectingContract.Event.COLUMN_NAME_LOCATION + " TEXT," +
			CarnnectingContract.Event.COLUMN_NAME_HOST + " TEXT," +
			CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION + " TEXT, " +
			CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID + " INTEGER" +
			")";
	
	private static final String SQL_CREATE_FAVORITE = 
			"CREATE TABLE IF NOT EXISTS " + CarnnectingContract.Favorite.TABLE_NAME + " (" +
			CarnnectingContract.Favorite.COLUMN_NAME_USER_ID + " INTEGER," + 
			CarnnectingContract.Favorite.COLUMN_NAME_EVENT_ID + " INTEGER," +
			" PRIMARY KEY ("+CarnnectingContract.Favorite.COLUMN_NAME_USER_ID+", "+CarnnectingContract.Favorite.COLUMN_NAME_EVENT_ID+")" +
			")";
	
	private static final String SQL_CREATE_RSVP = 
			"CREATE TABLE IF NOT EXISTS " + CarnnectingContract.RSVP.TABLE_NAME + " (" +
			CarnnectingContract.RSVP.COLUMN_NAME_USER_ID + " INTEGER," + 
			CarnnectingContract.RSVP.COLUMN_NAME_EVENT_ID + " INTEGER," +
			" PRIMARY KEY ("+CarnnectingContract.RSVP.COLUMN_NAME_USER_ID+", "+CarnnectingContract.RSVP.COLUMN_NAME_EVENT_ID+")" +
			")";

	private static final String SQL_CREATE_SUBSCRIBE = 
			"CREATE TABLE IF NOT EXISTS " + CarnnectingContract.Subscribe.TABLE_NAME + " (" +
			CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID + " INTEGER," + 
			CarnnectingContract.Subscribe.COLUMN_NAME_CATEGORY_ID + " INTEGER," +
			" PRIMARY KEY ("+CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID+", "+CarnnectingContract.Subscribe.COLUMN_NAME_CATEGORY_ID+")" +
			")";
	
	private static final String SQL_CREATE_USER = 
			"CREATE TABLE IF NOT EXISTS " + CarnnectingContract.User.TABLE_NAME + " (" +
			CarnnectingContract.User.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
			CarnnectingContract.User.COLUMN_NAME_FB_LOGIN + " TEXT" +
			")";
	
	private static final String SQL_CREATE_READ_EVENT= 
			"CREATE TABLE IF NOT EXISTS " + CarnnectingContract.ReadEvent.TABLE_NAME + " (" +
			CarnnectingContract.ReadEvent.COLUMN_NAME_USER_ID + " INTEGER," + 
			CarnnectingContract.ReadEvent.COLUMN_NAME_EVENT_ID + " INTEGER," +
			" PRIMARY KEY ("+CarnnectingContract.ReadEvent.COLUMN_NAME_USER_ID+", "+CarnnectingContract.ReadEvent.COLUMN_NAME_EVENT_ID+")" +
			")";

	
	public CarnnectingSQLiteOpenHelper (Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		Log.e("INFO", SQL_CREATE_CATEGORY);
		Log.e("INFO", SQL_CREATE_EVENT);
		Log.e("INFO", SQL_CREATE_FAVORITE);
		Log.e("INFO", SQL_CREATE_RSVP);
		Log.e("INFO", SQL_CREATE_SUBSCRIBE);
		Log.e("INFO", SQL_CREATE_READ_EVENT);
		
		db.execSQL(SQL_CREATE_CATEGORY);
		db.execSQL(SQL_CREATE_EVENT);
		db.execSQL(SQL_CREATE_FAVORITE);
		db.execSQL(SQL_CREATE_RSVP);
		db.execSQL(SQL_CREATE_SUBSCRIBE);
		db.execSQL(SQL_CREATE_USER);
		db.execSQL(SQL_CREATE_READ_EVENT);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 Log.w(CarnnectingSQLiteOpenHelper.class.getName(),
			        "Upgrading database from version " + oldVersion + " to "
			            + newVersion + ", which will destroy all old data");
			    db.execSQL("DROP TABLE IF EXISTS " + CarnnectingContract.Category.TABLE_NAME);
			    db.execSQL("DROP TABLE IF EXISTS " + CarnnectingContract.Event.TABLE_NAME);
			    db.execSQL("DROP TABLE IF EXISTS " + CarnnectingContract.Favorite.TABLE_NAME);
			    db.execSQL("DROP TABLE IF EXISTS " + CarnnectingContract.RSVP.TABLE_NAME);
			    db.execSQL("DROP TABLE IF EXISTS " + CarnnectingContract.Subscribe.TABLE_NAME);
			    db.execSQL("DROP TABLE IF EXISTS " + CarnnectingContract.User.TABLE_NAME);
			    db.execSQL("DROP TABLE IF EXISTS " + CarnnectingContract.ReadEvent.TABLE_NAME);
			    
			    onCreate(db);
	}
}
