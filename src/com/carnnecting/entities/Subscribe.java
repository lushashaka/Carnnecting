package com.carnnecting.entities;

public class Subscribe {
	// Supposely, both userId and categoryId together form the primary key
	int userId;
	int categoryId;
	
	// Constructors
	public Subscribe(int userId, int categoryId) {
		super();
		this.userId = userId;
		this.categoryId = categoryId;
	}
	
	// Getters, Setters
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String toString() {
		return "Subscribe [userId=" + userId + ", categoryId=" + categoryId
				+ "]";
	}
}
