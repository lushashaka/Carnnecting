package com.carnnecting.entities;

import java.text.SimpleDateFormat;
import java.util.*;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


@SuppressLint("SimpleDateFormat")
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
	  
	  // HERE
	  public int createEvent(
			  int id, 
			  String subject, 
			  String startTime,	// It's expected that time format to be consistent with Event.getDateformat(): yyyy-MM-dd HH:mm:ss 
			  String endTime,	// It's expected that time format to be consistent with Event.getDateformat(): yyyy-MM-dd HH:mm:ss
			  String location,
			  String host,
			  String description, 
			  int categoryId) 
	  {
		  
		  int newEventId = -1;
		  ContentValues values = new ContentValues();
		  values = new ContentValues();
		  values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, subject);
		  values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, startTime);
		  values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, endTime);
		  values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, location);
		  values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, host);
		  values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, description);
		  values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, categoryId);
		  // Check this out for the return value of db.insert: http://stackoverflow.com/questions/10363884/clarification-on-the-row-id-returned-by-sqlites-insert-statement
		  newEventId = (int)db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);
		  if (newEventId == -1)
			  Log.e("ERROR", "Not able to insert event with subject "+subject);
		  
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
			  Log.i("INFO", "SELECT "+ CarnnectingContract.Event.COLUMN_NAME_ID +","+
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
	  
	  public void getHomeItemModelsByFavoriteEventIds(ArrayList<Integer> favoriteEventIds, ArrayList<HomeItemModel> homeItems) 
			  throws SQLException 
	  {
		  
		  // itemModels must be empty now
		  for (int i = 0; i < favoriteEventIds.size(); i++) {
			  int eventId = favoriteEventIds.get(i);
			  Log.i("INFO_SQL", "SELECT "+ CarnnectingContract.Event.COLUMN_NAME_SUBJECT+","+
					  CarnnectingContract.Event.COLUMN_NAME_START_TIME+","+
					  CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID+" FROM " + CarnnectingContract.Event.TABLE_NAME+
					  " WHERE "+CarnnectingContract.Event.COLUMN_NAME_ID + " = " + eventId);
			  
			  
			  Cursor cursor = db.rawQuery(
					  "SELECT "+ CarnnectingContract.Event.COLUMN_NAME_SUBJECT+","+
							  CarnnectingContract.Event.COLUMN_NAME_START_TIME+","+
							  CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID+" FROM " + CarnnectingContract.Event.TABLE_NAME+
							  " WHERE "+CarnnectingContract.Event.COLUMN_NAME_ID + " = " + eventId, 
					  null);
			  
			  // Convert cursor to HomeItemModel
			  SimpleDateFormat dateOnlyFormat = HomeItemModel.dateOnlyFormat;
			  cursor.moveToFirst();
			  while(!cursor.isAfterLast()) {
				  String subject = cursor.getString(0);
				  String startDate = "01/01/1970";
				  try {
					  startDate = dateOnlyFormat.format(Event.dateFormat.parse(cursor.getString(1)));
				  } catch (Exception e) {
					  Log.e("ERROR", e.getStackTrace().toString());
				  }
				  int catId = cursor.getInt(2);
				  
				  // FIXME: should we discard all the past-due events?
				  
				  HomeItemModel it = new HomeItemModel();
				  it.setEventId(eventId);
				  it.setSubject(subject);
				  it.setStartDate(startDate);
				  it.setCategoryId(catId);
				  homeItems.add(it);
				  
				  cursor.moveToNext();
			  }
		  }
	  }
	  
	  public void getEventsByCategoryIds(int categoryId, ArrayList<HomeItemModel> eventItems) 
			  throws SQLException 
	  {	  	  
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
	  
	  public ArrayList<HomeItemModel> getTodayEvents(int userId) 
			  throws SQLException {
		  ArrayList<HomeItemModel> todayEvents = new ArrayList<HomeItemModel>();
		  Date today = new Date();
		  SimpleDateFormat SQLFormat = new SimpleDateFormat("yyyy-MM-dd");
		  String todaySQLFormat = SQLFormat.format(today);
		  Log.i("GET TODAY", "SELECT " + CarnnectingContract.Event.COLUMN_NAME_START_TIME + " FROM " + CarnnectingContract.Event.TABLE_NAME);
		  String todayEnd = todaySQLFormat + " 23:59:59";
		  String todayStart = todaySQLFormat + " 00:00:00";
		  Log.i("TODAY DATE", todayEnd);
		  
		  Cursor cursor = db.rawQuery(
				  "SELECT "+ CarnnectingContract.Event.COLUMN_NAME_START_TIME 
				  + "," + CarnnectingContract.Event.COLUMN_NAME_ID
				  + "," + CarnnectingContract.Event.COLUMN_NAME_SUBJECT
				  + " FROM " + CarnnectingContract.Event.TABLE_NAME
				  + " WHERE "+ CarnnectingContract.Event.COLUMN_NAME_START_TIME + " < \'" + todayEnd + "\'"
				  + " AND " + CarnnectingContract.Event.COLUMN_NAME_START_TIME + " > \'" + todayStart + "\'"
				  + " AND " + CarnnectingContract.Event.COLUMN_NAME_ID + " IN "
				  + "(SELECT " + CarnnectingContract.RSVP.COLUMN_NAME_EVENT_ID + " FROM " 
				  + CarnnectingContract.RSVP.TABLE_NAME 
				  + " WHERE "+ CarnnectingContract.RSVP.COLUMN_NAME_USER_ID + " = " + userId + ")", 
				  null);
		  
		  // Convert cursor to HomeItemModel
		  SimpleDateFormat dateOnlyFormat = HomeItemModel.dateOnlyFormat;
		  cursor.moveToFirst();
		  while(!cursor.isAfterLast()) {
			  int eventId = cursor.getInt(1);
			  String subject = cursor.getString(2);
			  Log.i("GET DATE SQL", cursor.getString(0));
			  String startDate = "01/01/1970";
			  try {
				  startDate = dateOnlyFormat.format(Event.dateFormat.parse(cursor.getString(0)));
			  } catch (Exception e) {
				  Log.e("ERROR", e.getStackTrace().toString());
			  }
			  
			  // FIXME: should we discard all the past-due events?
			  
			  HomeItemModel it = new HomeItemModel();
			  it.setEventId(eventId);
			  it.setSubject(subject);
			  it.setStartDate(startDate);
			  it.setRSVP(true);
			  todayEvents.add(it);
			  
			  cursor.moveToNext();
		  }
		  
		  return todayEvents;
	  }
	  
	  public ArrayList<HomeItemModel> getTmrwEvents(int userId) 
			  throws SQLException {
		  ArrayList<HomeItemModel> tmrwEvents = new ArrayList<HomeItemModel>();
		  Date today = new Date();
		  SimpleDateFormat SQLFormat = new SimpleDateFormat("yyyy-MM-dd");
		  String tmrwSQLFormat = SQLFormat.format(today);
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(today);
		  cal.add(Calendar.DATE, 1);
		  tmrwSQLFormat = SQLFormat.format(cal.getTime());
		  Log.i("GET TMRW", ""+tmrwSQLFormat);
		  Log.i("GET TODAY", "SELECT " + CarnnectingContract.Event.COLUMN_NAME_START_TIME + " FROM " + CarnnectingContract.Event.TABLE_NAME);
		  String tmrwEnd = tmrwSQLFormat + " 23:59:59";
		  String tmrwStart = tmrwSQLFormat + " 00:00:00";
		  Log.i("TODAY DATE", tmrwEnd);
		  
		  Cursor cursor = db.rawQuery(
				  "SELECT "+ CarnnectingContract.Event.COLUMN_NAME_START_TIME 
				  + "," + CarnnectingContract.Event.COLUMN_NAME_ID
				  + "," + CarnnectingContract.Event.COLUMN_NAME_SUBJECT
				  + " FROM " + CarnnectingContract.Event.TABLE_NAME
				  + " WHERE "+ CarnnectingContract.Event.COLUMN_NAME_START_TIME + " < \'" + tmrwEnd + "\'"
				  + " AND " + CarnnectingContract.Event.COLUMN_NAME_START_TIME + " > \'" + tmrwStart + "\'"
				  + " AND " + CarnnectingContract.Event.COLUMN_NAME_ID + " IN "
				  + "(SELECT " + CarnnectingContract.RSVP.COLUMN_NAME_EVENT_ID + " FROM " 
				  + CarnnectingContract.RSVP.TABLE_NAME 
				  + " WHERE "+ CarnnectingContract.RSVP.COLUMN_NAME_USER_ID + " = " + userId + ")", 
				  null);
		  
		  // Convert cursor to HomeItemModel
		  SimpleDateFormat dateOnlyFormat = HomeItemModel.dateOnlyFormat;
		  cursor.moveToFirst();
		  while(!cursor.isAfterLast()) {
			  int eventId = cursor.getInt(1);
			  String subject = cursor.getString(2);
			  Log.i("GET DATE SQL", cursor.getString(0));
			  String startDate = "01/01/1970";
			  try {
				  startDate = dateOnlyFormat.format(Event.dateFormat.parse(cursor.getString(0)));
			  } catch (Exception e) {
				  Log.e("ERROR", e.getStackTrace().toString());
			  }
			  
			  // FIXME: should we discard all the past-due events?
			  
			  HomeItemModel it = new HomeItemModel();
			  it.setEventId(eventId);
			  it.setSubject(subject);
			  it.setStartDate(startDate);
			  it.setRSVP(true);
			  tmrwEvents.add(it);
			  
			  cursor.moveToNext();
		  }
		  
		  return tmrwEvents;
	  }
	  
	  public ArrayList<HomeItemModel> getUpcomingEvents(int userId) 
			  throws SQLException {
		  ArrayList<HomeItemModel> upcomingEvents = new ArrayList<HomeItemModel>();
		  Date today = new Date();
		  SimpleDateFormat SQLFormat = new SimpleDateFormat("yyyy-MM-dd");
		  String tmrwSQLFormat = SQLFormat.format(today);
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(today);
		  cal.add(Calendar.DATE, 1);
		  tmrwSQLFormat = SQLFormat.format(cal.getTime());
		  Log.i("GET TMRW", ""+tmrwSQLFormat);
		  Log.i("GET TODAY", "SELECT " + CarnnectingContract.Event.COLUMN_NAME_START_TIME + " FROM " + CarnnectingContract.Event.TABLE_NAME);
		  String tmrwEnd = tmrwSQLFormat + " 23:59:59";
		  Log.i("TODAY DATE", tmrwEnd);
		  
		  Cursor cursor = db.rawQuery(
				  "SELECT "+ CarnnectingContract.Event.COLUMN_NAME_START_TIME 
				  + "," + CarnnectingContract.Event.COLUMN_NAME_ID
				  + "," + CarnnectingContract.Event.COLUMN_NAME_SUBJECT
				  + " FROM " + CarnnectingContract.Event.TABLE_NAME
				  + " WHERE "+ CarnnectingContract.Event.COLUMN_NAME_START_TIME + " > \'" + tmrwEnd + "\'"
				  + " AND " + CarnnectingContract.Event.COLUMN_NAME_ID + " IN "
				  + "(SELECT " + CarnnectingContract.RSVP.COLUMN_NAME_EVENT_ID + " FROM " 
				  + CarnnectingContract.RSVP.TABLE_NAME 
				  + " WHERE "+ CarnnectingContract.RSVP.COLUMN_NAME_USER_ID + " = " + userId + ")", 
				  null);
		  
		  // Convert cursor to HomeItemModel
		  SimpleDateFormat dateOnlyFormat = HomeItemModel.dateOnlyFormat;
		  cursor.moveToFirst();
		  while(!cursor.isAfterLast()) {
			  int eventId = cursor.getInt(1);
			  String subject = cursor.getString(2);
			  Log.i("GET DATE SQL", cursor.getString(0));
			  String startDate = "01/01/1970";
			  try {
				  startDate = dateOnlyFormat.format(Event.dateFormat.parse(cursor.getString(0)));
			  } catch (Exception e) {
				  Log.e("ERROR", e.getStackTrace().toString());
			  }
			  
			  // FIXME: should we discard all the past-due events?
			  
			  HomeItemModel it = new HomeItemModel();
			  it.setEventId(eventId);
			  it.setSubject(subject);
			  it.setStartDate(startDate);
			  it.setRSVP(true);
			  upcomingEvents.add(it);
			  
			  cursor.moveToNext();
		  }
		  
		  return upcomingEvents;
	  }
	  
	  public ArrayList<HomeItemModel> getPastEvents(int userId) 
			  throws SQLException {
		  ArrayList<HomeItemModel> pastEvents = new ArrayList<HomeItemModel>();
		  Date today = new Date();
		  SimpleDateFormat SQLFormat = new SimpleDateFormat("yyyy-MM-dd");
		  String todaySQLFormat = SQLFormat.format(today);
		  Log.i("GET TMRW", ""+today);
		  Log.i("GET TODAY", "SELECT " + CarnnectingContract.Event.COLUMN_NAME_START_TIME + " FROM " + CarnnectingContract.Event.TABLE_NAME);
		  String todayStart = todaySQLFormat + " 00:00:00";
		  Log.i("TODAY DATE PAST", todayStart);
		  
		  Cursor cursor = db.rawQuery(
				  "SELECT "+ CarnnectingContract.Event.COLUMN_NAME_START_TIME 
				  + "," + CarnnectingContract.Event.COLUMN_NAME_ID
				  + "," + CarnnectingContract.Event.COLUMN_NAME_SUBJECT
				  + " FROM " + CarnnectingContract.Event.TABLE_NAME
				  + " WHERE "+ CarnnectingContract.Event.COLUMN_NAME_START_TIME + " < \'" + todayStart + "\'"
				  + " AND " + CarnnectingContract.Event.COLUMN_NAME_ID + " IN "
				  + "(SELECT " + CarnnectingContract.RSVP.COLUMN_NAME_EVENT_ID + " FROM " 
				  + CarnnectingContract.RSVP.TABLE_NAME 
				  + " WHERE "+ CarnnectingContract.RSVP.COLUMN_NAME_USER_ID + " = " + userId + ")", 
				  null);
		  
		  // Convert cursor to HomeItemModel
		  SimpleDateFormat dateOnlyFormat = HomeItemModel.dateOnlyFormat;
		  cursor.moveToFirst();
		  while(!cursor.isAfterLast()) {
			  int eventId = cursor.getInt(1);
			  String subject = cursor.getString(2);
			  Log.i("GET DATE SQL", cursor.getString(0));
			  String startDate = "01/01/1970";
			  try {
				  startDate = dateOnlyFormat.format(Event.dateFormat.parse(cursor.getString(0)));
			  } catch (Exception e) {
				  Log.e("ERROR", e.getStackTrace().toString());
			  }
			  
			  // FIXME: should we discard all the past-due events?
			  
			  HomeItemModel it = new HomeItemModel();
			  it.setEventId(eventId);
			  it.setSubject(subject);
			  it.setStartDate(startDate);
			  it.setRSVP(true);
			  pastEvents.add(it);
			  
			  cursor.moveToNext();
		  }
		  
		  return pastEvents;
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
