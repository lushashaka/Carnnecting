package com.carnnecting.event;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.carnnecting.home.Home;
import com.carnnecting.util.*;
import com.carnnecting.widget.*;
import com.cmu.carnnecting.R;
import com.carnnecting.entities.*;
import com.carnnecting.event.EventDetail;
import com.carnnecting.category.CategoryDetail;
import com.carnnecting.category.CategoryMenu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;



public class MyEvents extends Activity {

	private MyEventsListAdapter ExpAdapter;
	private ArrayList<ExpandEventListGroup> ExpListItems;
	private ExpandableListView ExpandList;
	private FavoriteDataSource favDAO;
	private RSVPDataSource RSVPDAO;
	private EventDataSource eventDAO;
	private Long lastDatabaseLoadTimestamp = null;
	
	private HashMap<Integer, Boolean> changedRSVPIds;
	private HashMap<Integer, Boolean> changedFavIds;
	
	private int	userId;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_events);
		

		Button ficon1 = (Button) findViewById(R.id.ficon1);
		Button ficon2 = (Button) findViewById(R.id.ficon2);
		
		ficon1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyEvents.this, CreateEvent.class);
				startActivity(intent);
			}
		});
		
		ficon2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyEvents.this, Favorites.class);
				startActivity(intent);
			}
		});
		
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		ExpandList = (ExpandableListView) findViewById(R.id.myEventListView);
		changedRSVPIds = new HashMap<Integer, Boolean>();
		changedFavIds = new HashMap<Integer, Boolean>();
		Intent intent  = getIntent();
		RSVPDAO = new RSVPDataSource(this.getApplication());
		RSVPDAO.open();
		favDAO = new FavoriteDataSource(this.getApplication());
		favDAO.open();
		eventDAO = new EventDataSource(this.getApplication());
		eventDAO.open();
		userId = -1;
		if (intent != null && intent.getExtras() != null) {
			userId = intent.getExtras().getInt("userId");
		}
        ExpListItems = SetStandardGroups(userId);
        ExpAdapter = new MyEventsListAdapter(MyEvents.this, 
        									ExpListItems, 
        									changedRSVPIds, 
        									changedFavIds, 
        									userId);
        ExpandList.setAdapter(ExpAdapter);
        ExpandList.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
            	Intent eventDetailIntent = new Intent(v.getContext(), EventDetail.class);
				Log.i("Calling EventDetail", "inside onChildClick");
				eventDetailIntent.putExtra("eventId", ExpListItems.get(groupPosition).getItems().get(childPosition).getEventId());
				eventDetailIntent.putExtra("userId", userId);
				v.getContext().startActivity(eventDetailIntent);
                return false;

            }
        });

		
	}
	
	public ArrayList<ExpandEventListGroup> SetStandardGroups(int userId) {
    	ArrayList<ExpandEventListGroup> parentList = new ArrayList<ExpandEventListGroup>();
    	ArrayList<HomeItemModel> childList = new ArrayList<HomeItemModel>();
        
    	ExpandEventListGroup todayEvents = new ExpandEventListGroup();
        todayEvents.setName("Today");
        
        eventDAO = new EventDataSource(this.getApplication());
		eventDAO.open();
	    ArrayList<HomeItemModel> todays = eventDAO.getTodayEvents(userId);
	    
	    ArrayList<Integer> eventIds = favDAO.getFavoriteEventIdsByUserId(userId);
		HashSet<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < eventIds.size(); i++)
			set.add(eventIds.get(i));
		
		for (int i = 0; i < todays.size(); i++) {
			HomeItemModel childEvent = todays.get(i);
			childEvent.setRSVP(true);
			if (set.contains(childEvent.getEventId())) {
				childEvent.setFavorite(true);
			}
			childList.add(childEvent);
		}
        todayEvents.setItems(childList);
        
        ExpandEventListGroup tmrwEvents = new ExpandEventListGroup();
        tmrwEvents.setName("Tomorrow");
        childList = new ArrayList<HomeItemModel>();
        ArrayList<HomeItemModel> tmrws = eventDAO.getTmrwEvents(userId);

		for (int i = 0; i < tmrws.size(); i++) {
			HomeItemModel childEvent = tmrws.get(i);
			childEvent.setRSVP(true);
			if (set.contains(childEvent.getEventId())) {
				childEvent.setFavorite(true);
			}
			childList.add(childEvent);
		}

        tmrwEvents.setItems(childList);
        
        ExpandEventListGroup upcomingEvents = new ExpandEventListGroup();
        childList = new ArrayList<HomeItemModel>();
        ArrayList<HomeItemModel> upcomings = eventDAO.getUpcomingEvents(userId);
        upcomingEvents.setName("Upcoming");
		for (int i = 0; i < upcomings.size(); i++) {
			HomeItemModel childEvent = upcomings.get(i);
			childEvent.setRSVP(true);
			if (set.contains(childEvent.getEventId())) {
				childEvent.setFavorite(true);
			}
			childList.add(childEvent);
		}
		upcomingEvents.setItems(childList);
		
		ExpandEventListGroup pastEvents = new ExpandEventListGroup();
        childList = new ArrayList<HomeItemModel>();
        ArrayList<HomeItemModel> pasts = eventDAO.getPastEvents(userId);
        pastEvents.setName("Past");
		for (int i = 0; i < pasts.size(); i++) {
			HomeItemModel childEvent = pasts.get(i);
			childEvent.setRSVP(true);
			if (set.contains(childEvent.getEventId())) {
				childEvent.setFavorite(true);
			}
			childList.add(childEvent);
		}
		pastEvents.setItems(childList);
		
        parentList.add(todayEvents);
        parentList.add(tmrwEvents);
        parentList.add(upcomingEvents);
        parentList.add(pastEvents);
        
        return parentList;
    }
	
	@Override
	public void onResume(){
		super.onResume();
		ExpListItems = SetStandardGroups(userId);
		ExpAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		for (int eventId : changedRSVPIds.keySet()){
			boolean isRSVP = changedRSVPIds.get(eventId);
			if (isRSVP) {
				if (!RSVPDAO.createRSVP(userId, eventId)) {
					Log.e("ERROR", "Error creating a new Subscribe Cat entry");
				}
			} else {
				if(!RSVPDAO.deleteRSVP(userId, eventId)) {
					Log.e("ERROR", "Error deleting a new Subscribe Cat entry");
				}
			}
		}
		
		for (int eventId : changedFavIds.keySet()){
			boolean isRSVP = changedFavIds.get(eventId);
			if (isRSVP) {
				if (!favDAO.createFavorite(userId, eventId)) {
					Log.e("ERROR", "Error creating a new Subscribe Cat entry");
				}
			} else {
				if(!favDAO.deleteFavorite(userId, eventId)) {
					Log.e("ERROR", "Error deleting a new Subscribe Cat entry");
				}
			}
		}
		
		changedRSVPIds.clear();
		changedFavIds.clear();
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
	        //TODO: add more cases for action bar
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
