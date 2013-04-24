package com.carnnecting.entities;

import java.text.SimpleDateFormat;

public class HomeItemModel {
	private	int			eventId;
	private boolean 	favorite;
	private int 		categoryId;
	private String		subject;
	private String		startDate;
	private	boolean		RSVP;
	
	public static final SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	// Constructors
	public HomeItemModel() {
		super();
	}
	
	public HomeItemModel(int id, boolean favorite, int categoryId, String subject,
			String startDate, boolean rSVP) {
		super();
		this.eventId = id;
		this.favorite = favorite;
		this.categoryId = categoryId;
		this.subject = subject;
		this.startDate = startDate;
		RSVP = rSVP;
	}
	
	// Getters, Setters
	public int getEventId() {
		return eventId;
	}
	
	public void setEventId(int id) {
		this.eventId = id;	
	}
	
	public boolean isFavorite() {
		return favorite;
	}
	
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	
	public int getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public boolean isRSVP() {
		return RSVP;
	}
	
	public void setRSVP(boolean rSVP) {
		RSVP = rSVP;
	}
	

	@Override
	public String toString() {
		return "HomeItemModel [eventId=" + eventId + ", favorite=" + favorite
				+ ", categoryId=" + categoryId + ", subject=" + subject
				+ ", startDate=" + startDate + ", RSVP=" + RSVP + "]";
	}

}
