/**
 * 
 */
package com.carnnecting.entities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author Vijay
 *
 */
public class PopulateDB {
	private SQLiteDatabase db;
	private CarnnectingSQLiteOpenHelper dbHelper;
	
	public void populateDBData(Context context) {
		dbHelper = CarnnectingContract.getCarnnectingSQLiteOpenHelper(context);
		db = dbHelper.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();

			// Create several categories
//			for (int i = 1; i <= nCategories; i++) {
//				values = new ContentValues();
//				values.put(CarnnectingContract.Category.COLUMN_NAME_ID, i);
//				values.put(CarnnectingContract.Category.COLUMN_NAME_NAME, "catrgory"+i);
//				values.put(CarnnectingContract.Category.COLUMN_NAME_DESCRIPTION, "catrgory"+i+" description");
//				values.put(CarnnectingContract.Category.COLUMN_NAME_PARENT_CAT_ID, 0); // Root level categories
//				db.insert(CarnnectingContract.Category.TABLE_NAME, null, values);
//			}
			
			values = new ContentValues();
			values.put(CarnnectingContract.Category.COLUMN_NAME_ID, 1);
			values.put(CarnnectingContract.Category.COLUMN_NAME_NAME, "Android");
			values.put(CarnnectingContract.Category.COLUMN_NAME_DESCRIPTION, "Google Android Developer Events");
			values.put(CarnnectingContract.Category.COLUMN_NAME_PARENT_CAT_ID, 0); // Root level categories
			db.insert(CarnnectingContract.Category.TABLE_NAME, null, values);

			values = new ContentValues();
			values.put(CarnnectingContract.Category.COLUMN_NAME_ID, 2);
			values.put(CarnnectingContract.Category.COLUMN_NAME_NAME, "iOS");
			values.put(CarnnectingContract.Category.COLUMN_NAME_DESCRIPTION, "Apple iOS Developer Events");
			values.put(CarnnectingContract.Category.COLUMN_NAME_PARENT_CAT_ID, 0); // Root level categories
			db.insert(CarnnectingContract.Category.TABLE_NAME, null, values);
			
			values = new ContentValues();
			values.put(CarnnectingContract.Category.COLUMN_NAME_ID, 3);
			values.put(CarnnectingContract.Category.COLUMN_NAME_NAME, "Hadoop");
			values.put(CarnnectingContract.Category.COLUMN_NAME_DESCRIPTION, "Bay Area Hadoop Events");
			values.put(CarnnectingContract.Category.COLUMN_NAME_PARENT_CAT_ID, 0); // Root level categories
			db.insert(CarnnectingContract.Category.TABLE_NAME, null, values);
			
			values = new ContentValues();
			values.put(CarnnectingContract.Category.COLUMN_NAME_ID, 4);
			values.put(CarnnectingContract.Category.COLUMN_NAME_NAME, "Biking");
			values.put(CarnnectingContract.Category.COLUMN_NAME_DESCRIPTION, "Bay Area Biking Events");
			values.put(CarnnectingContract.Category.COLUMN_NAME_PARENT_CAT_ID, 0); // Root level categories
			db.insert(CarnnectingContract.Category.TABLE_NAME, null, values);
			
			values = new ContentValues();
			values.put(CarnnectingContract.Category.COLUMN_NAME_ID, 5);
			values.put(CarnnectingContract.Category.COLUMN_NAME_NAME, "Hiking");
			values.put(CarnnectingContract.Category.COLUMN_NAME_DESCRIPTION, "Subscribe for Hiking Events");
			values.put(CarnnectingContract.Category.COLUMN_NAME_PARENT_CAT_ID, 0); // Root level categories
			db.insert(CarnnectingContract.Category.TABLE_NAME, null, values);

			values = new ContentValues();
			values.put(CarnnectingContract.Category.COLUMN_NAME_ID, 6);
			values.put(CarnnectingContract.Category.COLUMN_NAME_NAME, "Startups");
			values.put(CarnnectingContract.Category.COLUMN_NAME_DESCRIPTION, "Silicon Valley Startup Incubation and Acceleration");
			values.put(CarnnectingContract.Category.COLUMN_NAME_PARENT_CAT_ID, 0); // Root level categories
			db.insert(CarnnectingContract.Category.TABLE_NAME, null, values);

			values = new ContentValues();
			values.put(CarnnectingContract.Category.COLUMN_NAME_ID, 7);
			values.put(CarnnectingContract.Category.COLUMN_NAME_NAME, "Hackathons");
			values.put(CarnnectingContract.Category.COLUMN_NAME_DESCRIPTION, "Subscribe to receive information about Hackathons happening around you");
			values.put(CarnnectingContract.Category.COLUMN_NAME_PARENT_CAT_ID, 0); // Root level categories
			db.insert(CarnnectingContract.Category.TABLE_NAME, null, values);

			values = new ContentValues();
			values.put(CarnnectingContract.Category.COLUMN_NAME_ID, 8);
			values.put(CarnnectingContract.Category.COLUMN_NAME_NAME, "Fitness");
			values.put(CarnnectingContract.Category.COLUMN_NAME_DESCRIPTION, "Zumba, Bootcamps, Aerobics and more..");
			values.put(CarnnectingContract.Category.COLUMN_NAME_PARENT_CAT_ID, 0); // Root level categories
			db.insert(CarnnectingContract.Category.TABLE_NAME, null, values);

			values = new ContentValues();
			values.put(CarnnectingContract.Category.COLUMN_NAME_ID, 9);
			values.put(CarnnectingContract.Category.COLUMN_NAME_NAME, "Node.js");
			values.put(CarnnectingContract.Category.COLUMN_NAME_DESCRIPTION, "Bay Area Node.js meetups");
			values.put(CarnnectingContract.Category.COLUMN_NAME_PARENT_CAT_ID, 0); // Root level categories
			db.insert(CarnnectingContract.Category.TABLE_NAME, null, values);
			
			values = new ContentValues();
			values.put(CarnnectingContract.Category.COLUMN_NAME_ID, 10);
			values.put(CarnnectingContract.Category.COLUMN_NAME_NAME, "Mobile Innovators");
			values.put(CarnnectingContract.Category.COLUMN_NAME_DESCRIPTION, "Want to do something new in Mobile? Subscribe!");
			values.put(CarnnectingContract.Category.COLUMN_NAME_PARENT_CAT_ID, 0); // Root level categories
			db.insert(CarnnectingContract.Category.TABLE_NAME, null, values);



			// Create several events for each of the category
			SimpleDateFormat dateFmt = Event.getDateformat(); //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Use the one in Event class: Event.dateFormat
			Date d = new Date();
			Calendar cal = Calendar.getInstance();
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 1);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Introduction to Camera API");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 1);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "1600 Amphitheatre Pkwy, Mountain View, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Google");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "This event is targeted for people who would like to get started with using Android Camera APIs. A basic knowledge of Android application development is required.");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 1);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 2);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Monetizing your Android app");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 1);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "Moscone Center West 800 Howard St., San Francisco, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Apps World");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "All are welcome for a discussion with the panel tackling Android monetization issues");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 1);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 3);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Getting started with Objective-C");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 1);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "Apple Incorporated, Infinite Loop, Cupertino, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Apple");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Introduction to Objective-C, the programming language for building iOS applications.");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 2);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 4);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Building games on iOS");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 1);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "Apple Development, Odessa Avenue, Los Angeles, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Apple Developer Group");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Come and learn how to develop games on iOS. Intermediate knowledge of iOS required.");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 2);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();

			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 5);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Hadoop User Group Monthly Meetup");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 2);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "701 1st Ave, Sunnyvale, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Yahoo!");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Detailed agenda and summaries to follow. Hadoop updates and socialize over food and beer(s)");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 3);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 6);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "How to use Hadoop effectively?");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 1);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "Yahoo!, West Java Drive, Sunnyvale, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Yahoo!");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "This session focuses on how you can use Hadoop for faster analysis of data");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 3);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 7);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Mountain Biking Tips");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 1);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "Sunnyvale Community Center, East Remington Drive, Sunnyvale, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Sunnyvale Biking Club");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Tips from professional bikers");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 4);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 8);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "De LaVeaga Park (DLV) - Ride");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 4);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "Vista De Laveaga, Santa Cruz, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Santa Cruz Biking Club");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "This will be a nice beginner level ride. Expect about a 60- 90 minute ride. Bring water and be sure your bike is ready to ride");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 4);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 9);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Welcome Walk");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 2);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "2960 Shoreline Blvd. , Mountain View, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Michael's Cafe");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "This is our monthly walk to introduce prospective members ");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 5);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();

			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 10);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Half Moon Bay Beach Hike");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 3);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "20 Stone Pine Road #A, Half Moon Bay, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Half Moon Bay Coffee Company");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "A moderately paced 5 mile mostly flat beach hike along Half Moon Bay State Beach");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 5);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();

			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 11);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Conference: Game Changing Technology");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 1);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "1065 La Avenida Avenue, Building 1, Mountain View, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Microsoft");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Over the next few years game changing technologies will change how we market, sell, communicate, analyze, manufacture and much more. Come listen to and meet experts in the field.");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 6);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 12);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Leadership training: Silicon Valley Entrepreneurs Toastmasters Open Meeting");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 1);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "97 E Brokaw Rd, San Jose, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Lusha");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "The Silicon Valley Entrepreneurs Toastmasters Club welcomes all entrepreneurs, engineers, professionals and students to join the meetings.");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 6);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 13);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "EA Hackathon");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 8);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "209 Redwood Shores Pkwy, Redwood City, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Electronic Arts");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Build a game in 8 hours and win cool prizes");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 7);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();

			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 14);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "LinkedIn Hackathon");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 8);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "2029 Stierlin Ct, Mountain View, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "LinkedIn");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Explore and analyze the world of Big Data by using LinkedIn APIs");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 7);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 21);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Sprint Hackathon");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 8);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "150 W San Carlos St, San Jose, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Sprint");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Develop cool apps using Sprint APIs for amazing prizes");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 7);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 22);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "ESPN Hackathon");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 8);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "1011 S Figueroa St B 101, Los Angeles, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Sprint");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Hackathon for sports enthusiasts");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 7);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 23);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Facebook Hackathon");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 8);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "1 Hacker Way, Menlo Park, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Facebook");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Build apps using Facebook APIs");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 7);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 15);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Bootcamp Session");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 1);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "60 N Winchester Blvd, Santa Clara, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Snap Fitness");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Great Bootcamp session. Free for members");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 8);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 16);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Zumba");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 1);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "1400 N Shoreline Blvd, Mountain View, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Gold's gym");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Come, indulge in Zumba. Couples only.");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 8);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 17);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Beginner - Node.js");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 1);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "1 Hacker Way, Menlo Park, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Facebook");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Introduction to Node.js");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 9);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 18);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "Is Node.js the next big thing?");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 1);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "1 Hacker Way, Menlo Park, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Facebook");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Participate in this discussion with panel of experts");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 9);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 19);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "HTML5 vs Native Android: Smart Enterprises for the Future");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 2);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "809 11th Avenue Building 4, Sunnyvale, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Motorola");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "Join Suzanne Alexandra, Android Technology Evangelist at Motorola Mobility, to learn more about the many factors involved in choosing native Android or HTML5 for apps that meet enterprise requirements.");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 10);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			values = new ContentValues();
			values.put(CarnnectingContract.Event.COLUMN_NAME_ID, 20);
			values.put(CarnnectingContract.Event.COLUMN_NAME_SUBJECT, "What Web Developers Need to Know to Develop Windows 8 Apps");
			values.put(CarnnectingContract.Event.COLUMN_NAME_START_TIME, dateFmt.format(d));
			// Event duration will be set to one hour
			cal.setTime(d);
			cal.add(Calendar.HOUR, 2);
			values.put(CarnnectingContract.Event.COLUMN_NAME_END_TIME, dateFmt.format(cal.getTime()));
			values.put(CarnnectingContract.Event.COLUMN_NAME_LOCATION, "1020 Enterprise Way, Building B, Sunnyvale, CA");
			values.put(CarnnectingContract.Event.COLUMN_NAME_HOST, "Microsoft");
			values.put(CarnnectingContract.Event.COLUMN_NAME_DESCRIPTION, "You already have a Web app on the Internet and want to reach customers with a new, targeted experience on Windows 8.  Come get practical guidance and best practices on how to reuse your Web assets.");
			values.put(CarnnectingContract.Event.COLUMN_NAME_CATEGORY_ID, 10);
			db.insert(CarnnectingContract.Event.TABLE_NAME, null, values);

			// Event 2 will be one day later than event 1 and so on so forth
			cal.add(Calendar.HOUR, 23);
			d = cal.getTime();
			
			// Create the categories that the user subscribed to
			values = new ContentValues();
			values.put(CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID, 1);
			values.put(CarnnectingContract.Subscribe.COLUMN_NAME_CATEGORY_ID, 1);
			db.insert(CarnnectingContract.Subscribe.TABLE_NAME, null, values);
			
			values = new ContentValues();
			values.put(CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID, 1);
			values.put(CarnnectingContract.Subscribe.COLUMN_NAME_CATEGORY_ID, 2);
			db.insert(CarnnectingContract.Subscribe.TABLE_NAME, null, values);
			
			values = new ContentValues();
			values.put(CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID, 1);
			values.put(CarnnectingContract.Subscribe.COLUMN_NAME_CATEGORY_ID, 10);
			db.insert(CarnnectingContract.Subscribe.TABLE_NAME, null, values);
			
			values = new ContentValues();
			values.put(CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID, 1);
			values.put(CarnnectingContract.Subscribe.COLUMN_NAME_CATEGORY_ID, 4);
			db.insert(CarnnectingContract.Subscribe.TABLE_NAME, null, values);
			
			values = new ContentValues();
			values.put(CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID, 1);
			values.put(CarnnectingContract.Subscribe.COLUMN_NAME_CATEGORY_ID, 8);
			db.insert(CarnnectingContract.Subscribe.TABLE_NAME, null, values);
			
			values = new ContentValues();
			values.put(CarnnectingContract.Subscribe.COLUMN_NAME_USER_ID, 1);
			values.put(CarnnectingContract.Subscribe.COLUMN_NAME_CATEGORY_ID, 7);
			db.insert(CarnnectingContract.Subscribe.TABLE_NAME, null, values);
		} catch (SQLException e) {
			Log.e("ERROR", e.getStackTrace().toString());
		} 
		
	}
}
