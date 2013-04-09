package com.carnnecting.home;

import com.cmu.carnnecting.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Home extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carnnecting_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.carnnecting_main, menu);
		return true;
	}

}
