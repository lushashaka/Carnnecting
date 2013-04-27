package com.carnnecting.entities;

import java.text.SimpleDateFormat;
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
			  CarnnectingContract.Event.COLUMN_NAME_LOCATION,
			  CarnnectingContract.Event.COLUMN_NAME_HOST,
			  CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION,
			  CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID
	  };
	  
	  public EventDataSource (Context context) {
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
	  
	  // TODO: fill in the (necessary subset of) CRUD operations here. For now we just need to read
	  public int createEvent(
			  int id, 
			  String subject, 
			  Date startTime, 
			  Date endTime,
			  String location,
			  String host,
			  String description, 
			  int categoryId) 
	  {
		  
		  int newEventId = -1;
		  return newEventId;
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
	  
	  public Event getAnEventByEventId(int eventId) {
		  Cursor cursor = db.rawQuery(" SELECT * FROM "+CarnnectingContract.Event.TABLE_NAME+" WHERE "+
				  			CarnnectingContract.Event.COLUMN_NAME_ID+" = "+eventId, null);

		  cursor.moveToFirst();
		  // We should have just one record returned
		  return cursorToEvent(cursor);
	  }
	  
	  public void getHomeItemModelsByCategoryIds(ArrayList<Integer> categoryIds, ArrayList<HomeItemModel> homeItems) 
			  throws SQLException 
	  {
		  
		  // itemModels must be empty now
		  for (int i = 0; i < categoryIds.size(); i++) {
			  int catId = categoryIds.get(i);
			  Log.e("INFO", "SELECT "+ CarnnectingContract.Event.COLUMN_NAME_ID +","+
					  CarnnectingContract.Event.COLUMN_NAME_SUBJECT+","+
					  CarnnectingContract.Event.COLUMN_NAME_START_TIME+" FROM " + CarnnectingContract.Event.TABLE_NAME+
					  " WHERE "+CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID + " = " + catId);
			  
			  
			  Cursor cursor = db.rawQuery(
					  "SELECT "+ CarnnectingContract.Event.COLUMN_NAME_ID +","+
							  CarnnectingContract.Event.COLUMN_NAME_SUBJECT+","+
							  CarnnectingContract.Event.COLUMN_NAME_START_TIME+" FROM " + CarnnectingContract.Event.TABLE_NAME+
							  " WHERE "+CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID + " = " + catId, 
					  null);
			  
			  // Convert cursor to HomeItemModel
			  SimpleDateFormat dateOnlyFormat = HomeItemModel.dateOnlyFormat;
			  cursor.moveToFirst();
			  while(!cursor.isAfterLast()) {
				  int eventId = cursor.getInt(0);
				  String subject = cursor.getString(1);
				  String startDate = "01/01/1970";
				  try {
					  startDate = dateOnlyFormat.format(Event.dateFormat.parse(cursor.getString(2)));
				  } catch (Exception e) {
					  Log.e("ERROR", e.getStackTrace().toString());
				  }
				  
				  // FIXME: should we discard all the past-due events?
				  
				  HomeItemModel it = new HomeItemModel();
				  it.setEventId(eventId);
				  it.setCategoryId(catId);
				  it.setSubject(subject);
				  it.setStartDate(startDate);
				  homeItems.add(it);
				  
				  cursor.moveToNext();
			  }
		  }
	  }
	  
	  public void getEventsByCategoryIds(int categoryId, ArrayList<HomeItemModel> eventItems) 
			  throws SQLException 
	  {
		  
		  // itemModels must be empty now
			  /*Log.e("INFO", "SELECT "+ CarnnectingContract.Event.COLUMN_NAME_ID +","+
					  CarnnectingContract.Event.COLUMN_NAME_SUBJECT+","+
					  CarnnectingContract.Event.COLUMN_NAME_START_TIME+" FROM " + CarnnectingContract.Event.TABLE_NAME+
					  " WHERE "+CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID + " = " + categoryId);*/
			  
			  
			  Cursor cursor = db.rawQuery(
					  "SELECT "+ CarnnectingContract.Event.COLUMN_NAME_ID +","+
							  CarnnectingContract.Event.COLUMN_NAME_SUBJECT+","+
							  CarnnectingContract.Event.COLUMN_NAME_START_TIME+" FROM " + CarnnectingContract.Event.TABLE_NAME+
							  " WHERE "+CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID + " = " + categoryId, 
					  null);
			  
			  // Convert cursor to HomeItemModel
			  SimpleDateFormat dateOnlyFormat = HomeItemModel.dateOnlyFormat;
			  cursor.moveToFirst();
			  while(!cursor.isAfterLast()) {
				  int eventId = cursor.getInt(0);
				  String subject = cursor.getString(1);
				  String startDate = "01/01/1970";
				  try {
					  startDate = dateOnlyFormat.format(Event.dateFormat.parse(cursor.getString(2)));
				  } catch (Exception e) {
					  Log.e("ERROR", e.getStackTrace().toString());
				  }
				  
				  // FIXME: should we discard all the past-due events?
				  
				  HomeItemModel it = new HomeItemModel();
				  it.setEventId(eventId);
				  it.setCategoryId(categoryId);
				  it.setSubject(subject);
				  it.setStartDate(startDate);
				  eventItems.add(it);
				  
				  cursor.moveToNext();
			  }
		  
	  }
	  
	  private Event cursorToEvent(Cursor cursor) {
		  // TODO: don't hardcode 0, 1, 2, 3... Instead, define them in contract class.
		  try {
			  return new Event(cursor.getInt(0), cursor.getString(1), Event.dateFormat.parse(cursor.getString(2)), Event.dateFormat.parse(cursor.getString(3)), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7));
		  } catch (Exception e) {
			  Log.e("ERROR", e.getStackTrace().toString());
			  return new Event(-1, "ERORR ORCCURED!", new Date(), new Date(),"NAV", "NAV", "ERROR: exception when cursorToEvent()", 0);
		  }
	  }
}
