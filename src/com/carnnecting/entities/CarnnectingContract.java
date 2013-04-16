package com.carnnecting.entities;

public class CarnnectingContract {
	// To prevent the contract class from being instantiated
	private CarnnectingContract() {}
	
	// Category table
	public static abstract class Category {
		public static final String TABLE_NAME 						= "category";
		public static final String COLUMN_NAME_ID					= "id";
		public static final String COLUMN_NAME_NAME 				= "name";
		public static final String COLUMN_NAME_DESCRIPTION 			= "description";
		public static final String COLUMN_NAME_PARENT_CAT_ID		= "parent_category_id";
	}
	
	// Event table
	public static abstract class Event {
		public static final String TABLE_NAME						= "event";
		public static final String COLUMN_NAME_ID					= "id";
		public static final String COLUMN_NAME_CATEGORY_ID			= "category_id";
		public static final String COLUMN_NAME_SUBJECT				= "subject";
		public static final String COLUMN_NAME_START_TIME			= "start_time";
		public static final String COLUMN_NAME_END_TIME				= "end_time";
		public static final String COLUMN_NAME_DESCRIPTION			= "description";
	}
	
	// Favorite table
	public static abstract class Favorite {
		public static final String TABLE_NAME						= "favroite";
		public static final String COLUMN_NAME_USER_ID				= "user_id";
		public static final String COLUMN_NAME_EVENT_ID				= "event_id";
	}
	
	// RSVP table
	public static abstract class RSVP {
		public static final String TABLE_NAME						= "rsvp";
		public static final String COLUMN_NAME_USER_ID				= "user_id";
		public static final String COLUMN_NAME_EVENT_ID				= "event_id";
	}
	
	// Subscribe table
	public static abstract class Subscribe {
		public static final String TABLE_NAME						= "subscribe";
		public static final String COLUMN_NAME_USER_ID				= "user_id";
		public static final String COLUMN_NAME_CATEGORY_ID			= "category_id";
	}
		
	// User table
	public static abstract class User {
		public static final String TABLE_NAME						= "user";
		public static final String COLUMN_NAME_ID					= "id";
		public static final String COLUMN_NAME_FB_LOGIN				= "fb_login";
	}
}
