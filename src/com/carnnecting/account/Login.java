package com.carnnecting.account;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.carnnecting.ws.FBConnect;
import com.cmu.carnnecting.R;
import com.carnnecting.category.CategoryMenu;
import com.carnnecting.entities.*;
import com.carnnecting.home.Home;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;

// FIXME: To-Be-Removed. These are just to create the db and do bulk-populate in the first time. Using ADB shell is also feasible
import android.database.sqlite.SQLiteDatabase;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Login extends FragmentActivity {

	private FBConnect mainFragment;
	private static final String TAG = "Login";

	// FIXME: To-Be-Removed. These are just to create the db and do bulk-populate in the first time. Using ADB shell is also feasible
	private SQLiteDatabase db;
	private CarnnectingSQLiteOpenHelper dbHelper;
	
	// FIXME: To-Be-Removed. These are to demo how to use DataSoruce classes
	private SubscribeDataSource subscribeDAO;
	private CategoryDataSource categoryDAO;
	private EventDataSource eventDAO;
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
		//
		// FIXME: To-Be-Removed. These are to demo how to use DataSoruce (DAO) classes
		//
		/*
		// Testing subscribeDAO
		subscribeDAO = new SubscribeDataSource(this.getApplication());
		subscribeDAO.open();
		ArrayList<Integer> subscribedCatIds;
		ArrayList<Subscribe> subscribes = subscribeDAO.getSubscribeByUserId(1);
		for (int i = 0; i < subscribes.size(); i++) {
			Subscribe subscribe = subscribes.get(i);
			Log.e("INFO", subscribe.toString());
		}
		
		// Tesing categoryDAO
		categoryDAO = new CategoryDataSource(this.getApplication());
		categoryDAO.open();
		ArrayList<Category> subscribedCategories = categoryDAO.getSubscribedCategoriesByUserId(1);
		for (int i = 0; i < subscribedCategories.size(); i++) {
			Category category = subscribedCategories.get(i);
			Log.e("INFO", category.toString());
		}
		
		// Testing EventDAO
		eventDAO = new EventDataSource(this.getApplication());
		eventDAO.open();
		ArrayList<Event> events = eventDAO.getAllEvents();
		for (int i = 0; i < events.size(); i+=5) {
			Log.e("INFO", events.get(i).toString());
		}
		*/
		
		//
		// FIXME: To-Be-Removed. These are just to create the db and do bulk-populate in the first time. Using ADB shell is also feasible
		//
		
		dbHelper = CarnnectingContract.getCarnnectingSQLiteOpenHelper(this.getApplication());
		db = dbHelper.getWritableDatabase();
		try {
			int nCategories = 10;
			int nEventsPerCategory = 20;
			
			//db.beginTransaction();
			// Create a single user
			ContentValues values = new ContentValues();
//			values.put(CarnnectingContract.User.COLUMN_NAME_ID, 1);
//			values.put(CarnnectingContract.User.COLUMN_NAME_FB_LOGIN, "user1@fb.com");
//			db.insert(CarnnectingContract.User.TABLE_NAME, null, values);
//			Log.e("INFO", "user inserted");
			
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
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Use the one in Event class: Event.dateFormat
			for (int i = 1; i <= nCategories; i++) {
				for (int j = (i-1)*20+1; j <= (i-1)*20+nEventsPerCategory; j++) {
					values = new ContentValues();
					values.put(CarnnectingContract.Event.COLUMN_NAME_ID, j);
					values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Event"+j+" Subject");
					values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(new Date()));
					values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(new Date()));  // Both start and end times are set to now
					values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, i);
					values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Event"+j+" Descriptions......");
					db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);
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
		} finally {
			// db.endTransaction();
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
		setContentView(R.layout.activity_carnnecting_main);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.carnnecting_main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
	    switch (item.getItemId()) {
	        case R.id.news_feed:
	            // app icon in action bar clicked; go home
	            intent = new Intent(this, Home.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        case R.id.categories:
	        	intent = new Intent(this, CategoryMenu.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	intent.putExtra("userId", userId); //TODO: remove hard coded userId, should get current logged in userId
	        	startActivity(intent);
	        	return true;	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
