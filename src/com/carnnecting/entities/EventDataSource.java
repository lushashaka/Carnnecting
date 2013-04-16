package com.carnnecting.entities;

import java.util.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class EventDataSource {
	// Database fields
	  private SQLiteDatabase db;
	  private CarnnectingSQLiteOpenHelper dbHelper;
	  
	  // Projection
	  String[] allColumns = {
			  CarnnectingContract.Event.COLUMN_NAME_ID,
			  CarnnectingContract.Event.COLUMN_NAME_SUBJECT,
			  CarnnectingContract.Event.COLUMN_NAME_START_TIME,
			  CarnnectingContract.Event.COLUMN_NAME_END_TIME,
			  CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION,
			  CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID
	  };
	  
	  public EventDataSource (Context context) {
		  dbHelper = new CarnnectingSQLiteOpenHelper(context);
	  }
	  
	  public void open() throws SQLException {
		  db = dbHelper.getWritableDatabase();
	  }
	  
	  public void close() throws SQLException {
		  db.close();
		  dbHelper.close();
	  }
	  
	  // TODO: fill in the (necessary subset of) CRUD operations here. For now we just need to read
	  public boolean createEvent(int id, int categoryId, String subject, Date startTime, Date endTime, String description) {
		  return true;
	  }
	  
	  public boolean deleteEvent(int id) { // Primary key
		  return true;
	  }
	  
	  public ArrayList<Event> getAllEvents() throws SQLException{
		  ArrayList<Event> events = new ArrayList<Event>();
		  Cursor cursor = db.query(CarnnectingContract.Event.TABLE_NAME,
			        allColumns, null, null, null, null, null);
		  
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  events.add(cursorToEvent(cursor));
			  cursor.moveToNext();
		  }
		  return events;
	  }
	  
	  /*
	  public ArrayList<Category> getSubscribedCategoriesByUserId(int userId) throws SQLException {
		  ArrayList<Category> subscribedCategories = new ArrayList<Category>();
		  
		  Cursor cursor = db.rawQuery(
				  "SELECT * from "+CarnnectingContract.Category.TABLE_NAME+" WHERE "+ 
						  CarnnectingContract.Category.COLUMN_NAME_ID + " IN " + "(" +
						  	"SELECT "+CarnnectingContract.Subscribe.COLUMN_NAME_CATEGORY_ID+" FROM " + CarnnectingContract.Subscribe.TABLE_NAME + " WHERE "
						  	+ CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID + "="+ userId+
						  ")", 
				  null);
		  
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  events.add(cursorToEvent(cursor));
			  cursor.moveToNext();
		  }
		  
		  return subscribedCategories;
	  }
	  */
	  
	  private Event cursorToEvent(Cursor cursor) {
		  // TODO: don't hardcode 0, 1, 2, 3... Instead, define them in contract class.
		  try {
			  return new Event(cursor.getInt(0), cursor.getString(1), Event.dateFormat.parse(cursor.getString(2)), Event.dateFormat.parse(cursor.getString(3)), cursor.getString(4), cursor.getInt(5));
		  } catch (Exception e) {
			  Log.e("ERROR", e.getStackTrace().toString());
			  return new Event(-1, "ERORR ORCCURED!", new Date(), new Date(), "ERROR: exception when cursorToEvent()", 0);
		  }
	  }
}
