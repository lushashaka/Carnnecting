package com.carnnecting.home;



import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;import com.carnnecting.account.Logout;import com.carnnecting.category.CategoryMenu;
import com.carnnecting.entities.CarnnectingContract;
import com.carnnecting.event.CreateEvent;
import com.carnnecting.event.Favorites;
import com.carnnecting.event.MyEvents;
import com.carnnecting.entities.CategoryDataSource;
import com.carnnecting.entities.EventDataSource;
import com.carnnecting.entities.FavoriteDataSource;
import com.carnnecting.entities.HomeItemModel;
import com.carnnecting.entities.RSVPDataSource;
import com.carnnecting.entities.ReadEventDataSource;
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
	private ReadEventDataSource			readEventDao;

	// FIXME: this should be passed using intent
	private int							userId;

	private static HashSet<Integer> 	readEventIds;

	@SuppressLint({ "NewApi", "UseSparseArrays" })
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		userId = getIntent().getIntExtra("USERID", 1);
		Log.i("HOME", "Received user id: " + userId);

		eventDao = new EventDataSource(this.getApplication());
		eventDao.open();
		categoryDao = new CategoryDataSource(this.getApplication());
		categoryDao.open();
		favoriteDao = new FavoriteDataSource(this.getApplication());
		favoriteDao.open();
		RSVPDao = new RSVPDataSource(this.getApplication());
		RSVPDao.open();
		readEventDao = new ReadEventDataSource(this.getApplication());
		readEventDao.open();

		changedFavoriteEventIds = new HashMap<Integer, Boolean>();
		changedRSVPEventIds	= new HashMap<Integer, Boolean>();
		
		loadHomeItems();

		homeAdapter = new HomeAdapter();
		setListAdapter(homeAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		loadHomeItems();
		homeAdapter.notifyDataSetChanged();

		Log.i("INFO_HOME", "dumping read event ids");
		Iterator<Integer> it = readEventIds.iterator();
		while(it.hasNext()) {
			int eventId = it.next();
			Log.i("INFO_HOME", "read eventId = "+eventId);
		}
	}

	/*
	@Override
	protected void onStop() {
		super.onStop();
		
		Iterator<Integer> it = changedReadEventIds.iterator();
		while(it.hasNext()) {
			int eventId = it.next();
			if (!readEventDao.createReadEvent(userId, eventId)) {
				Log.e("ERROR", "Error creating a new ReadEvent entry");
			}
		}
		changedReadEventIds.clear();
	}
	*/

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
			Log.i("INFO_HOME", "Before get categoryIds");
			ArrayList<Integer> categoryIds = categoryDao.getSubscribedCategoryIdsByUserId(userId);

			Log.i("INFO_HOME", "Before get event generals");
			eventDao.getHomeItemModelsByCategoryIds(categoryIds, homeItems);


			Log.i("INFO_HOME", "Before get favorites");
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

			Log.i("INFO_HOME", "Before get RSVPs");
			// Get RSVPs
			set.clear();
			eventIds.clear();
			eventIds = RSVPDao.getRSVPEventIdsByUserId(userId);
			for (int i = 0; i < eventIds.size(); i++) {
				Log.i("INFO_HOME", "RSVPed eventIds = "+eventIds.get(i));
				set.add(eventIds.get(i));
			}
			for (int i = 0; i < homeItems.size(); i++) {
				if (set.contains(homeItems.get(i).getEventId())) {
					homeItems.get(i).setRSVP(true);
				}
			}

			// Get read events
			readEventIds = readEventDao.getReadEventIdsByUserId(userId);

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
						return date1.compareTo(date0); // We want to sort DESC, i.e., the newer events on the top
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
		Log.i("INFO_HOME", "in onPause");
		Log.i("INFO_HOME", "favChanged size = "+changedFavoriteEventIds.size());
		Log.i("INFO_HOME", "RSVPChanged size = "+changedRSVPEventIds.size());

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
			holder.favoriteCheckBox.setText("  ");

			holder.subjectTextView.setText(homeItems.get(position).getSubject());
			holder.subjectTextView.setTypeface(Typeface.DEFAULT_BOLD, 0);

			if (!readEventIds.contains(homeItems.get(position).getEventId()))
				holder.subjectTextView.setTextColor(Color.WHITE);
			else
				holder.subjectTextView.setTextColor(Color.GRAY);

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
	        case R.id.favorites:
	        	intent = new Intent(this, Favorites.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	intent.putExtra("userId", userId);
				startActivity(intent);
				return true;
	        case R.id.create_event:
	        	intent = new Intent(this, CreateEvent.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("userId", userId);
				startActivity(intent);
				return true;	        case R.id.logout:	        	Log.i("INFO_HOME", "Logged out");	        	Logout logout = new Logout();	        	logout.FBLogout();	        	finish();	        	return true;		        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
		@Override	public void onBackPressed() {		finish();		return;	}}