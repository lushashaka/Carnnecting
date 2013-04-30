package com.carnnecting.entities;

import java.io.ByteArrayOutputStream;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageDataSource {
	// Database fields
	private SQLiteDatabase db;
	private CarnnectingSQLiteOpenHelper dbHelper;
	  
	// Projection
	String[] allColumns = {
			CarnnectingContract.Image.COLUMN_NAME_EVENT_ID,
			CarnnectingContract.Image.COLUMN_NAME_IMAGE
	};
	  
	public ImageDataSource (Context context) {
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

	public boolean createImage(int eventId, Bitmap bmp) {
		// Convert to PNG byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		bmp.compress(Bitmap.CompressFormat.PNG, 100 /*doesn't matter for PNG*/, baos);
		byte[] pngSrc = baos.toByteArray();
		ContentValues values = new ContentValues();
		values.put(CarnnectingContract.Image.COLUMN_NAME_EVENT_ID, eventId);
		values.put(CarnnectingContract.Image.COLUMN_NAME_IMAGE, pngSrc);
		boolean ret = false;
		
		if (db.insert(CarnnectingContract.Image.TABLE_NAME, null, values) != -1)
			ret = true;
		
		CarnnectingContract.setNowDatabaseLastUpdateTimestamp();
		return ret;
	}
	
	public Bitmap getAnImageByEventId(int eventId) {
		Cursor cursor = db.rawQuery("SELECT "+CarnnectingContract.Image.COLUMN_NAME_IMAGE+" FROM "+CarnnectingContract.Image.TABLE_NAME + " WHERE " + 
					CarnnectingContract.Image.COLUMN_NAME_EVENT_ID+" = "+eventId, null);
		if (cursor!= null && cursor.getCount() > 0) {
			// return cursorToRSVP(); FIXME: lets implement this method later
			cursor.moveToFirst();
			byte[] pngArray = cursor.getBlob(0);
			return BitmapFactory.decodeByteArray(pngArray, 0, pngArray.length);
		} else 
			return null;
	}
	
}
