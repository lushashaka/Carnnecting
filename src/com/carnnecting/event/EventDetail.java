
package com.carnnecting.event;


import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutionException;

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
	private GoogleMap map;
	private Marker hamburg;
	// static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	

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
					Log.e("INFO", "There is a image");
				} else {
					Log.e("INFO", "No image found");
				}
				
				// Event has been loaded
				Log.e("INFO", event.toString());

				// Insert into read event table
				if (readEventDao.createReadEvent(userId, eventId)) {
					Log.e("INFO", "EventDetail: added read event id:"+eventId);
				} else {
					Log.e("ERROR", "EventDetail: cannot add read event id:"+eventId);
				}
				
								
				// Get latitude, longitude
				
				// FIXME: formattedAddress
				LatiLongi latilongi = null;
				try {
					latilongi = new GecodingAsyncTask().execute(event.getLocation().trim().replace(' ', '+')).get();
					// Log.e("INFO", latilongi.latitude);
					// Log.e("INFI", latilongi.longitude);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.e("ERROR", e.toString());
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					Log.e("ERROR", e.toString());
				}
				if (latilongi.latitude != null && latilongi.longitude != null) {

					try {
						map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
						LatLng eventLatLong = new LatLng(Double.parseDouble(latilongi.latitude), 
								Double.parseDouble(latilongi.longitude));
						hamburg = map.addMarker(new MarkerOptions().position(eventLatLong)
							.title(event.getSubject())
							.snippet("Come to Join US!")
							);
						// Move the camera instantly to hamburg with a zoom of 15.
						map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLatLong, 15));
						// Zoom in, animating the camera.
						map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
						/*
						map.setOnMarkerClickListener(new OnMarkerClickListener(){

							@Override
							public boolean onMarkerClick(Marker arg0) {
								if (arg0 == hamburg) { // Should always be true
									Log.e("INFO", "Clicked");
								}
								return true;
							}
							
						});
						*/
					} catch (Exception e) {
						Log.e("ERROR", e.toString());
					}
				} else {
					// FIXME: TODO: Hide the map if no location?
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
	        case R.id.favorites:
	        	intent = new Intent(this, Favorites.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	intent.putExtra("userId", userId);
				startActivity(intent);
				return true;
	        case R.id.create_event:
	        	intent = new Intent(this, CreateEvent.class);
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
	
	private class GecodingAsyncTask extends AsyncTask<String, Void, LatiLongi> {

		@Override
		protected LatiLongi doInBackground(String... formattedAddress) {
			String latitude = null;
			String longitude = null;
			
			try {
				String charset = "UTF-8";
				String url = "https://maps.googleapis.com/maps/api/geocode/xml";
				// String address = event.getLocation()
				// FIXME: replace this with the formattedAddress
				// String address = "1600+Amphitheatre+Parkway,+Mountain+View,+CA";
				String address = formattedAddress[0];
				
				String sensor = "false";
				String paraList = "";
			
				paraList = String.format("address=%s&sensor=%s", URLEncoder.encode(address, charset),
					URLEncoder.encode(sensor, charset));
				String wholeUrl = url +"?"+paraList;
				URLConnection connection = new URL(wholeUrl).openConnection();
				connection.setRequestProperty("Accept-Charset", charset);
				InputStream respStream = connection.getInputStream();
				String output = "";
				BufferedReader br = new BufferedReader(new InputStreamReader(respStream));
				String line;
				while ((line = br.readLine()) != null) {
					output += (line.trim());
				}
				// Log.e("INFO", output);
				int idx = 0;
				if ((idx = output.indexOf("<location><lat>")) != -1) {
					String temp = output.substring(idx);
					temp = temp.substring(15);
					latitude = temp.substring(0, temp.indexOf('<'));
					temp = temp.substring(temp.indexOf("<lng>")).substring(5);
					longitude = temp.substring(0, temp.indexOf('<'));
					
				}
				
			} catch (Exception e) {
				Log.e("ERROR", e.toString());
				Log.e("ERROR", e.getStackTrace().toString());
			}
			
			return new LatiLongi(latitude, longitude);
		}
		
	}
	
	private class LatiLongi {
		public String latitude = null;
		public String longitude = null;
		
		public LatiLongi(String lati, String longi) {
			latitude = lati;
			longitude = longi;
		}
	}

}
