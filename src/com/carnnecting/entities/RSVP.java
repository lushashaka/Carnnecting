package com.carnnecting.entities;

public class RSVP {
	// Supposely, both userId and eventId together form the primary key
	
	private int userId;
	private int eventId;
	
	// Constructors
	public RSVP(int userId, int eventId) {
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
		return "RSVP [userId=" + userId + ", eventId=" + eventId + "]";
	}	
}
