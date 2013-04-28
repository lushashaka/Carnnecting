package com.carnnecting.widget;

import java.util.ArrayList;
import java.util.HashMap;

import com.carnnecting.*;
import com.carnnecting.category.CategoryDetail;
import com.carnnecting.entities.HomeItemModel;
import com.carnnecting.util.*;
import com.cmu.carnnecting.R;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class MyEventsListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ExpandEventListGroup> groups;
	private HashMap<Integer, Boolean> changedFavIds;
	private HashMap<Integer, Boolean> changedRSVPIds;
	private int userId;
	
	private static class ViewHolder {
		public CheckBox favoriteCheckBox;
		public TextView subjectTextView;
		public CheckBox RSVPCheckBox;
	}
	
	public MyEventsListAdapter(Context context, ArrayList<ExpandEventListGroup> groups) {
		this.context = context;
		this.groups = groups;
	}
	
	public MyEventsListAdapter(Context context, ArrayList<ExpandEventListGroup> groups, 
								HashMap<Integer, Boolean> changedRSVPIds, int userId) {
		this.context = context;
		this.groups = groups;
		this.changedFavIds = changedFavIds;
		this.changedRSVPIds = changedRSVPIds;
		this.userId = userId;
	}
	
	public void addItem(HomeItemModel item, ExpandEventListGroup group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		ArrayList<HomeItemModel> ch = groups.get(index).getItems();
		ch.add(item);
		groups.get(index).setItems(ch);
	}
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		ArrayList<HomeItemModel> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		final HomeItemModel child = (HomeItemModel) getChild(groupPosition, childPosition);
		ViewHolder holder = null;
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.home_item, null);
			holder = new ViewHolder();
			holder.favoriteCheckBox = (CheckBox)view.findViewById(R.id.homeFavoriteCheckBox);
			holder.subjectTextView = (TextView) view.findViewById(R.id.homeSubjectTextView);
			holder.RSVPCheckBox = (CheckBox)view.findViewById(R.id.homeRSVPCheckBox);
			view.setTag(holder);
		}
		else{
			holder = (ViewHolder)view.getTag();
		}
		TextView tv = (TextView) view.findViewById(R.id.homeSubjectTextView);
		tv.setText(child.getSubject());
		tv.setTag(child.getEventId());
		
		// TODO Auto-generated method stub
		//TODO: add favorite checkbox for events
		/*holder.favoriteCheckBox.setOnCheckedChangeListener(null);
		holder.favoriteCheckBox.setChecked(child.isSubscribed());
		holder.favoriteCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton button,
					boolean isChecked) {
				Log.i("child name", child.getName());
				Log.i("child id", "" + child.getId());
				int categoryId = child.getId();
				groups.get(groupPosition).getItems().get(childPosition).setSubscribed(isChecked);
						if (changedSubscribedCatIds.containsKey(categoryId)) {
						// Toggle a boolean even number of times changes nothing
							changedSubscribedCatIds.remove(categoryId);
						}
						else {
							changedSubscribedCatIds.put(categoryId, isChecked);
						}
			
			}
			
		});*/
		
		
		holder.RSVPCheckBox.setOnCheckedChangeListener(null);
		holder.RSVPCheckBox.setChecked(child.isRSVP());
		holder.RSVPCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton button,
					boolean isChecked) {
				Log.i("child name", child.getSubject());
				Log.i("child id", "" + child.getEventId());
				int eventId = child.getEventId();
				groups.get(groupPosition).getItems().get(childPosition).setRSVP(isChecked);
						if (changedRSVPIds.containsKey(eventId)) {
						// Toggle a boolean even number of times changes nothing
							changedRSVPIds.remove(eventId);
						}
						else {
							changedRSVPIds.put(eventId, isChecked);
						}
			
			}
			
		});

		return view;
	}

	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		ArrayList<HomeItemModel> chList = groups.get(groupPosition).getItems();

		return chList.size();

	}

	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		ExpandEventListGroup group = (ExpandEventListGroup) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.expandlist_group_item, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.tvGroup);
		tv.setText(group.getName());
		// TODO Auto-generated method stub
		return view;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	
	/*public HashMap<Integer, Boolean> getSubscribedCatIds(){
		return changedSubscribedCatIds;
	}*/
	

}