package com.carnnecting.category;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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
	private CategoryDataSource categoryDAO;
	
	private int	userId;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_menu);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		ExpandList = (ExpandableListView) findViewById(R.id.categoryListView);
		Intent intent  = getIntent();
		userId = -1;
		if (intent != null && intent.getExtras() != null) {
			userId = intent.getExtras().getInt("USERID");
		}
        ExpListItems = SetStandardGroups(userId);
        ExpAdapter = new ExpandListAdapter(CategoryMenu.this, ExpListItems);
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
		for (int i = 0; i < subscribedCategories.size(); i++) {
			Category category = subscribedCategories.get(i);
			ExpandListChild childCat = new ExpandListChild();
			childCat.setName(category.getName());
			childCat.setTag(null);
			childList.add(childCat);
		}
        
        myCats.setItems(childList);
        
        ExpandListGroup otherCats = new ExpandListGroup();
        otherCats.setName("Other Categories");
        childList = new ArrayList<ExpandListChild>();
        ExpandListChild ch2_1 = new ExpandListChild();
        ch2_1.setName("A movie");
        ch2_1.setTag(null);
        childList.add(ch2_1);
        ExpandListChild ch2_2 = new ExpandListChild();
        ch2_2.setName("An other movie");
        ch2_2.setTag(null);
        childList.add(ch2_2);
        ExpandListChild ch2_3 = new ExpandListChild();
        ch2_3.setName("And an other movie");
        ch2_3.setTag(null);
        childList.add(ch2_3);
        otherCats.setItems(childList);
        
        ExpandListGroup allCats = new ExpandListGroup();
        childList = new ArrayList<ExpandListChild>();
        allCats.setName("All Categories");
        ArrayList<Category> allCategories = categoryDAO.getAllCategories();
		for (int i = 0; i < allCategories.size(); i++) {
			Category category = allCategories.get(i);
			ExpandListChild childCat = new ExpandListChild();
			childCat.setName(category.getName());
			childCat.setTag(null);
			childList.add(childCat);
		}
		allCats.setItems(childList);
		
        parentList.add(myCats);
        parentList.add(otherCats);
        parentList.add(allCats);
        
        return parentList;
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
