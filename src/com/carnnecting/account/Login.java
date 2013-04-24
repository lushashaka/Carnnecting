package com.carnnecting.account;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.carnnecting.home.Home;
import com.cmu.carnnecting.R;
import com.carnnecting.entities.*;

import android.database.SQLException;


// FIXME: To-Be-Removed. These are just to create the db and do bulk-populate in the first time. Using ADB shell is also feasible
import android.database.sqlite.SQLiteDatabase;
import com.carnnecting.entities.*;
import java.util.Date;
import java.text.SimpleDateFormat;

// FIXME: To-Be-Removed. These are to demo how to use DataSoruce classes
import java.util.ArrayList;



public class Login extends Activity {

	private Button demoButton;
	
	// FIXME: To-Be-Removed. These are just to create the db and do bulk-populate in the first time. Using ADB shell is also feasible
	private SQLiteDatabase db;
	private CarnnectingSQLiteOpenHelper dbHelper;
	
	// FIXME: To-Be-Removed. These are to demo how to use DataSoruce classes
	private SubscribeDataSource subscribeDAO;
	private CategoryDataSource categoryDAO;
	private EventDataSource eventDAO;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carnnecting_main);
		
		
		//
		// FIXME: To-Be-Removed. These are to demo how to use DataSoruce (DAO) classes
		//
		
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
		
		
		//
		// FIXME: To-Be-Removed. These are just to create the db and do bulk-populate in the first time. Using ADB shell is also feasible
		//
		
		dbHelper = CarnnectingContract.getCarnnectingSQLiteOpenHelper(this.getApplication());
		db = dbHelper.getWritableDatabase();
		try {
			int nCategories = 10;
			int nEventsPerCategory = 20;
			
			db.beginTransaction();
			// Create a single user
			ContentValues values = new ContentValues();
			values.put(CarnnectingContract.User.COLUMN_NAME_ID, 1);
			values.put(CarnnectingContract.User.COLUMN_NAME_FB_LOGIN, "user1@fb.com");
			db.insert(CarnnectingContract.User.TABLE_NAME, null, values);
			Log.e("INFO", "user inserted");
			
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
		
		demoButton = (Button) findViewById(R.id.demoOnlyButton);
		
		demoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Home.class);
				startActivity(intent);
			}
		});
		
		
		//test comment
		
	}
}
