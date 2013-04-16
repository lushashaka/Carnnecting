package com.carnnecting.home;


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

import com.carnnecting.event.EventDetail;
import com.cmu.carnnecting.R;

import java.util.*;

public class Home extends Activity {

	LinearLayout homeScrollLinearLayout;
	ArrayList<TextView> feeds = new ArrayList<TextView>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		homeScrollLinearLayout = (LinearLayout) findViewById(R.id.homeScrollLinearLayout);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// Initialize feeds. This will be done by loading them from the database.
		// Hardcode here for demo purpose only
		for (int i = 0; i < 300; i++) {
			TextView feed = new TextView(this);
			feed.setText("Hello wrold #"+i);
			feed.setGravity(Gravity.CENTER);
			feed.setTextSize(50);
			feed.setId(i); // This is to set to eventId of the corresponding article
			feed.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), EventDetail.class);
					intent.putExtra("eventId", v.getId());
					startActivity(intent);
				}
			});
			
			feeds.add(feed);
			homeScrollLinearLayout.addView(feed);
		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.carnnecting_main, menu);
	    return true;
	}

}
