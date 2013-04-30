
package com.carnnecting.event;


import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.carnnecting.account.Logout;
import com.carnnecting.category.CategoryMenu;
import com.carnnecting.entities.EventDataSource;
import com.carnnecting.entities.ImageDataSource;
import com.carnnecting.entities.RSVPDataSource;
import com.carnnecting.entities.FavoriteDataSource;
import com.carnnecting.entities.Event;
import com.carnnecting.entities.RSVP;
import com.carnnecting.entities.Favorite;
import com.carnnecting.entities.ReadEventDataSource;
import com.carnnecting.home.Home;
import com.carnnecting.ws.FBShare;
import com.cmu.carnnecting.R;

public class EventDetail extends Activity {

	private CheckBox 	favoriteCheckBox;
	private TextView 	subjectTextView;
	private CheckBox 	RSVPCheckBox;
	private TextView 	eventTimeTextView;
	private TextView 	locationTextView;
	private TextView 	hostTextView;
	private TextView 	descriptionTextView;
	private ImageView	eventImageView;
	private Button shareButton;
	
	private FBShare share = new FBShare();
	private String FBmessage;
	
	private EventDataSource eventDao;
	private FavoriteDataSource favoriteDao;
	private RSVPDataSource RSVPDao;
	private ReadEventDataSource readEventDao;
	private ImageDataSource imageDao;

	private static final String USER_ID = "USER_ID";
	private static final String EVENT_ID = "EVENT_ID";
	int userId = -1;
	int eventId = -1;

	boolean isFavoriteChanged = false;
	boolean currentFavoriteValIfChanged = false; // If isFavoriteChanged is false then this value should be ignored

	boolean isRSVPChanged = false;
	boolean currentRSVPValIfChanged = false; // If isRSVPChanged is false then this value should be ignored
	

