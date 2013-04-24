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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		favoriteCheckBox 	= (CheckBox)	findViewById(R.id.eventDetailFavoriteCheckBox);
		subjectTextView 	= (TextView)	findViewById(R.id.eventDetailSubjectTextView);
		RSVPCheckBox 		= (CheckBox) 	findViewById(R.id.eventDetailRSVPCheckBox);
		eventTimeTextView 	= (TextView) 	findViewById(R.id.eventDetailEventTimeTextView);
		descriptionTextView = (TextView)	findViewById(R.id.eventDetailDescriptionTextView);
		
		eventDao = new EventDataSource(this.getApplication());
		eventDao.open();
		favoriteDao = new FavoriteDataSource(this.getApplication());
		favoriteDao.open();
		RSVPDao = new RSVPDataSource(this.getApplication());
		RSVPDao.open();
		
		Intent intent  = getIntent();
		int eventId = -1;
		int userId = -1;
		if (intent != null && intent.getExtras() != null) {
			eventId = intent.getExtras().getInt("eventId");
			userId = intent.getExtras().getInt("userId");
		}
		
		// FIXME: save userId and eventId? onSavedInstance
		
		
		if (userId != -1 && eventId != -1) {
			Event event = eventDao.getAnEventByEventId(eventId);
			Favorite favorite = favoriteDao.getAnFavoriteByUserIdAndEventId(userId, eventId);
			RSVP rsvp = RSVPDao.getAnRSVPByUserIdAndEventId(userId, eventId);
		
			favoriteCheckBox.setChecked(favorite!=null);
			subjectTextView.setText(event.getSubject());
			RSVPCheckBox.setChecked(rsvp!=null);
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
	
	@Override
	protected void onPause() {
		super.onPause();
		// Save the read or RSVP/Favorite Info here
	}

}
