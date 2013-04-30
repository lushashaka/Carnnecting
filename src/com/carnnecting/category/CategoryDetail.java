package com.carnnecting.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import com.carnnecting.account.Logout;
import com.carnnecting.entities.CarnnectingContract;
import com.carnnecting.entities.EventDataSource;
import com.carnnecting.entities.FavoriteDataSource;
import com.carnnecting.entities.HomeItemModel;
import com.carnnecting.entities.RSVPDataSource;
import com.carnnecting.entities.ReadEventDataSource;
import com.carnnecting.entities.SubscribeDataSource;
import com.carnnecting.event.CreateEvent;
import com.carnnecting.event.EventDetail;
import com.carnnecting.event.Favorites;
import com.carnnecting.event.MyEvents;
import com.carnnecting.home.Home;
import com.carnnecting.widget.ExpandListAdapter;
import com.cmu.carnnecting.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CategoryDetail extends ListActivity {
	private Long lastDatabaseLoadTimestamp = null;
	private CategoryDetailAdapter categoryDetailAdapter;
	
	private ArrayList<HomeItemModel> eventItems;
	private HashMap<Integer, Boolean> changedFavoriteEventIds;
	private HashMap<Integer, Boolean> changedRSVPEventIds;
	private int userId;
	private int categoryId;
	private EventDataSource eventDAO;
	private FavoriteDataSource favDAO;
	private RSVPDataSource RSVPDAO;
	private ReadEventDataSource	readEventDao;
	private static HashSet<Integer> readEventIds;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_detail);


		
		Button ficon1 = (Button) findViewById(R.id.ficon1);
		Button ficon2 = (Button) findViewById(R.id.ficon2);
		
		ficon1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CategoryDetail.this, CreateEvent.class);
				startActivity(intent);
			}
		});
		
		ficon2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CategoryDetail.this, Favorites.class);
				intent.putExtra("userId", userId);
				startActivity(intent);
			}
		});
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
		Intent intent  = getIntent();
		eventDAO = new EventDataSource(this.getApplication());
		eventDAO.open();
		favDAO = new FavoriteDataSource(this.getApplication());
		favDAO.open();
		RSVPDAO = new RSVPDataSource(this.getApplication());
		RSVPDAO.open();
		readEventDao = new ReadEventDataSource(this.getApplication());
		readEventDao.open();
		changedFavoriteEventIds = new HashMap<Integer, Boolean>();
		changedRSVPEventIds	= new HashMap<Integer, Boolean>();
		userId = -1;
		categoryId = -1;
		
				
		if (intent != null && intent.getExtras() != null) {
			
			userId = intent.getExtras().getInt("userId");
			categoryId = intent.getExtras().getInt("categoryId");
			Log.i("In Category Detail", "userId is "+ userId + " categoryId is " + categoryId);
			
		}
		loadEventItems();
		categoryDetailAdapter = new CategoryDetailAdapter();
		setListAdapter(categoryDetailAdapter);
		
	}
	
	private static class EventItemHolder {
		public CheckBox favoriteCheckBox;
		public TextView subjectTextView;
		public CheckBox RSVPCheckBox;
	}
	
	private class CategoryDetailAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return eventItems.size();
		}

		@Override
		public HomeItemModel getItem(int position) {
			return eventItems.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			EventItemHolder holder = null;
			
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.home_item, parent, false);
				holder = new EventItemHolder();
				holder.favoriteCheckBox = (CheckBox) convertView.findViewById(R.id.homeFavoriteCheckBox);
				holder.subjectTextView = (TextView) convertView.findViewById(R.id.homeSubjectTextView);
				holder.RSVPCheckBox = (CheckBox) convertView.findViewById(R.id.homeRSVPCheckBox);
				convertView.setTag(holder);
			} else {
				holder = (EventItemHolder)convertView.getTag();
			}
				
			holder.favoriteCheckBox.setOnCheckedChangeListener(null);
			holder.favoriteCheckBox.setChecked(eventItems.get(position).isFavorite());
			holder.favoriteCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				public void onCheckedChanged(CompoundButton arg0,
						boolean arg1) {
					final int position = getListView().getPositionForView(arg0);
					if (position != ListView.INVALID_POSITION) {
						eventItems.get(position).setFavorite(arg1);
						int eventId = eventItems.get(position).getEventId();
						Log.i("INFO_CAT_DETAIL", "current eventID faved " + eventId);
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
			
			holder.subjectTextView.setText(eventItems.get(position).getSubject());
			holder.subjectTextView.setTypeface(Typeface.DEFAULT_BOLD, 0);

			if (!readEventIds.contains(eventItems.get(position).getEventId()))
				holder.subjectTextView.setTextColor(Color.WHITE);
			else
				holder.subjectTextView.setTextColor(Color.GRAY);
			
			holder.RSVPCheckBox.setOnCheckedChangeListener(null);
			holder.RSVPCheckBox.setChecked(eventItems.get(position).isRSVP());
			holder.RSVPCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				public void onCheckedChanged(CompoundButton arg0,
						boolean arg1) {
					final int position = getListView().getPositionForView(arg0);
					if (position != ListView.INVALID_POSITION) {
						eventItems.get(position).setRSVP(arg1);
						int eventId = eventItems.get(position).getEventId();
						if (changedRSVPEventIds.containsKey(eventId)) {
							// Toggle a boolean even number of times changes nothing
							changedRSVPEventIds.remove(eventId);
						} else {
							changedRSVPEventIds.put(eventId, arg1);
						}
					}
				}			
			});
			holder.RSVPCheckBox.setText(eventItems.get(position).getStartDate());
			return convertView;
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		loadEventItems();
		categoryDetailAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		// FIXME: Maybe we could move the db commit code to onStop()? 
		Log.e("INFO", "in onPause");
		Log.e("INFO_CAT_DETAIL", "favChanged size = "+changedFavoriteEventIds.size());
		Log.e("INFO_CAT_DETAIL", "RSVPChanged size = "+changedRSVPEventIds.size());
		
		for (int eventId : changedFavoriteEventIds.keySet()){
			boolean isFavoriteNow = changedFavoriteEventIds.get(eventId);
			if (isFavoriteNow) {
				if (!favDAO.createFavorite(userId, eventId)) {
					Log.e("ERROR", "Error creating a new favorite entry");
				}
			} else {
				if(!favDAO.deleteFavorite(userId, eventId)) {
					Log.e("ERROR", "Error deleting a new favorite entry");
				}
			}
		}
		
		for (int eventId : changedRSVPEventIds.keySet()){
			boolean isRSVPNow = changedRSVPEventIds.get(eventId);
			if (isRSVPNow) {
				if (!RSVPDAO.createRSVP(userId, eventId)) {
					Log.e("ERROR", "Error creating a new RSVP entry");
				}
			} else {
				if(!RSVPDAO.deleteRSVP(userId, eventId)) {
					Log.e("ERROR", "Error deleting a new RSVP entry");
				}
			}
		}
		
		
		changedFavoriteEventIds.clear();
		changedRSVPEventIds.clear();
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent eventDetailIntent = new Intent(v.getContext(), EventDetail.class);
		// FIXME: the userId variable is now hardcoded
		eventDetailIntent.putExtra("userId", userId);
		eventDetailIntent.putExtra("eventId", eventItems.get(position).getEventId());
		
		startActivity(eventDetailIntent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		intent = new Intent(this, CategoryMenu.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	intent.putExtra("userId", userId);
	        	startActivity(intent);
	        	return true;
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
	        case R.id.logout:
	        	System.out.println("***LOGOUT***");
	        	Logout logout = new Logout();
	        	logout.FBLogout();
	        	finish();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.carnnecting_main, menu);
	    return true;
	}
	
	private void loadEventItems() {
		/* Always sync to newest in databases since last updates */
		
		/*Log.e("INFO", "Entering loadHomeItems()");
		Log.e("INFO", "lastDatabaseLoadTimeStamp="+lastDatabaseLoadTimestamp);
		Log.e("INFO", "databaseLastUpdateTimestamp="+CarnnectingContract.getDatabaseLastUpdateTimestamp());*/
		if (lastDatabaseLoadTimestamp == null || 
			lastDatabaseLoadTimestamp < CarnnectingContract.getDatabaseLastUpdateTimestamp()) 
		{
			lastDatabaseLoadTimestamp = new Date().getTime();
			
			eventItems = new ArrayList<HomeItemModel>();
			
			//
			// FIXME: should we discard those past-due events?
			//
		
			
			// Get events general
			Log.e("INFO", "Before get event generals");
			eventDAO.getEventsByCategoryIds(categoryId, eventItems);
			
			
			Log.e("INFO_CAT_DETAIL", "Before get favorites");
			// Get favorites
			ArrayList<Integer> eventIds = favDAO.getFavoriteEventIdsByCatId(userId, categoryId);
			Log.e("INFO_CAT_DETAIL", ""+eventIds.size());
			HashSet<Integer> set = new HashSet<Integer>();
			for (int i = 0; i < eventIds.size(); i++)
				set.add(eventIds.get(i));
			for (int i = 0; i < eventItems.size(); i++) {
				if (set.contains(eventItems.get(i).getEventId())) {
					eventItems.get(i).setFavorite(true);
				}
			}
			
			Log.e("INFO", "Before get RSVPs");
			// Get RSVPs
			set.clear();
			eventIds.clear();
			eventIds = RSVPDAO.getRSVPEventIdsByCatId(userId, categoryId);
			for (int i = 0; i < eventIds.size(); i++) {
				Log.e("INFO", "RSVPed eventIds = "+eventIds.get(i));
				set.add(eventIds.get(i));
			}
			for (int i = 0; i < eventItems.size(); i++) {
				if (set.contains(eventItems.get(i).getEventId())) {
					eventItems.get(i).setRSVP(true);
				}
			}
			
			readEventIds = readEventDao.getReadEventIdsByUserId(userId);
			
			// Sort the Events by their startDates
			Collections.sort(eventItems, new Comparator<HomeItemModel>(){

				@Override
				public int compare(HomeItemModel arg0, HomeItemModel arg1) {
					try {
						Date date0 = HomeItemModel.dateOnlyFormat.parse(arg0.getStartDate());
						Date date1 = HomeItemModel.dateOnlyFormat.parse(arg1.getStartDate());
						return date1.compareTo(date0);
					} catch (Exception e) {
						Log.e("ERROR", e.getStackTrace().toString());
					}
					return 0;
				}
			});
		}	
	}
	
}