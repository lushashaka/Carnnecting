package com.carnnecting.event;


import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.carnnecting.category.CategoryMenu;
import com.carnnecting.entities.EventDataSource;
import com.carnnecting.entities.RSVPDataSource;
import com.carnnecting.entities.FavoriteDataSource;
import com.carnnecting.entities.Event;
import com.carnnecting.entities.RSVP;
import com.carnnecting.entities.Favorite;
import com.cmu.carnnecting.R;

import java.util.*;

public class EventDetail extends Activity {

private CheckBox favoriteCheckBox;
private TextView subjectTextView;
private CheckBox RSVPCheckBox;
private TextView eventTimeTextView;
private TextView descriptionTextView;
private EventDataSource eventDao;
private FavoriteDataSource favoriteDao;
private RSVPDataSource RSVPDao;

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


Button ficon1 = (Button) findViewById(R.id.ficon1);
Button ficon2 = (Button) findViewById(R.id.ficon2);

ficon1.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(EventDetail.this, CreateEvent.class);
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

if (savedInstanceState != null) {
userId = savedInstanceState.getInt(USER_ID);
eventId = savedInstanceState.getInt(EVENT_ID);
}

ActionBar actionBar = getActionBar();
actionBar.setDisplayHomeAsUpEnabled(true);

favoriteCheckBox = (CheckBox)	findViewById(R.id.eventDetailFavoriteCheckBox);
subjectTextView = (TextView)	findViewById(R.id.eventDetailSubjectTextView);
RSVPCheckBox = (CheckBox) findViewById(R.id.eventDetailRSVPCheckBox);
eventTimeTextView = (TextView) findViewById(R.id.eventDetailEventTimeTextView);
descriptionTextView = (TextView)	findViewById(R.id.eventDetailDescriptionTextView);

eventDao = new EventDataSource(this.getApplication());
eventDao.open();
favoriteDao = new FavoriteDataSource(this.getApplication());
favoriteDao.open();
RSVPDao = new RSVPDataSource(this.getApplication());
RSVPDao.open();

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
descriptionTextView.setText(event.getDescription());

Log.e("INFO", event.toString());

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

}