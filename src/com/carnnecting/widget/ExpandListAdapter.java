package com.carnnecting.widget;

import java.util.ArrayList;
import java.util.HashMap;

import com.carnnecting.*;
import com.carnnecting.util.*;
import com.cmu.carnnecting.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ExpandListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ExpandListGroup> groups;
	private HashMap<Integer, Boolean> changedSubscribedCatIds;
	
	private static class ViewHolder {
		public CheckBox subscribeCheckBox;
		public TextView subjectTextView;
	}
	
	public ExpandListAdapter(Context context, ArrayList<ExpandListGroup> groups) {
		this.context = context;
		this.groups = groups;
	}
	
	public ExpandListAdapter(Context context, ArrayList<ExpandListGroup> groups, HashMap<Integer, Boolean> changedSubscribedCatIds) {
		this.context = context;
		this.groups = groups;
		this.changedSubscribedCatIds = changedSubscribedCatIds;
	}
	
	public void addItem(ExpandListChild item, ExpandListGroup group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		ArrayList<ExpandListChild> ch = groups.get(index).getItems();
		ch.add(item);
		groups.get(index).setItems(ch);
	}
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		ArrayList<ExpandListChild> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		final ExpandListChild child = (ExpandListChild) getChild(groupPosition, childPosition);
		ViewHolder holder = null;
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.expandlist_child_item, null);
			holder = new ViewHolder();
			holder.subscribeCheckBox = (CheckBox)view.findViewById(R.id.checkbox_subscribe);
			holder.subjectTextView = (TextView) view.findViewById(R.id.tvChild);
			view.setTag(holder);
		}
		else{
			holder = (ViewHolder)view.getTag();
		}
		TextView tv = (TextView) view.findViewById(R.id.tvChild);
		tv.setText(child.getName().toString());
		tv.setTag(child.getId());
		// TODO Auto-generated method stub
		
		holder.subscribeCheckBox.setOnCheckedChangeListener(null);
		holder.subscribeCheckBox.setChecked(false);
		holder.subscribeCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton button,
					boolean isChecked) {
				Log.i("child name", child.getName());
				Log.i("child id", "" + child.getId());
				int categoryId = child.getId();
					if (!changedSubscribedCatIds.isEmpty()){
						if (changedSubscribedCatIds.containsKey(categoryId)) {
						// Toggle a boolean even number of times changes nothing
							changedSubscribedCatIds.remove(categoryId);
						}
						else {
							changedSubscribedCatIds.put(categoryId, isChecked);
						}
					}
					else{
						changedSubscribedCatIds.put(categoryId, isChecked);
					}
			
			}
			
		});
		return view;
	}

	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		ArrayList<ExpandListChild> chList = groups.get(groupPosition).getItems();

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
		ExpandListGroup group = (ExpandListGroup) getGroup(groupPosition);
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

}