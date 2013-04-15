package com.carnnecting.entities;

public class Favorite {
	// Supposely, both userId and eventId together form the primary key
	int userId;
	int eventId;
	
	// Constructors
	public Favorite(int userId, int eventId) {
		super();
		this.userId = userId;
		this.eventId = eventId;
	}
	
	// Getters, Setters
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String toString() {
		return "Favorite [userId=" + userId + ", eventId=" + eventId + "]";
	}
}
