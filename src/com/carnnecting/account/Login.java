package com.carnnecting.account;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;

import android.util.Log;
import android.view.Menu;

import com.carnnecting.ws.FBConnect;
import com.cmu.carnnecting.R;
import com.carnnecting.entities.*;

import android.content.ContentValues;
import android.database.SQLException;

// FIXME: To-Be-Removed. These are just to create the db and do bulk-populate in the first time. Using ADB shell is also feasible
import android.database.sqlite.SQLiteDatabase;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;;

public class Login extends FragmentActivity {

	private FBConnect mainFragment;
	private static final String TAG = "Login";

	// FIXME: To-Be-Removed. These are just to create the db and do bulk-populate in the first time. Using ADB shell is also feasible
	private SQLiteDatabase db;
	private CarnnectingSQLiteOpenHelper dbHelper;
	private int userId;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Login Screen");
		/* Use this code to generate hash
		  try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.cmu.carnnecting", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }*/
		
		dbHelper = CarnnectingContract.getCarnnectingSQLiteOpenHelper(this.getApplication());
		db = dbHelper.getWritableDatabase();
		try {
			int nCategories = 10;
			int nEventsPerCategory = 20;
			ContentValues values = new ContentValues();
			// Create several categories
			for (int i = 1; i <= nCategories; i++) {
				values = new ContentValues();
				values.put(CarnnectingContract.Category.COLUMN_NAME_ID, i);
				values.put(CarnnectingContract.Category.COLUMN_NAME_NAME, "catrgory"+i);
				values.put(CarnnectingContract.Category.COLUMN_NAME_DESCRIPTION, "catrgory"+i+" description");
				values.put(CarnnectingContract.Category.COLUMN_NAME_PARENT_CAT_ID, 0); // Root level categories
				db.insert(CarnnectingContract.Category.TABLE_NAME, null, values);
			}
			// Create several events for each of the category
			SimpleDateFormat dateFmt = Event.getDateformat(); //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Use the one in Event class: Event.dateFormat
			Date d = new Date();
			Calendar cal = Calendar.getInstance();
			for (int i = 1; i <= nCategories; i++) {
				for (int j = (i-1)*20+1; j <= (i-1)*20+nEventsPerCategory; j++) {
					values = new ContentValues();
					values.put(CarnnectingContract.Event.COLUMN_NAME_ID, j);
					values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Event"+j+" Subject");
					values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
					// Event duration will be set to one hour
					cal.setTime(d);
					cal.add(Calendar.HOUR, 1);
					values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
					values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "Event"+j+" Location");
					values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Event"+j+" HOST");
					values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Event"+j+" Descriptions......");
					values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, i);
					db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);
					// Event 2 will be one day later than event 1 and so on so forth
					cal.add(Calendar.HOUR, 23);
					d = cal.getTime();
				}
			}
			// Create the categories that the user subscribed to
			for (int i = 1; i <= nCategories/2; i++) {
				values = new ContentValues();
				values.put(CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID, 1);
				values.put(CarnnectingContract.Subscribe.COLUMN_NAME_CATEGORY_ID, i);
				db.insert(CarnnectingContract.Subscribe.TABLE_NAME, null, values);
			}
		} catch (SQLException e) {
			Log.e("ERROR", e.getStackTrace().toString());
		} 
		if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	        mainFragment = new FBConnect();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, mainFragment)
	        .commit();
	    } else {
	        // Or set the fragment from restored state info
	        mainFragment = (FBConnect) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }
		userId = mainFragment.getUserId();
		Log.i(TAG, "User ID in login screen: " + userId);
		setContentView(R.layout.activity_carnnecting_main);			}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
}