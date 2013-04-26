package com.carnnecting.category;

import java.util.HashMap;

import com.carnnecting.entities.SubscribeDataSource;
import com.carnnecting.widget.ExpandListAdapter;
import com.cmu.carnnecting.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

public class CategoryDetail extends Activity {
	private int userId;
	private int categoryId;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_detail);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Intent intent  = getIntent();
		
		userId = -1;
		if (intent != null && intent.getExtras() != null) {
			
			userId = intent.getExtras().getInt("userId");
			categoryId = intent.getExtras().getInt("categoryId");
			Log.i("In Category Detail", "userId is "+ userId + " categoryId is " + categoryId);
			
		}
        

		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
	}
	
	
	
	
}