package com.prs.delivery;


public class SubMenu {
	String restId;
	String name;
	String type;
	double price;
	String desc;
	String menuname;
	
	public SubMenu(String id, String name, String type,
			double price,String desc,String menuname) {
		super();
		this.restId = id;
		this.name = name;
		this.type = type;
		this.price = price;
		this.desc = desc;
		
	}
	public String getDesc() {
		return desc;
	}
	
	public String getName() {
		return name;
	}

	public String gettype() {
		return type;
	}
	
	public double getPrice() {
		return price;
	}
	public String getRestId() {
		return restId;
	}
	public String getMenuName() {
		return menuname;
	}
}
