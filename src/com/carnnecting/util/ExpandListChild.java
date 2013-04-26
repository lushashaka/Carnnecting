package com.carnnecting.util;

public class ExpandListChild {

	private String Name;
	private int Id;
	private boolean isSubscribed;
	
	public String getName() {
		return Name;
	}
	public void setName(String Name) {
		this.Name = Name;
	}
	public int getId() {
		return Id;
	}
	public void setId(int Id) {
		this.Id = Id;
	}
	
	public boolean isSubscribed() {
		return isSubscribed;
	}
	
	public void setSubscribed(boolean isSubscribed) {
		this.isSubscribed = isSubscribed;
	}
	
	@Override
	public String toString() {
		return "CategoryListItem [categoryId=" + Id + ", isSubscribed=" + isSubscribed
				+ ", name=" + Name + "]";
	}
}