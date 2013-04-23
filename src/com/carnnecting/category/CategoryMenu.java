package com.carnnecting.category;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.carnnecting.home.Home;
import com.carnnecting.util.*;
import com.carnnecting.widget.*;
import com.cmu.carnnecting.R;
import com.carnnecting.entities.*;

import android.database.SQLException;


// FIXME: To-Be-Removed. These are just to create the db and do bulk-populate in the first time. Using ADB shell is also feasible
import android.database.sqlite.SQLiteDatabase;

// FIXME: To-Be-Removed. These are to demo how to use DataSoruce classes
import java.util.ArrayList;



public class CategoryMenu extends Activity {

	private ExpandListAdapter ExpAdapter;
	private ArrayList<ExpandListGroup> ExpListItems;
	private ExpandableListView ExpandList;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_menu);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		ExpandList = (ExpandableListView) findViewById(R.id.categoryListView);
        ExpListItems = SetStandardGroups();
        ExpAdapter = new ExpandListAdapter(CategoryMenu.this, ExpListItems);
        ExpandList.setAdapter(ExpAdapter);

		
	}
	
	public ArrayList<ExpandListGroup> SetStandardGroups() {
    	ArrayList<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
    	ArrayList<ExpandListChild> list2 = new ArrayList<ExpandListChild>();
        ExpandListGroup gru1 = new ExpandListGroup();
        gru1.setName("Comedy");
        ExpandListChild ch1_1 = new ExpandListChild();
        ch1_1.setName("A movie");
        ch1_1.setTag(null);
        list2.add(ch1_1);
        ExpandListChild ch1_2 = new ExpandListChild();
        ch1_2.setName("An other movie");
        ch1_2.setTag(null);
        list2.add(ch1_2);
        ExpandListChild ch1_3 = new ExpandListChild();
        ch1_3.setName("And an other movie");
        ch1_3.setTag(null);
        list2.add(ch1_3);
        gru1.setItems(list2);
        list2 = new ArrayList<ExpandListChild>();
        
        ExpandListGroup gru2 = new ExpandListGroup();
        gru2.setName("Action");
        ExpandListChild ch2_1 = new ExpandListChild();
        ch2_1.setName("A movie");
        ch2_1.setTag(null);
        list2.add(ch2_1);
        ExpandListChild ch2_2 = new ExpandListChild();
        ch2_2.setName("An other movie");
        ch2_2.setTag(null);
        list2.add(ch2_2);
        ExpandListChild ch2_3 = new ExpandListChild();
        ch2_3.setName("And an other movie");
        ch2_3.setTag(null);
        list2.add(ch2_3);
        gru2.setItems(list2);
        list.add(gru1);
        list.add(gru2);
        
        return list;
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
	        	startActivity(intent);
	        	return true;
	        //TODO: add more cases for action bar
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
