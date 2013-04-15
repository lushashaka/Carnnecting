package com.carnnecting.entities;

public class Category {
	private int 		id;					// Primary key. Use some HashMap or Enum to make it human readable, but not for now.
	private String		name;
	private String		description;
	private int			parentCatId;		// The id to its parent directory
	// private ImageObject	Icon;			// The icon of this cateogry 
	
	// Constructors
	public Category(int id, String name, String description, int parentCatId) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.parentCatId = parentCatId;
	}

	// Getters, Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getParentCatId() {
		return parentCatId;
	}

	public void setParentCatId(int parentCatId) {
		this.parentCatId = parentCatId;
	}

	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", description="
				+ description + ", parentCatId=" + parentCatId + "]";
	}
	
}