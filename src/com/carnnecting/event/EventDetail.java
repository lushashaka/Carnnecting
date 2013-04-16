package com.carnnecting.event;


import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cmu.carnnecting.R;

import java.util.*;

public class EventDetail extends Activity {

	TextView demoTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		demoTextView = (TextView) findViewById(R.id.demoTextView);
		Intent intent  = getIntent();
		int eventId = -1;
		if (intent != null && intent.getExtras() != null) {
			eventId = intent.getExtras().getInt("eventId");
		}
		
		demoTextView.setText("This page will contain the detail of this event with eventId #"+eventId);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.carnnecting_main, menu);
	    return true;
	}

}
