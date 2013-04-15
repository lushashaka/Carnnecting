package com.carnnecting.entities;

import java.text.SimpleDateFormat;
import java.util.*;

public class Event {
	// Single place that define the date format across the system
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private int 			id;				// Primary key
	private String 			subject;
	private Date			startTime;
	private Date			endTime;
	private String 			description;
	private int 			categoryId;		// Can be defined in some Enum in entities/Category.java
	
	// Constructors
	public Event(int id, String subject, Date startTime, Date endTime,
			String description, int categoryId) {
		super();
		this.id = id;
		this.subject = subject;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.categoryId = categoryId;
	}

	// Getters, Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public static SimpleDateFormat getDateformat() {
		return dateFormat;
	}

	public String toString() {
		return "Event [id=" + id + ", subject=" + subject + ", startTime="
				+ startTime + ", endTime=" + endTime + ", description="
				+ description + ", categoryId=" + categoryId + "]";
	}

}