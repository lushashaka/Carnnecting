package com.carnnecting.entities;

import java.util.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class SubscribeDataSource {
	// Database fields
	  private SQLiteDatabase db;
	  private CarnnectingSQLiteOpenHelper dbHelper;
	  
	  // Projection
	  String[] allColumns = {
			  CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID,
			  CarnnectingContract.Subscribe.COLUMN_NAME_CATEGORY_ID,
	  };
	  
	  public SubscribeDataSource (Context context) {
		  // dbHelper = new CarnnectingSQLiteOpenHelper(context);
		  dbHelper = CarnnectingContract.getCarnnectingSQLiteOpenHelper(context);
	  }
	  
	  public void open() throws SQLException {
		  db = dbHelper.getWritableDatabase();
	  }
	  
	  public void close() throws SQLException {
		  db.close();
		  // According to http://stackoverflow.com/questions/7930139/android-database-locked. It is only a file handle
		  // and will be recycled once the application finishes.
		  // dbHelper.close();
	  }
	  
	  // TODO: fill in the (necessary subset of) CRUD operations here. For now we just need to read
	  public boolean createSubscribe(int userId, int categoryId) {
		  return true;
	  }
	  
	  public boolean deleteSubscribe(int userId, int categoryId) {
		  return true;
	  }
	  
	  public ArrayList<Subscribe> getSubscribeByUserId(int userId) throws SQLException{
		  ArrayList<Subscribe> subscribes = new ArrayList<Subscribe>();
		  Cursor cursor = db.rawQuery("SELECT * FROM "+CarnnectingContract.Subscribe.TABLE_NAME+" WHERE " + 
		  CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID+"="+userId, null);
		  
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  subscribes.add(cursorToSubscribe(cursor));
			  cursor.moveToNext();
		  }
		  return subscribes;
	  }
	  
	  public ArrayList<Integer> getSubscribedCatIdsByUserId(int userId) throws SQLException {
		  ArrayList<Integer> subscribedCatIds = new ArrayList<Integer>();
		  Cursor cursor = db.rawQuery("SELECT "+ CarnnectingContract.Subscribe.COLUMN_NAME_CATEGORY_ID +" FROM "+CarnnectingContract.Subscribe.TABLE_NAME+" WHERE " + 
				  CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID+"="+userId, null);
		  
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  // FIXME: I believe hard code 0 here is always safe?
			  subscribedCatIds.add(cursor.getInt(0));
			  cursor.moveToNext();
		  }
		  
		  return subscribedCatIds;
	  }
	  
	  private Subscribe cursorToSubscribe(Cursor cursor) {
		  // TODO: don't hardcode 0, 1. Instead, define them in contract class.
		  return new Subscribe(cursor.getInt(0), cursor.getInt(1));
	  }
}
