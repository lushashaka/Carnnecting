package com.carnnecting.category;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.carnnecting.home.Home;
import com.carnnecting.util.*;
import com.carnnecting.widget.*;
import com.cmu.carnnecting.R;
import com.carnnecting.entities.*;

import android.database.SQLException;


// FIXME: To-Be-Removed. These are just to create the db and do bulk-populate in the first time. Using ADB shell is also feasible
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;

// FIXME: To-Be-Removed. These are to demo how to use DataSoruce classes
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;



public class CategoryMenu extends Activity {

	private ExpandListAdapter ExpAdapter;
	private ArrayList<ExpandListGroup> ExpListItems;
	private ExpandableListView ExpandList;
	private CategoryDataSource categoryDAO;
	private SubscribeDataSource subscribeDAO;
	private Long lastDatabaseLoadTimestamp = null;
	
	private HashMap<Integer, Boolean> changedSubscribedCatIds;
	
	private int	userId;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_menu);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		ExpandList = (ExpandableListView) findViewById(R.id.categoryListView);
		changedSubscribedCatIds = new HashMap<Integer, Boolean>();
		Intent intent  = getIntent();
		subscribeDAO = new SubscribeDataSource(this.getApplication());
		subscribeDAO.open();
		userId = -1;
		if (intent != null && intent.getExtras() != null) {
			userId = intent.getExtras().getInt("USERID");
		}
        ExpListItems = SetStandardGroups(userId);
        ExpAdapter = new ExpandListAdapter(CategoryMenu.this, ExpListItems, changedSubscribedCatIds);
        ExpandList.setAdapter(ExpAdapter);

		
	}
	
	public ArrayList<ExpandListGroup> SetStandardGroups(int userId) {
    	ArrayList<ExpandListGroup> parentList = new ArrayList<ExpandListGroup>();
    	ArrayList<ExpandListChild> childList = new ArrayList<ExpandListChild>();
        ExpandListGroup myCats = new ExpandListGroup();
        
        myCats.setName("My Categories");
        
        categoryDAO = new CategoryDataSource(this.getApplication());
		categoryDAO.open();
		ArrayList<Category> subscribedCategories = categoryDAO.getSubscribedCategoriesByUserId(userId);
		ArrayList<Category> allCategories = categoryDAO.getAllCategories();
		
		for (int i = 0; i < subscribedCategories.size(); i++) {
			Category category = subscribedCategories.get(i);
			ExpandListChild childCat = new ExpandListChild();
			childCat.setName(category.getName());
			childCat.setId(category.getId());
			childList.add(childCat);
		}
        
        myCats.setItems(childList);
        
        ExpandListGroup otherCats = new ExpandListGroup();
        otherCats.setName("Other Categories");
        childList = new ArrayList<ExpandListChild>();
        ArrayList<Category> otherCategories = categoryDAO.getOtherCategoriesByUserId(userId);

		for (int i = 0; i < otherCategories.size(); i++) {
			Category category = otherCategories.get(i);
			ExpandListChild childCat = new ExpandListChild();
			childCat.setName(category.getName());
			childCat.setId(category.getId());
			childList.add(childCat);
		}

        otherCats.setItems(childList);
        
        ExpandListGroup allCats = new ExpandListGroup();
        childList = new ArrayList<ExpandListChild>();
        allCats.setName("All Categories");
		for (int i = 0; i < allCategories.size(); i++) {
			Category category = allCategories.get(i);
			ExpandListChild childCat = new ExpandListChild();
			childCat.setName(category.getName());
			childCat.setId(category.getId());
			childList.add(childCat);
		}
		allCats.setItems(childList);
		
        parentList.add(myCats);
        parentList.add(otherCats);
        parentList.add(allCats);
        
        return parentList;
    }
	
	@Override
	public void onResume(){
		super.onResume();
		ExpListItems = SetStandardGroups(userId);
		changedSubscribedCatIds = ExpAdapter.getSubscribedCatIds();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		changedSubscribedCatIds = ExpAdapter.getSubscribedCatIds();
		for (int categoryId : changedSubscribedCatIds.keySet()){
			boolean isSubscribed = changedSubscribedCatIds.get(categoryId);
			if (isSubscribed) {
				if (!subscribeDAO.createSubscribe(userId, categoryId)) {
					Log.e("ERROR", "Error creating a new Subscribe Cat entry");
				}
			} else {
				if(!subscribeDAO.deleteSubscribe(userId, categoryId)) {
					Log.e("ERROR", "Error deleting a new Subscribe Cat entry");
				}
			}
		}
		
		changedSubscribedCatIds.clear();
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
	        	intent.putExtra("USERID", userId);
	        	startActivity(intent);
	        	return true;
	        //TODO: add more cases for action bar
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
