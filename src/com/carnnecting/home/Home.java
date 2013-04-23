package com.carnnecting.home;



import android.app.ListActivity;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.text.Editable;
import android.text.TextWatcher;


import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carnnecting.entities.CarnnectingContract;
import com.carnnecting.entities.Category;
import com.carnnecting.entities.CategoryDataSource;
import com.carnnecting.entities.EventDataSource;
import com.carnnecting.entities.FavoriteDataSource;
import com.carnnecting.entities.HomeItemModel;
import com.carnnecting.entities.RSVPDataSource;
import com.carnnecting.event.EventDetail;
import com.cmu.carnnecting.R;

import java.util.*;

public class Home extends ListActivity {
	
	private Long							lastDatabaseLoadTimestamp = null;
	private HomeAdapter 					homeAdapter;
	
	private ArrayList<HomeItemModel>		homeItems;
	private HashMap<Integer, Boolean>		changedFavoriteEventIds;
	private HashMap<Integer, Boolean>		changedRSVPEventIds;
	
	// DAOs
	private EventDataSource				eventDao;
	private CategoryDataSource			categoryDao;
	private	FavoriteDataSource			favoriteDao;
	private RSVPDataSource				RSVPDao;
	
	// FIXME: this should be passed using intent
	private int							userId = 1;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		eventDao = new EventDataSource(this);
		eventDao.open();
		categoryDao = new CategoryDataSource(this);
		categoryDao.open();
		favoriteDao = new FavoriteDataSource(this);
		favoriteDao.open();
		RSVPDao = new RSVPDataSource(this);
		RSVPDao.open();
		
		changedFavoriteEventIds = new HashMap<Integer, Boolean>();
		changedRSVPEventIds	= new HashMap<Integer, Boolean>();
		Log.e("INFO", "Before entering loadHomeItems()");
		loadHomeItems();
		
		homeAdapter = new HomeAdapter();
		setListAdapter(homeAdapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		loadHomeItems();
	}
	
	private void loadHomeItems() {
		/* Always sync to newest in databases since last updates */
		if (lastDatabaseLoadTimestamp == null || 
			lastDatabaseLoadTimestamp < CarnnectingContract.getDatabaseLastUpdateTimestamp()) 
		{
			lastDatabaseLoadTimestamp = new Date().getTime();
			
			homeItems = new ArrayList<HomeItemModel>();
			
			//
			// FIXME: should we discard those past-due events?
			//
		
			
			// Get events general
			Log.e("INFO", "Before get categoryIds");
			ArrayList<Integer> categoryIds = categoryDao.getSubscribedCategoryIdsByUserId(userId);
			Log.e("INFO", "Before get event generals");
			eventDao.getHomeItemModelsByCategoryIds(categoryIds, homeItems);
			
			
			Log.e("INFO", "Before get favorites");
			// Get favorites
			ArrayList<Integer> eventIds = favoriteDao.getFavoriteEventIdsByUserId(userId);
			HashSet<Integer> set = new HashSet<Integer>();
			for (int i = 0; i < eventIds.size(); i++)
				set.add(eventIds.get(i));
			for (int i = 0; i < homeItems.size(); i++) {
				if (set.contains(homeItems.get(i).getEventId())) {
					homeItems.get(i).setFavorite(true);
				}
			}
			
			Log.e("INFO", "Before get RSVPs");
			// Get RSVPs
			set.clear();
			eventIds.clear();
			eventIds = RSVPDao.getRSVPEventIdsByUserId(userId);
			for (int i = 0; i < eventIds.size(); i++)
				set.add(eventIds.get(i));
			for (int i = 0; i < homeItems.size(); i++) {
				if (set.contains(homeItems.get(i).getEventId())) {
					homeItems.get(i).setRSVP(true);
				}
			}
			
			/*
			for (int i = 0; i < homeItems.size(); i++) {
				Log.e("INFO", homeItems.get(i).toString());
			}
			*/
			
			// Sort the Events by their startDates
			Collections.sort(homeItems, new Comparator<HomeItemModel>(){

				@Override
				public int compare(HomeItemModel arg0, HomeItemModel arg1) {
					try {
						Date date0 = HomeItemModel.dateOnlyFormat.parse(arg0.getStartDate());
						Date date1 = HomeItemModel.dateOnlyFormat.parse(arg1.getStartDate());
						return date0.compareTo(date1);
					} catch (Exception e) {
						Log.e("ERROR", e.getStackTrace().toString());
					}
					return 0;
				}
			});
		}	
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		// FIXME: Maybe we could move the db commit code to onStop()? 
		Log.e("INFO", "in onPause");
		Log.e("INFO", "favChanged size = "+changedFavoriteEventIds.size());
		Log.e("INFO", "RSVPChanged size = "+changedRSVPEventIds.size());
		
		for (int eventId : changedFavoriteEventIds.keySet()){
			boolean isFavoriteNow = changedFavoriteEventIds.get(eventId);
			if (isFavoriteNow) {
				if (!favoriteDao.createFavorite(userId, eventId)) {
					Log.e("ERROR", "Error creating a new favorite entry");
				}
			} else {
				if(!favoriteDao.deleteFavorite(userId, eventId)) {
					Log.e("ERROR", "Error deleting a new favorite entry");
				}
			}
		}
		
		for (int eventId : changedRSVPEventIds.keySet()){
			boolean isRSVPNow = changedRSVPEventIds.get(eventId);
			if (isRSVPNow) {
				if (!RSVPDao.createRSVP(userId, eventId)) {
					Log.e("ERROR", "Error creating a new RSVP entry");
				}
			} else {
				if(!RSVPDao.deleteRSVP(userId, eventId)) {
					Log.e("ERROR", "Error deleting a new RSVP entry");
				}
			}
		}
		
		
		changedFavoriteEventIds.clear();
		changedRSVPEventIds.clear();
	}
	
	
	
