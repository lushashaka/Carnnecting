package com.carnnecting.entities;

public class User {
	private int 	id;			// Primary key
	private String	fbLogin;	// However, fbLogin should also be unique
	// other user info..
	
	// Constructors
	public User(int id, String fbLogin) {
		super();
		this.id = id;
		this.fbLogin = fbLogin;
	}


	// Getters, Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFbLogin() {
		return fbLogin;
	}

	public void setFbLogin(String fbLogin) {
		this.fbLogin = fbLogin;
	}

	public String toString() {
		return "User [id=" + id + ", fbLogin=" + fbLogin + "]";
	}
}