	@SuppressLint("NewApi")
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_event_detail);

			if (savedInstanceState != null) {
				userId = savedInstanceState.getInt(USER_ID);
				eventId = savedInstanceState.getInt(EVENT_ID);
			}
			
			Button ficon1 = (Button) findViewById(R.id.ficon1);
			Button ficon2 = (Button) findViewById(R.id.ficon2);

			ficon1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(EventDetail.this, CreateEvent.class);
					intent.putExtra("userId", userId);
					startActivity(intent);
				}
			});

			ficon2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(EventDetail.this, Favorites.class);
					startActivity(intent);
				}
			});

			ActionBar actionBar = getActionBar();
			//actionBar.setDisplayHomeAsUpEnabled(true);

			favoriteCheckBox = (CheckBox)	findViewById(R.id.eventDetailFavoriteCheckBox);
			subjectTextView = (TextView)	findViewById(R.id.eventDetailSubjectTextView);
			RSVPCheckBox = (CheckBox) findViewById(R.id.eventDetailRSVPCheckBox);
			eventTimeTextView = (TextView) findViewById(R.id.eventDetailEventTimeTextView);
			locationTextView = (TextView) findViewById(R.id.eventDetailLocationTextView);
			hostTextView = (TextView) findViewById(R.id.eventDetailHostTextView);
			descriptionTextView = (TextView)	findViewById(R.id.eventDetailDescriptionTextView);
			eventImageView = (ImageView) findViewById(R.id.eventImageView);
			
			// Share event on Facebook - Begin
			shareButton = (Button) findViewById(R.id.shareButton);
			
			shareButton.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	Log.i("Event Detail", "FBEvent: " + FBmessage);
		            share.shareEvent(FBmessage);        
		        }
		    });
			// Share event on Facebook - End
			
			// favoriteCheckBox.setText("   ");
			// RSVPCheckBox.setText("   ");

			eventDao = new EventDataSource(this.getApplication());
			eventDao.open();
			favoriteDao = new FavoriteDataSource(this.getApplication());
			favoriteDao.open();
			RSVPDao = new RSVPDataSource(this.getApplication());
			RSVPDao.open();
			readEventDao = new ReadEventDataSource(this.getApplication());
			readEventDao.open();
			imageDao = new ImageDataSource(this.getApplication());
			imageDao.open();

			Intent intent = getIntent();
			if (intent != null && intent.getExtras() != null) {
				eventId = intent.getExtras().getInt("eventId");
				userId = intent.getExtras().getInt("userId");
			}

			// FIXME: save userId and eventId? onSavedInstance

			if (userId != -1 && eventId != -1) {
				Event event = eventDao.getAnEventByEventId(eventId);
				Favorite favorite = favoriteDao.getAnFavoriteByUserIdAndEventId(userId, eventId);
				RSVP rsvp = RSVPDao.getAnRSVPByUserIdAndEventId(userId, eventId);

				// Begin - String construction for "Share event on Facebook"
				FBmessage = "Event: " + event.getSubject();
				FBmessage += "\nHost: " + event.getHost();
				FBmessage += "\nLocation: " + event.getLocation();
				FBmessage += "\nWhen: " + Event.dateFormat.format(event.getStartTime()) + "~" + Event.dateFormat.format(event.getEndTime());
				FBmessage += "\nDescription: " + event.getDescription();
				FBmessage += "\nRSVP to this event by downloading the 'Carnnecting' app!";
				// End - String construction for "Share event on Facebook"
				
				favoriteCheckBox.setOnCheckedChangeListener(null);
				favoriteCheckBox.setChecked(favorite!=null);
				favoriteCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						isFavoriteChanged = !isFavoriteChanged; // Toggle itself
						currentFavoriteValIfChanged = isChecked;

						}

						});

				subjectTextView.setText(event.getSubject());

				RSVPCheckBox.setOnCheckedChangeListener(null);
				RSVPCheckBox.setChecked(rsvp!=null);
				RSVPCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						isRSVPChanged = !isRSVPChanged;
						currentRSVPValIfChanged = isChecked;
						}

						});

				eventTimeTextView.setText(Event.dateFormat.format(event.getStartTime()) + "~" + Event.dateFormat.format(event.getEndTime()));
				locationTextView.setText(event.getLocation());
				hostTextView.setText(event.getHost());
				descriptionTextView.setText(event.getDescription());
				Bitmap bmp = imageDao.getAnImageByEventId(eventId);
				if (bmp != null) {
					eventImageView.setImageBitmap(bmp);
				}
				

				Log.e("INFO", event.toString());

				if (readEventDao.createReadEvent(userId, eventId)) {
					Log.e("INFO", "EventDetail: added read event id:"+eventId);
				} else {
					Log.e("ERROR", "EventDetail: cannot add read event id:"+eventId);
				}

			} else {
				favoriteCheckBox.setEnabled(false);
				RSVPCheckBox.setEnabled(false);
				subjectTextView.setText("Oops..");
				descriptionTextView.setText("Unrecognized userId: "+userId +" or eventId:"+eventId);
			}

		}

	protected void onSaveInstacnceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(USER_ID, userId);
		outState.putInt(EVENT_ID, eventId);
	}

	@Override
		protected void onPause() {
			super.onPause();
			// Save the read or RSVP/Favorite Info here
			if (isFavoriteChanged) {
				if (currentFavoriteValIfChanged == true) {
					if (!favoriteDao.createFavorite(userId, eventId)) {
						Log.e("ERROR", "Error creating a new favorite entry");
					}
				} else {
					if(!favoriteDao.deleteFavorite(userId, eventId)) {
						Log.e("ERROR", "Error deleting a new favorite entry");
					}
				}
			}

			if (isRSVPChanged) {
				if (currentRSVPValIfChanged == true) {
					if (!RSVPDao.createRSVP(userId, eventId)) {
						Log.e("ERROR", "Error creating a new RSVP entry");
					}
				} else {
					if(!RSVPDao.deleteRSVP(userId, eventId)) {
						Log.e("ERROR", "Error deleting a new RSVP entry");
					}
				}
			}
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.carnnecting_main, menu);
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
	        	intent.putExtra("userId", userId);
	        	startActivity(intent);
	        	return true;
	        case R.id.my_events:
	        	intent = new Intent(this, MyEvents.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	intent.putExtra("userId", userId);
	        	startActivity(intent);
	        	return true;
	        	
	        case R.id.logout:
	        	System.out.println("***LOGOUT***");
	        	Logout logout = new Logout();
	        	logout.FBLogout();
	        	finish();
	        	return true;	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