	protected void onSaveInstacnceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// Toast.makeText(Home.this, homeItems.get(position).getSubject(), Toast.LENGTH_SHORT).show();
		Intent eventDetailIntent = new Intent(v.getContext(), EventDetail.class);
		// FIXME: the userId variable is now hardcoded
		eventDetailIntent.putExtra("userId", userId);
		eventDetailIntent.putExtra("eventId", homeItems.get(position).getEventId());
		
		startActivity(eventDetailIntent);
	}
	
	private static class HomeItemHolder {
		public CheckBox favoriteCheckBox;
		public TextView subjectTextView;
		public CheckBox RSVPCheckBox;
	}
	
	private class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return homeItems.size();
		}

		@Override
		public HomeItemModel getItem(int position) {
			return homeItems.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HomeItemHolder holder = null;
			
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.home_item, parent, false);
				holder = new HomeItemHolder();
				holder.favoriteCheckBox = (CheckBox) convertView.findViewById(R.id.homeFavoriteCheckBox);
				holder.subjectTextView = (TextView) convertView.findViewById(R.id.homeSubjectTextView);
				holder.RSVPCheckBox = (CheckBox) convertView.findViewById(R.id.homeRSVPCheckBox);
				convertView.setTag(holder);
			} else {
				holder = (HomeItemHolder)convertView.getTag();
			}
				
			holder.favoriteCheckBox.setOnCheckedChangeListener(null);
			holder.favoriteCheckBox.setChecked(homeItems.get(position).isFavorite());
			holder.favoriteCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				public void onCheckedChanged(CompoundButton arg0,
						boolean arg1) {
					final int position = getListView().getPositionForView(arg0);
					if (position != ListView.INVALID_POSITION) {
						homeItems.get(position).setFavorite(arg1);
						int eventId = homeItems.get(position).getEventId();
						
						if (changedFavoriteEventIds.containsKey(eventId)) {
							// Toggle a boolean even number of times changes nothing
							changedFavoriteEventIds.remove(eventId);
						} else {
							changedFavoriteEventIds.put(eventId, arg1);
						}
					}
				}			
			});
			
			holder.subjectTextView.setText(homeItems.get(position).getSubject());
			holder.subjectTextView.setTypeface(Typeface.DEFAULT_BOLD, 0);
			
			holder.RSVPCheckBox.setOnCheckedChangeListener(null);
			holder.RSVPCheckBox.setChecked(homeItems.get(position).isRSVP());
			holder.RSVPCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				public void onCheckedChanged(CompoundButton arg0,
						boolean arg1) {
					final int position = getListView().getPositionForView(arg0);
					if (position != ListView.INVALID_POSITION) {
						homeItems.get(position).setRSVP(arg1);
						int eventId = homeItems.get(position).getEventId();
						if (changedRSVPEventIds.containsKey(eventId)) {
							// Toggle a boolean even number of times changes nothing
							changedRSVPEventIds.remove(eventId);
						} else {
							changedRSVPEventIds.put(eventId, arg1);
						}
					}
				}			
			});
			holder.RSVPCheckBox.setText(homeItems.get(position).getStartDate());
			return convertView;
		}
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.carnnecting_main, menu);
	    return true;
	}

}