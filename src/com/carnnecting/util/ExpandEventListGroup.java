package com.carnnecting.util;

import java.util.ArrayList;

import com.carnnecting.entities.HomeItemModel;

public class ExpandEventListGroup {
 
	private String Name;
	private ArrayList<HomeItemModel> Items;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
	}
	public ArrayList<HomeItemModel> getItems() {
		return Items;
	}
	public void setItems(ArrayList<HomeItemModel> Items) {
		this.Items = Items;
	}
	
	
}