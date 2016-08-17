package com.prs.delivery;

public class RestaurantModel {
	int id;
	String name;
	String location;
	String logoname;
	
	public RestaurantModel(int id, String name, String location,
			String logoname) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.logoname = logoname;
		
	}
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}
	
	public String getLogoname() {
		return logoname;
	}
}
