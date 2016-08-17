package com.prs.delivery;

public class Cart {
	
		int count;
		int price;
		String restid;
		String submenuname;
		String foodname;
		
		
		public Cart(String restid,String submenuname,String foodname,int count,int price) {
			super();
			this.count = count;
			this.price = price;
			this.restid = restid;
			this.submenuname = submenuname;
			this.foodname = foodname;
			
		}
		public int getCount() {
			return count;
		}
		public int getPrice() {
			return price;
		}
		
		public String getRestId() {
			return restid;
		}
		public String getSubmenuname() {
			return submenuname;
		}
		public String getFoodname() {
			return foodname;
		}
		
}
