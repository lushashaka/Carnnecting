package com.carnnecting.entities;

import java.util.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class CategoryDataSource {
	// Database fields
	  private SQLiteDatabase db;
	  private CarnnectingSQLiteOpenHelper dbHelper;
	  
	  // Projection
	  String[] allColumns = {
			  CarnnectingContract.Category.COLUMN_NAME_ID,
			  CarnnectingContract.Category.COLUMN_NAME_NAME,
			  CarnnectingContract.Category.COLUMN_NAME_DESCRIPTION,
			  CarnnectingContract.Category.COLUMN_NAME_PARENT_CAT_ID
	  };
	  
	  public CategoryDataSource (Context context) {
		  // dbHelper = new CarnnectingSQLiteOpenHelper(context);
		  dbHelper = CarnnectingContract.getCarnnectingSQLiteOpenHelper(context);
	  }
	  
	  public void open() throws SQLException {
		  db = dbHelper.getWritableDatabase();
	  }
	  
	  public void close() throws SQLException {
		  // db.close();
		  // dbHelper.close();
		  // According to http://stackoverflow.com/questions/7930139/android-database-locked. It is only a file handle
		  // and will be recycled once the application finishes.
	  }
	  
	  // TODO: fill in the (necessary subset of) CRUD operations here. For now we just need to read
	  public boolean createCategory(int id, String name, String description, int parentCatId) {
		  return true;
	  }
	  
	  public boolean deleteCategory(int id) { // Primary key
		  return true;
	  }
	  
	  public ArrayList<Category> getAllCategories() throws SQLException{
		  ArrayList<Category> categories = new ArrayList<Category>();
		  Cursor cursor = db.query(CarnnectingContract.Category.TABLE_NAME,
			        allColumns, null, null, null, null, null); 
		  
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  categories.add(cursorToCategory(cursor));
			  cursor.moveToNext();
		  }
		  return categories;
	  }
	  
	  public ArrayList<Integer> getSubscribedCategoryIdsByUserId(int userId) throws SQLException {
		  ArrayList<Integer> subscribedCategoryIds = new ArrayList<Integer>();
		  
		  Cursor cursor = db.rawQuery(
				  "SELECT "+CarnnectingContract.Category.COLUMN_NAME_ID+" from "+CarnnectingContract.Category.TABLE_NAME+ " WHERE "
				  + CarnnectingContract.Category.COLUMN_NAME_ID + " IN " + "(" +
						  	"SELECT "+CarnnectingContract.Subscribe.COLUMN_NAME_CATEGORY_ID+" FROM " + CarnnectingContract.Subscribe.TABLE_NAME + " WHERE "
						  	+ CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID + "="+ userId+
						  ")", 
				  null);
		  
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  subscribedCategoryIds.add(cursor.getInt(0));
			  cursor.moveToNext();
		  }
		  
		  return subscribedCategoryIds;
	  }
	  
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
			  subscribedCategories.add(cursorToCategory(cursor));
			  cursor.moveToNext();
		  }
		  
		  return subscribedCategories;
	  }
	  
	  public ArrayList<Category> getOtherCategoriesByUserId(int userId) throws SQLException {
		  ArrayList<Category> otherCategories = new ArrayList<Category>();
		  
		  Cursor cursor = db.rawQuery(
				  "SELECT * from "+CarnnectingContract.Category.TABLE_NAME+" WHERE "+ 
						  CarnnectingContract.Category.COLUMN_NAME_ID + " NOT IN " + "(" +
						  	"SELECT "+CarnnectingContract.Subscribe.COLUMN_NAME_CATEGORY_ID+" FROM " + CarnnectingContract.Subscribe.TABLE_NAME + " WHERE "
						  	+ CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID + "=="+ userId+
						  ")", 
				  null);
		  
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  otherCategories.add(cursorToCategory(cursor));
			  cursor.moveToNext();
		  }
		  
		  return otherCategories;
	  }

	  private Category cursorToCategory(Cursor cursor) {
		  // TODO: don't hardcode 0, 1, 2, 3... Instead, define them in contract class.
		  return new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
	  }
}
