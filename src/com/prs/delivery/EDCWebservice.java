package com.prs.delivery;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class EDCWebservice {
	private static final String ALLOWED_URI_CHARS = null;
	Context mcontext;

	public EDCWebservice(Context mcontext) {
		super();
		this.mcontext = mcontext;
	}

	public String httpGetMethod(String method) throws ClientProtocolException,
			IOException {
		String result = null;

		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet(method);

		httpGet.setHeader("Accept", "application/xml");
		httpGet.setHeader("Content-Type", "application/xml");

		HttpResponse httpResponse = null;

		httpResponse = httpClient.execute(httpGet);

		result = TextHelper.GetText(httpResponse);

		return result;
	}

	// Login
	public boolean LoginAuth(String url) throws ClientProtocolException,
			IOException {
		boolean loginAuthResult = false;
		String loginResult = null;

		loginResult = httpGetMethod(url);

		try {
			XmlPullParserFactory xmlPullFactory = XmlPullParserFactory
					.newInstance();
			xmlPullFactory.setNamespaceAware(true);
			XmlPullParser parser = xmlPullFactory.newPullParser();
			parser.setInput(new StringReader(loginResult));
			int eventType = parser.getEventType();
			while (eventType != parser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_DOCUMENT) {
					System.out.println("Start document");
				} else if (eventType == XmlPullParser.END_DOCUMENT) {
					System.out.println("End document");
				} else if (eventType == XmlPullParser.START_TAG) {
					System.out.println("Start tag " + parser.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					System.out.println("End tag " + parser.getName());
				} else if (eventType == XmlPullParser.TEXT) {

					if (parser.getText().equalsIgnoreCase("true")) {
						loginAuthResult = true;
					} else {
						loginAuthResult = false;
					}
					System.out.println("Text " + parser.getText());
				}
				eventType = parser.next();
			}

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return loginAuthResult;
	}

	// Getting study data by username
	public String GetAllData(String url, int categoryId)
			throws ClientProtocolException, IOException {

		
		
		
		url = "http://deliverydoots.com/android.txt";
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		
		try {
			mDbHelper.clearData(db);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ContentValues values;
		String result;
		result = httpGetMethod(url);
		result = result.replace('&', 'N');
		JSONObject jsonObj = null;
		try {
			jsonObj = XML.toJSONObject(result);
			Log.d("mylog", jsonObj.toString());
			JSONObject jsonGetTable = jsonObj.getJSONObject("urlset");
			//
			if ((jsonGetTable.get("restaur")) instanceof JSONArray) {
				JSONArray jsonArrayTable = (JSONArray) (jsonGetTable
						.get("restaur"));
				for (int i = 0; i < jsonArrayTable.length(); i++) {
					JSONObject objJson = jsonArrayTable.getJSONObject(i);
					values = new ContentValues();
					values.put(FeedReaderDbHelper.RESTAURANT_ID,
							objJson.getInt("id"));
					values.put(FeedReaderDbHelper.RESTAURANT_LOGO,
							objJson.getString("logo"));
					values.put(FeedReaderDbHelper.RESTAURANT_LOCATION,
							objJson.getString("loc"));
					values.put(FeedReaderDbHelper.RESTAURANT_NAME,
							objJson.getString("name"));
					// values.put(FeedReaderDbHelper.RESTAURANT_CAT_ID_FK,categoryId);

					db.insert(FeedReaderDbHelper.RESTAURANTS_TABLE_NAME, null,
							values);

					if ((objJson.get("menu")) instanceof JSONArray) {
						JSONArray jsonMenuArrayTable = (JSONArray) (objJson
								.get("menu"));
						for (int i_menu = 0; i_menu < jsonMenuArrayTable
								.length(); i_menu++) {
							JSONObject objJsonForMenu = jsonMenuArrayTable
									.getJSONObject(i_menu);

							JSONObject jsonMenuitemslist;
							
							try{
							jsonMenuitemslist = (JSONObject) (objJsonForMenu
									.get("menu_items"));
							}
							catch(Exception e){
								continue;
							}
							
							if (jsonMenuitemslist.get("item") instanceof JSONArray) {
								
								
								JSONArray arrayofItems = (JSONArray) jsonMenuitemslist
										.get("item");
								JSONArray arrayofPrice = (JSONArray) jsonMenuitemslist
										.get("price");
								JSONArray arrayofType = (JSONArray) jsonMenuitemslist
										.get("type");
								JSONArray arrayofDesc = (JSONArray) jsonMenuitemslist
										.get("desc");

								int Length = arrayofItems.length();
								for (int i_menu_item = 0; i_menu_item < Length; i_menu_item++) {
									String objForMenuItem = (String) arrayofItems
											.get(i_menu_item);
									int objForMenuPriceinInt = 0;
									double objForMenuPrice;

									try {
										if (arrayofPrice.get(i_menu_item) instanceof Integer) {
											objForMenuPriceinInt = (int) arrayofPrice
													.get(i_menu_item);
											objForMenuPrice = (double) (objForMenuPriceinInt / 1.0);
										} else
											objForMenuPrice = (double) arrayofPrice
													.get(i_menu_item);
									} catch (Exception e) {
										objForMenuPrice = 0.0;
									}

									int objJsonForMenuType = (int) arrayofType
											.get(i_menu_item);
									String objJsonForMenuDesc = (String) arrayofDesc
											.get(i_menu_item);

									ContentValues valuesformenu;
									valuesformenu = new ContentValues();
									valuesformenu
											.put(FeedReaderDbHelper.RESTAURANT_ID_FK,
													objJson.getInt("id"));
									valuesformenu
											.put(FeedReaderDbHelper.RESTAURANT_MENU_NAME,
													objJsonForMenu
															.getString("menu_name"));
									valuesformenu
											.put(FeedReaderDbHelper.RESTAURANT_ITEM_NAME,
													objForMenuItem);
									valuesformenu
											.put(FeedReaderDbHelper.RESTAURANT_ITEM_PRICE,
													Double.toString(objForMenuPrice));
									valuesformenu
											.put(FeedReaderDbHelper.RESTAURANT_ITEM_TYPE,
													Integer.toString(objJsonForMenuType));
									valuesformenu
											.put(FeedReaderDbHelper.RESTAURANT_ITEM_DESC,
													objJsonForMenuDesc);
									db.insert(
											FeedReaderDbHelper.RESTAURANT_MENU,
											null, valuesformenu);

									Log.d("INFO", values.toString());
									Log.d("INFO", valuesformenu.toString());

								}

							}

						}

					}
				}
			}

		} catch (Exception e) {
			Log.e("JSON exception", e.getMessage());
			e.printStackTrace();
		}
		return "";
	}
	public List<String> readLocations() throws ClientProtocolException, IOException{
		
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
		    SQLiteDatabase db = mDbHelper.getReadableDatabase();
		    List<String> rList = new ArrayList<String>();
		    
		    

		 try{
			 
			 String[] projection = {FeedReaderDbHelper.RESTAURANT_LOCATION};

			    	// How you want the results sorted in the resulting Cursor
			    	String sortOrder =
			    			FeedReaderDbHelper.RESTAURANT_LOCATION ;

			    	Cursor cursor = db.query(true,
			    			FeedReaderDbHelper.RESTAURANTS_TABLE_NAME,  // The table to query
			    	    projection,                               // The columns to return
			    	    null,                                // The columns for the WHERE clause
			    	    null,                            // The values for the WHERE clause
			    	    null,                                     // don't group the rows
			    	    null,                                     // don't filter by row groups
			    	    sortOrder,"5000"                                 // The sort order
			    	    );
			    	
			    	while (cursor.moveToNext()) {
			    		rList.add(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_LOCATION)));
			    		
			    	}
			    	
	  
		} catch (Exception e) {
		    Log.e("JSON exception", e.getMessage());
		    e.printStackTrace();
		}
		return rList; 	
		 
	}
	
	public List<RestaurantModel> ReadRestaurantsDataOnLocation(String location) throws ClientProtocolException, IOException{
		
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
		    SQLiteDatabase db = mDbHelper.getReadableDatabase();
		    List<RestaurantModel> rList = new ArrayList<RestaurantModel>();

		 try{
			 
			 String[] projection = {
			    		FeedReaderDbHelper.RESTAURANT_ID,
			    		FeedReaderDbHelper.RESTAURANT_LOCATION,
			    		FeedReaderDbHelper.RESTAURANT_LOGO,		    		
			    		FeedReaderDbHelper.RESTAURANT_NAME

			    	    };
			 String[] selectValue = {
			    		location};
			
			    	// How you want the results sorted in the resulting Cursor
			    	String sortOrder =
			    			FeedReaderDbHelper.RESTAURANT_ID ;

			    	Cursor cursor = db.query(
			    			FeedReaderDbHelper.RESTAURANTS_TABLE_NAME,  // The table to query
			    	    projection,                               // The columns to return
			    	    FeedReaderDbHelper.RESTAURANT_LOCATION+"=?",                                // The columns for the WHERE clause
			    	    selectValue,                            // The values for the WHERE clause
			    	    null,                                     // don't group the rows
			    	    null,                                     // don't filter by row groups
			    	    sortOrder                                 // The sort order
			    	    );
			    	
			    	RestaurantModel aRestaurant;
			    	while (cursor.moveToNext()) {
			    		aRestaurant = new RestaurantModel(cursor.getInt(
					    	    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_ID)),cursor.getString(
						    	    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_NAME)),cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_LOCATION)),
						    	    		cursor.getString(
						    	    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_LOGO)));
			    		rList.add(aRestaurant);
			    	}
			    	
	  
		} catch (Exception e) {
		    Log.e("JSON exception", e.getMessage());
		    e.printStackTrace();
		}
		return rList; 
	}
public List<RestaurantModel> ReadRestaurantsDataOnLocation() throws ClientProtocolException, IOException{
		
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
		    SQLiteDatabase db = mDbHelper.getReadableDatabase();
		    List<RestaurantModel> rList = new ArrayList<RestaurantModel>();

		 try{
			 
			 String[] projection = {
			    		FeedReaderDbHelper.RESTAURANT_ID,
			    		FeedReaderDbHelper.RESTAURANT_LOCATION,
			    		FeedReaderDbHelper.RESTAURANT_LOGO,		    		
			    		FeedReaderDbHelper.RESTAURANT_NAME

			    	    };
			 String[] selectValue = {
			    		};
			
			    	// How you want the results sorted in the resulting Cursor
			    	String sortOrder =
			    			FeedReaderDbHelper.RESTAURANT_ID ;

			    	Cursor cursor = db.query(
			    			FeedReaderDbHelper.RESTAURANTS_TABLE_NAME,  // The table to query
			    	    projection,                               // The columns to return
			    	    null,                                // The columns for the WHERE clause
			    	    null,                            // The values for the WHERE clause
			    	    null,                                     // don't group the rows
			    	    null,                                     // don't filter by row groups
			    	    sortOrder                                 // The sort order
			    	    );
			    	
			    	RestaurantModel aRestaurant;
			    	while (cursor.moveToNext()) {
			    		aRestaurant = new RestaurantModel(cursor.getInt(
					    	    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_ID)),cursor.getString(
						    	    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_NAME)),cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_LOCATION)),
						    	    		cursor.getString(
						    	    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_LOGO)));
			    		rList.add(aRestaurant);
			    	}
			    	
	  
		} catch (Exception e) {
		    Log.e("JSON exception", e.getMessage());
		    e.printStackTrace();
		}
		return rList; 
	}
public RestaurantModel ReadRestaurantDataOnId(String restId) throws ClientProtocolException, IOException{
		
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
		    SQLiteDatabase db = mDbHelper.getReadableDatabase();
		    
		    RestaurantModel aRestaurant = null;
		 try{
			 
			 String[] projection = {
			    		FeedReaderDbHelper.RESTAURANT_ID,
			    		FeedReaderDbHelper.RESTAURANT_LOCATION,
			    		FeedReaderDbHelper.RESTAURANT_LOGO,		    		
			    		FeedReaderDbHelper.RESTAURANT_NAME

			    	    };
			 String[] selectValue = {
			    		restId};
			
			    	// How you want the results sorted in the resulting Cursor
			    	String sortOrder =
			    			FeedReaderDbHelper.RESTAURANT_ID ;

			    	Cursor cursor = db.query(
			    			FeedReaderDbHelper.RESTAURANTS_TABLE_NAME,  // The table to query
			    	    projection,                               // The columns to return
			    	    FeedReaderDbHelper.RESTAURANT_ID+"=?",                                // The columns for the WHERE clause
			    	    selectValue,                            // The values for the WHERE clause
			    	    null,                                     // don't group the rows
			    	    null,                                     // don't filter by row groups
			    	    sortOrder                                 // The sort order
			    	    );
			    	
			    	
			    	while (cursor.moveToNext()) {
			    		aRestaurant = new RestaurantModel(cursor.getInt(
					    	    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_ID)),cursor.getString(
						    	    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_NAME)),cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_LOCATION)),
						    	    		cursor.getString(
						    	    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_LOGO)));
			    		
			    	}
			    	
	  
		} catch (Exception e) {
		    Log.e("JSON exception", e.getMessage());
		    e.printStackTrace();
		}
		return aRestaurant; 
	}
public List<String> ReadRestaurantsAndMenu(String restaurantId) throws ClientProtocolException, IOException{
		
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
		    SQLiteDatabase db = mDbHelper.getReadableDatabase();
		    List<String> rList = new ArrayList<String>();

		 try{
			 
//			 String[] projection = {
//			    		FeedReaderDbHelper.RESTAURANT_MENU_NAME,
//			    		
//
//			    	    };
//			 String[] selectValue = {
//					 restaurantId};
//			
//			    	// How you want the results sorted in the resulting Cursor
//			    	String sortOrder =
//			    			FeedReaderDbHelper.RESTAURANT_MENU_NAME ;
			 
			 
			 String q = "SELECT DISTINCT "+FeedReaderDbHelper.RESTAURANT_MENU_NAME+" FROM "+FeedReaderDbHelper.RESTAURANT_MENU+" WHERE "+FeedReaderDbHelper.RESTAURANT_ID_FK+" = " + restaurantId  ;
			 Cursor cursor = db.rawQuery(q, null);
			    	
			    	
			    	while (cursor.moveToNext()) {
			    		rList.add(cursor.getString(
					    	    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_MENU_NAME)));
			    	}
			    	
	  
		} catch (Exception e) {
		    Log.e("JSON exception", e.getMessage());
		    e.printStackTrace();
		}
		return rList; 
	}
public List<SubMenu> ReadRestaurantMenuForSubmenu(String restaurantId,String menuname) throws ClientProtocolException, IOException{
	
	FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
	    SQLiteDatabase db = mDbHelper.getReadableDatabase();
	    List<SubMenu> menuList = new ArrayList<SubMenu>();
	    SubMenu submenuObj;

	 try{
		  
		 
		 String q = "SELECT "+FeedReaderDbHelper.RESTAURANT_ITEM_NAME+","+FeedReaderDbHelper.RESTAURANT_ITEM_PRICE+","+FeedReaderDbHelper.RESTAURANT_ITEM_TYPE+","+FeedReaderDbHelper.RESTAURANT_ITEM_DESC+" FROM "+FeedReaderDbHelper.RESTAURANT_MENU+" WHERE "+FeedReaderDbHelper.RESTAURANT_ID_FK+" = " + restaurantId +" and "+FeedReaderDbHelper.RESTAURANT_MENU_NAME+" = '"+menuname +"'" ;
		 Cursor cursor = db.rawQuery(q, null);
		    	
		    	
		    	while (cursor.moveToNext()) {
		    		
		    		submenuObj = new SubMenu(restaurantId,cursor.getString(
						    	    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_ITEM_NAME)),cursor.getString(
								    	    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_ITEM_TYPE)),cursor.getDouble(
										    	    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_ITEM_PRICE)),cursor.getString(
												    	    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_ITEM_DESC)),menuname);
		    			
		    	menuList.add(submenuObj);
		    	}
		    	
  
	} catch (Throwable e) {
	    Log.e("JSON exception", e.getMessage());
	    e.printStackTrace();
	}
	return menuList; 
}
	
	// public String GetCategories(String url) throws ClientProtocolException,
	// IOException {
	//
	// FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
	// SQLiteDatabase db = mDbHelper.getWritableDatabase();
	//
	// ContentValues values;
	// String result;
	// result = httpGetMethod(url);
	//
	// JSONObject jsonObj = null;
	// try {
	// jsonObj = XML.toJSONObject(result);
	// Log.d("mylog", jsonObj.toString());
	// JSONObject jsonGetTable = jsonObj.getJSONObject("urlset");
	// //
	// if ((jsonGetTable.get("cat")) instanceof JSONArray) {
	// JSONArray jsonArrayTable = (JSONArray) (jsonGetTable.get("cat"));
	// for (int i = 0; i < jsonArrayTable.length(); i++) {
	// JSONObject objJson = jsonArrayTable.getJSONObject(i);
	// values = new ContentValues();
	// values.put(FeedReaderDbHelper.RESTAURANT_CAT_ID,
	// objJson.getInt("id"));
	// values.put(FeedReaderDbHelper.RESTAURANT_CATNAME,
	// objJson.getString("name"));
	//
	// db.insert(FeedReaderDbHelper.RESTAURANT_CAT_TABLE_NAME,
	// null, values);
	//
	// }
	//
	// } else {
	// JSONObject objJson = (JSONObject) (jsonGetTable.get("cat"));
	// values = new ContentValues();
	// values.put(FeedReaderDbHelper.RESTAURANT_ID,
	// objJson.getInt("id"));
	// values.put(FeedReaderDbHelper.RESTAURANT_LOGO,
	// objJson.getString("logo"));
	//
	// db.insert(FeedReaderDbHelper.RESTAURANT_CAT_TABLE_NAME, null,
	// values);
	//
	// }
	//
	// } catch (JSONException e) {
	// Log.e("JSON exception", e.getMessage());
	// e.printStackTrace();
	// }
	// return jsonObj.toString();
	// }
	//
	// // public void cleanCart(){
	// //
	// // FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
	// // SQLiteDatabase db = mDbHelper.getWritableDatabase();
	// //
	// // String whereclause = FeedReaderDbHelper.COUNT+"=0"; // The columns for
	// // the WHERE clause
	// // int noOfRows =
	// db.delete(FeedReaderDbHelper.CARTTABLE,whereclause,null);
	// // Log.d("XXX", String.valueOf(noOfRows));
	// //
	// // }
	 public void upsert(String restname, String submenuname, String foodname,
	 int count, int price) throws ClientProtocolException, IOException {
	 
	 try {
	 FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
	 SQLiteDatabase db = mDbHelper.getWritableDatabase();
	
	 if (foodhasentry(restname, submenuname, foodname) == -1) {
	
	 ContentValues values = new ContentValues();
	 values.put(FeedReaderDbHelper.RESTID, restname);
	 values.put(FeedReaderDbHelper.SUBMENUNAME, submenuname);
	 values.put(FeedReaderDbHelper.FOODNAME, foodname);
	 values.put(FeedReaderDbHelper.COUNT, count);
	 values.put(FeedReaderDbHelper.PRICE, price);
	
	 long l = db.insert(FeedReaderDbHelper.CARTTABLE, null, values);
	 Log.d("XXX", Long.toString(l));
	
	 } else {
	 String[] args = { restname, submenuname, foodname };
	
	 String query = "update " + FeedReaderDbHelper.CARTTABLE
	 + " set count=" + count + " where "
	 + FeedReaderDbHelper.RESTID + "='" + restname
	 + "' and " + FeedReaderDbHelper.SUBMENUNAME + "='"
	 + submenuname + "' and " + FeedReaderDbHelper.FOODNAME
	 + "='" + foodname + "'";
	 db.execSQL(query);
	 // }
	 }
	 } catch (Exception e) {
	 e.getMessage();
	 }
	 // String query = update
	 // FeedReaderDbHelper.CARTTABLE
	 // UPDATE SET column1 = value1, column2 = value2...., columnN = valueN
	 // WHERE [condition];
	 //
	 // return jsonObj.toString();
	 }
	
	 public void deleteCart() {
	 FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
	 SQLiteDatabase db = mDbHelper.getReadableDatabase();
	 db.execSQL("delete from " + FeedReaderDbHelper.CARTTABLE);
	 }
	
	 public int foodhasentry(String restname, String catname, String foodname)
	 throws ClientProtocolException, IOException {
	
	 FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
	 SQLiteDatabase db = mDbHelper.getReadableDatabase();
	 List<RestaurantModel> rList = new ArrayList<RestaurantModel>();
	 int flag = -1;
	 try {
	 String[] args = { restname, catname, foodname };
	
	 //
	
	 String[] projection = { "rowid", FeedReaderDbHelper.RESTID,
	 FeedReaderDbHelper.SUBMENUNAME, FeedReaderDbHelper.FOODNAME,
	 FeedReaderDbHelper.COUNT
	
	 };
	
	 // How you want the results sorted in the resulting Cursor
	 String sortOrder = FeedReaderDbHelper.RESTAURANT_ID;
	
	 Cursor cursor = db.query(FeedReaderDbHelper.CARTTABLE, // The table
	 // to query
	 projection, // The columns to return
	 FeedReaderDbHelper.RESTID + "=? and "
	 + FeedReaderDbHelper.SUBMENUNAME + "=? and "
	 + FeedReaderDbHelper.FOODNAME + "=?", // The columns
	 // for the
	 // WHERE
	 // clause
	 args, // The values for the WHERE clause
	 null, // don't group the rows
	 null, // don't filter by row groups
	 null // The sort order
	 );
	 //
	  String query ="select * from "+FeedReaderDbHelper.CARTTABLE+" where "+FeedReaderDbHelper.RESTID+"='"+restname+"' and "+FeedReaderDbHelper.SUBMENUNAME+"='"+catname+"' and "+FeedReaderDbHelper.FOODNAME+"='"+foodname+"'";
	 // Cursor cursor = db.rawQuery(query, null);
	 if (cursor.getCount() > 0) {
	 cursor.moveToNext();
	 flag = cursor.getInt(cursor.getColumnIndexOrThrow("rowid"));
	 } else {
	 flag = -1;
	
	 }
	
	 } catch (Exception e) {
	 Log.e("JSON exception", e.getMessage());
	 e.printStackTrace();
	 }
	 return flag;
	 }
	 
	 
	 public int getcount(String foodname)
			 throws ClientProtocolException, IOException {
			
			 FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
			 SQLiteDatabase db = mDbHelper.getReadableDatabase();
			 List<RestaurantModel> rList = new ArrayList<RestaurantModel>();
			 int flag = -1;
			 try {
			 String[] args = {foodname };
			
			 //
			
			 String[] projection = {
			 FeedReaderDbHelper.COUNT
			
			 };
			
			 // How you want the results sorted in the resulting Cursor
			 String sortOrder = FeedReaderDbHelper.RESTAURANT_ID;
			
			 Cursor cursor = db.query(FeedReaderDbHelper.CARTTABLE, // The table
			 // to query
			 projection, // The columns to return
			 FeedReaderDbHelper.FOODNAME + "=?", // The columns
			 // for the
			 // WHERE
			 // clause
			 args, // The values for the WHERE clause
			 null, // don't group the rows
			 null, // don't filter by row groups
			 null // The sort order
			 );
			 //
			 // Cursor cursor = db.rawQuery(query, null);
			 if (cursor.getCount() > 0) {
			 cursor.moveToNext();
			 flag = cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderDbHelper.COUNT));
			 } else {
			 flag = -1;
			
			 }
			
			 } catch (Exception e) {
			 Log.e("JSON exception", e.getMessage());
			 e.printStackTrace();
			 }
			 return flag;
			 }
	
	 public List<Cart> readCart() throws ClientProtocolException, IOException
	 {
	
	 FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
	 SQLiteDatabase db = mDbHelper.getReadableDatabase();
	 List<Cart> list = new ArrayList<Cart>();
	
	 try {
	
	 String[] projection = { FeedReaderDbHelper.RESTID,
	 FeedReaderDbHelper.SUBMENUNAME, FeedReaderDbHelper.FOODNAME,
	 FeedReaderDbHelper.COUNT, FeedReaderDbHelper.PRICE
	
	 };
	 String[] selectValue = { "0", null };
	
	 // How you want the results sorted in the resulting Cursor
	 String sortOrder = FeedReaderDbHelper.RESTAURANT_ID;
	
	 Cursor cursor = db.query(FeedReaderDbHelper.CARTTABLE, // The table
	 // to query
	 projection, // The columns to return
	 FeedReaderDbHelper.COUNT + "!=0", // The columns for the
	 // WHERE clause
	 null, // The values for the WHERE clause
	 null, // don't group the rows
	 null, // don't filter by row groups
	 null // The sort order
	 );
	
	 Cart cartObject;
	 while (cursor.moveToNext()) {
	
	 cartObject = new Cart(
	 cursor.getString(cursor
	 .getColumnIndexOrThrow(FeedReaderDbHelper.RESTID)),
	 cursor.getString(cursor
	 .getColumnIndexOrThrow(FeedReaderDbHelper.SUBMENUNAME)),
	 cursor.getString(cursor
	 .getColumnIndexOrThrow(FeedReaderDbHelper.FOODNAME)),
	 cursor.getInt(cursor
	 .getColumnIndexOrThrow(FeedReaderDbHelper.COUNT)),
	 cursor.getInt(cursor
	 .getColumnIndexOrThrow(FeedReaderDbHelper.PRICE)));
	 //
	 if(cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderDbHelper.COUNT))!=0)
	
	 list.add(cartObject);
	 }
	
	 } catch (Exception e) {
	 Log.e("JSON exception", e.getMessage());
	 e.printStackTrace();
	 }
	 return list;
	
	 }
	//
	// public List<RestaurantModel> ReadRestaurantsDataOnLocation(String
	// location)
	// throws ClientProtocolException, IOException {
	//
	// FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
	// SQLiteDatabase db = mDbHelper.getReadableDatabase();
	// List<RestaurantModel> rList = new ArrayList<RestaurantModel>();
	//
	// try {
	//
	// String[] projection = { FeedReaderDbHelper.RESTAURANT_ID,
	// FeedReaderDbHelper.RESTAURANT_LOCATION,
	// FeedReaderDbHelper.RESTAURANT_LOGO,
	// FeedReaderDbHelper.RESTAURANT_NAME
	//
	// };
	// String[] selectValue = { location };
	//
	// // How you want the results sorted in the resulting Cursor
	// String sortOrder = FeedReaderDbHelper.RESTAURANT_ID;
	//
	// Cursor cursor = db.query(FeedReaderDbHelper.RESTAURANTS_TABLE_NAME, //
	// The
	// // table
	// // to
	// // query
	// projection, // The columns to return
	// FeedReaderDbHelper.RESTAURANT_LOCATION + "=?", // The
	// // columns
	// // for the
	// // WHERE
	// // clause
	// selectValue, // The values for the WHERE clause
	// null, // don't group the rows
	// null, // don't filter by row groups
	// sortOrder // The sort order
	// );
	//
	// RestaurantModel aRestaurant;
	// while (cursor.moveToNext()) {
	// aRestaurant = new RestaurantModel(
	// cursor.getInt(cursor
	// .getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_ID)),
	// cursor.getString(cursor
	// .getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_NAME)),
	// cursor.getString(cursor
	// .getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_LOCATION)),
	// cursor.getString(cursor
	// .getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_LOGO)));
	// rList.add(aRestaurant);
	// }
	//
	// } catch (Exception e) {
	// Log.e("JSON exception", e.getMessage());
	// e.printStackTrace();
	// }
	// return rList;
	// }
	//
	// public RestaurantModel ReadRestaurantsDataOnId(int restid)
	// throws ClientProtocolException, IOException {
	//
	// FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
	// SQLiteDatabase db = mDbHelper.getReadableDatabase();
	// RestaurantModel aRestaurant = null;
	// try {
	//
	// String[] projection = { FeedReaderDbHelper.RESTAURANT_ID,
	// FeedReaderDbHelper.RESTAURANT_LOCATION,
	// FeedReaderDbHelper.RESTAURANT_LOGO,
	// FeedReaderDbHelper.RESTAURANT_NAME
	//
	// };
	// String[] selectValue = { Integer.toString(restid) };
	//
	// // How you want the results sorted in the resulting Cursor
	// String sortOrder = FeedReaderDbHelper.RESTAURANT_ID;
	//
	// Cursor cursor = db.query(FeedReaderDbHelper.RESTAURANTS_TABLE_NAME, //
	// The
	// // table
	// // to
	// // query
	// projection, // The columns to return
	// FeedReaderDbHelper.RESTAURANT_ID + "=?", // The columns for
	// // the WHERE
	// // clause
	// selectValue, // The values for the WHERE clause
	// null, // don't group the rows
	// null, // don't filter by row groups
	// sortOrder // The sort order
	// );
	//
	// while (cursor.moveToNext()) {
	// aRestaurant = new RestaurantModel(
	// cursor.getInt(cursor
	// .getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_ID)),
	// cursor.getString(cursor
	// .getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_NAME)),
	// cursor.getString(cursor
	// .getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_LOCATION)),
	// cursor.getString(cursor
	// .getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_LOGO)));
	// }
	//
	// } catch (Exception e) {
	// Log.e("JSON exception", e.getMessage());
	// e.printStackTrace();
	// }
	// return aRestaurant;
	// }
	//
	// public List<RestaurantModel> ReadRestaurantsDataOnCategory(String
	// category)
	// throws ClientProtocolException, IOException {
	//
	// FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
	// SQLiteDatabase db = mDbHelper.getReadableDatabase();
	// List<RestaurantModel> rList = new ArrayList<RestaurantModel>();
	//
	// try {
	//
	// String[] projection = { FeedReaderDbHelper.RESTAURANT_ID,
	// FeedReaderDbHelper.RESTAURANT_LOCATION,
	// FeedReaderDbHelper.RESTAURANT_LOGO,
	// FeedReaderDbHelper.RESTAURANT_NAME
	//
	// };
	// String[] selectValue = { category };
	//
	// // How you want the results sorted in the resulting Cursor
	// String sortOrder = FeedReaderDbHelper.RESTAURANT_ID;
	//
	// Cursor cursor = db.query(FeedReaderDbHelper.RESTAURANTS_TABLE_NAME, //
	// The
	// // table
	// // to
	// // query
	// projection, // The columns to return
	// FeedReaderDbHelper.RESTAURANT_CAT_ID_FK + "=?", // The
	// // columns
	// // for the
	// // WHERE
	// // clause
	// selectValue, // The values for the WHERE clause
	// null, // don't group the rows
	// null, // don't filter by row groups
	// sortOrder // The sort order
	// );
	//
	// RestaurantModel aRestaurant;
	// while (cursor.moveToNext()) {
	// aRestaurant = new RestaurantModel(
	// cursor.getInt(cursor
	// .getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_ID)),
	// cursor.getString(cursor
	// .getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_NAME)),
	// cursor.getString(cursor
	// .getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_LOCATION)),
	// cursor.getString(cursor
	// .getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_LOGO)));
	// rList.add(aRestaurant);
	// }
	//
	// } catch (Exception e) {
	// Log.e("JSON exception", e.getMessage());
	// e.printStackTrace();
	// }
	// return rList;
	//
	// }
	//
	// public List<String> readLocations(String url)
	// throws ClientProtocolException, IOException {
	//
	// FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
	// SQLiteDatabase db = mDbHelper.getReadableDatabase();
	// List<String> rList = new ArrayList<String>();
	//
	// try {
	//
	// String[] projection = { FeedReaderDbHelper.RESTAURANT_LOCATION };
	//
	// // How you want the results sorted in the resulting Cursor
	// String sortOrder = FeedReaderDbHelper.RESTAURANT_LOCATION;
	//
	// Cursor cursor = db.query(true,
	// FeedReaderDbHelper.RESTAURANTS_TABLE_NAME, // The table to
	// // query
	// projection, // The columns to return
	// null, // The columns for the WHERE clause
	// null, // The values for the WHERE clause
	// null, // don't group the rows
	// null, // don't filter by row groups
	// sortOrder, "5000" // The sort order
	// );
	//
	// while (cursor.moveToNext()) {
	// rList.add(cursor.getString(cursor
	// .getColumnIndexOrThrow(FeedReaderDbHelper.RESTAURANT_LOCATION)));
	//
	// }
	//
	// } catch (Exception e) {
	// Log.e("JSON exception", e.getMessage());
	// e.printStackTrace();
	// }
	// return rList;
	//
	// }
	//
	// public String readFileAsString(String fileName) {
	//
	// StringBuilder stringBuilder = new StringBuilder();
	// String line;
	// BufferedReader in = null;
	//
	// try {
	// in = new BufferedReader(new FileReader(new File(
	// mcontext.getFilesDir(), fileName)));
	// while ((line = in.readLine()) != null)
	// stringBuilder.append(line);
	// in.close();
	//
	// } catch (FileNotFoundException e) {
	// // Logger.logError("ERRO", e);
	// } catch (IOException e) {
	// // Logger.logError(TAG, e);
	// }
	//
	// return stringBuilder.toString();
	// }
	//
	// public void writeStringAsFile(final String fileContents, String fileName)
	// {
	// // Context context = this.;
	// try {
	// FileWriter out = new FileWriter(new File(mcontext.getFilesDir(),
	// fileName));
	// out.write(fileContents);
	// out.close();
	// } catch (IOException e) {
	// // Logger.logError("", e);
	// }
	// }
	//
	// public List<String> GetMenu(int restid) throws ClientProtocolException,
	// IOException {
	//
	// FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
	// SQLiteDatabase db = mDbHelper.getWritableDatabase();
	// ContentValues values;
	// String result;
	// String url = "http://deliverydoots.com/android007/menu/" + restid
	// + ".xml";
	// result = httpGetMethod(url);
	// result =
	// "<urlset><menu><name>STARTERS (veg)</name><id>5</id><items><item><name>green salad 1</name><id>10</id><cat>veg</cat><price>110</price></item><item><name>green salad 2</name><id>10</id><cat>veg</cat><price>120</price></item><item><name>green salad 3</name><id>10</id><cat>veg</cat><price>130</price></item></items></menu><menu><name>MEAL (veg)</name><id>5</id><items><item><name>veg meal 1</name><id>10</id><cat>veg</cat><price>310</price></item><item><name>veg meal 2</name><id>10</id><cat>veg</cat><price>320</price></item><item><name>veg meal 3</name><id>10</id><cat>veg</cat><price>330</price></item></items></menu></urlset>";
	// this.writeStringAsFile(result, "result.xml");
	// result = "";
	// result = this.readFileAsString("result.xml");
	// List<String> rList = new ArrayList<String>();
	// JSONObject jsonObj = null;
	//
	// try {
	// jsonObj = XML.toJSONObject(result);
	// Log.d("mylog", jsonObj.toString());
	// JSONObject jsonGetTable = jsonObj.getJSONObject("urlset");
	// //
	// JSONArray jsonArrayTable = (JSONArray) (jsonGetTable.get("menu"));
	// for (int i = 0; i < jsonArrayTable.length(); i++) {
	// JSONObject json = jsonArrayTable.getJSONObject(i);
	// rList.add(json.getString("name"));
	//
	// }
	//
	// } catch (JSONException e) {
	// Log.e("JSON exception", e.getMessage());
	// e.printStackTrace();
	// }
	// return rList;
	// }
	//
	// public List<SubMenu> GetSubMenu(int menuid) throws
	// ClientProtocolException,
	// IOException {
	//
	// FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(mcontext);
	// SQLiteDatabase db = mDbHelper.getWritableDatabase();
	// ContentValues values;
	// String result;
	// // String url =
	// // "http://deliverydoots.com/android007/menu/"+restid+".xml";
	// // result=httpGetMethod(url);
	// // result =
	// //
	// "<urlset><menu><name>STARTERS (veg)</name><id>5</id><items><item><name>green salad 1</name><id>41</id><cat>veg</cat><price>110</price></item><item><name>green salad 2</name><id>42</id><cat>veg</cat><price>120</price></item><item><name>green salad 3</name><id>43</id><cat>veg</cat><price>130</price></item></items></menu><menu><name>MEAL (veg)</name><id>5</id><items><item><name>veg meal 1</name><id>51</id><cat>veg</cat><price>310</price></item><item><name>veg meal 2</name><id>53</id><cat>veg</cat><price>320</price></item><item><name>veg meal 3</name><id>54</id><cat>veg</cat><price>330</price></item></items></menu></urlset>";
	// // this.writeStrinfgAsFile(result, "result.xml");
	// // result = "";
	// result = this.readFileAsString("result.xml");
	// JSONObject jsonObj = null;
	// List<SubMenu> submenuList = new ArrayList<SubMenu>();
	//
	// try {
	// jsonObj = XML.toJSONObject(result);
	// Log.d("mylog", jsonObj.toString());
	// JSONObject jsonGetTable = jsonObj.getJSONObject("urlset");
	// //
	// JSONArray jsonArrayTable = (JSONArray) (jsonGetTable.get("menu"));
	// // for(int i=0;i<jsonArrayTable.length();i++){
	// JSONObject json = jsonArrayTable.getJSONObject(menuid);
	// // rList.add(json.getString("name"));
	// JSONArray jsonArrayforitems = (JSONArray) ((JSONObject) json
	// .get("items")).get("item");
	// SubMenu submenuObject;
	// for (int i = 0; i < jsonArrayforitems.length(); i++) {
	//
	// JSONObject obj = (JSONObject) jsonArrayforitems.get(i);
	//
	// submenuObject = new SubMenu(obj.getInt("id"),
	// obj.getString("name"), obj.getString("cat"),
	// obj.getInt("price"));
	// submenuList.add(submenuObject);
	// }
	//
	// // }
	//
	// } catch (JSONException e) {
	// Log.e("JSON exception", e.getMessage());
	// e.printStackTrace();
	// }
	// return submenuList;
	// }
	// // Getting study data by Study ID
	// public List<EDCStudyIdTable> GetStudyIdDetails(String url) throws
	// ClientProtocolException, IOException{
	//
	// List<EDCStudyIdTable> iDTableList= new ArrayList<EDCStudyIdTable>() ;
	//
	// String result;
	// result=httpGetMethod(url);
	//
	//
	// JSONObject jsonObj = null;
	// try {
	// jsonObj = XML.toJSONObject(result);
	// JSONObject jsonGetTable=
	// jsonObj.getJSONObject("GetSiteDataResponse").getJSONObject("GetSiteDataResult");
	//
	// if(jsonGetTable.get("a:SiteMetaData") instanceof JSONArray){
	// JSONArray
	// jsonArrayTable=jsonObj.getJSONObject("GetSiteDataResponse").getJSONObject("GetSiteDataResult").getJSONArray("a:SiteMetaData");
	// for(int i=0;i<jsonArrayTable.length();i++){
	// JSONObject json= jsonArrayTable.getJSONObject(i);
	// EDCStudyIdTable edcTabelData= new
	// EDCStudyIdTable(json.getInt("a:arms"),json.getString("a:country"),json.getString("a:location"),json.getString("a:sitedescription"),json.getInt("a:siteid"),json.getString("a:sitename"),json.getInt("a:subjects"));
	// iDTableList.add(edcTabelData);
	// }
	// }else{
	// JSONObject json=jsonGetTable.getJSONObject("a:SiteMetaData");
	// EDCStudyIdTable edcTabelData= new
	// EDCStudyIdTable(json.getInt("a:arms"),json.getString("a:country"),json.getString("a:location"),json.getString("a:sitedescription"),json.getInt("a:siteid"),json.getString("a:sitename"),json.getInt("a:subjects"));
	// iDTableList.add(edcTabelData);
	//
	// }
	//
	// } catch (JSONException e) {
	// Log.e("JSON exception", e.getMessage());
	// e.printStackTrace();
	// }
	//
	//
	// return iDTableList;
	// }
	//
	// //Getting subject data by Study ID and Site Name
	// public List<EDCSubjectDataTable> GetSubjectDetails(String url) throws
	// ClientProtocolException, IOException, URISyntaxException{
	//
	// List<EDCSubjectDataTable> subjectDataTableList= new
	// ArrayList<EDCSubjectDataTable>() ;
	//
	// String result;
	//
	//
	// result=httpGetMethod(url);
	//
	//
	// JSONObject jsonObj = null;
	// try {
	// jsonObj = XML.toJSONObject(result);
	// JSONObject jsonGetTable=
	// jsonObj.getJSONObject("GetSubjectDataResponse").getJSONObject("GetSubjectDataResult");
	//
	//
	// if(jsonGetTable.get("a:SubjectMetaData") instanceof JSONArray){
	// JSONArray
	// jsonArrayTable=jsonObj.getJSONObject("GetSubjectDataResponse").getJSONObject("GetSubjectDataResult").getJSONArray("a:SubjectMetaData");
	// for(int i=0;i<jsonArrayTable.length();i++){
	// JSONObject json= jsonArrayTable.getJSONObject(i);
	// EDCSubjectDataTable edcTabelData= new
	// EDCSubjectDataTable(json.getString("a:armname"),json.getString("a:sitename"),json.getInt("a:subjectid"));
	// subjectDataTableList.add(edcTabelData);
	// }
	// }else{
	// JSONObject json=jsonGetTable.getJSONObject("a:SubjectMetaData");
	// EDCSubjectDataTable edcTabelData= new
	// EDCSubjectDataTable(json.getString("a:armname"),json.getString("a:sitename"),json.getInt("a:subjectid"));
	// subjectDataTableList.add(edcTabelData);
	//
	// }
	//
	// } catch (JSONException e) {
	// Log.e("JSON exception", e.getMessage());
	// e.printStackTrace();
	// }
	//
	//
	// return subjectDataTableList;
	// }
	//
	//
	// public List<EDCVisitDataTable> GetVisitData(String url) throws
	// ClientProtocolException, IOException{
	// List<EDCVisitDataTable> visitDataTableList= new
	// ArrayList<EDCVisitDataTable>() ;
	//
	// String result;
	//
	//
	// result=httpGetMethod(url);
	//
	//
	// JSONObject jsonObj = null;
	// try {
	// jsonObj = XML.toJSONObject(result);
	// JSONObject jsonGetTable=
	// jsonObj.getJSONObject("GetVisitDataResponse").getJSONObject("GetVisitDataResult");
	//
	//
	// if(jsonGetTable.get("a:VisitMetaData") instanceof JSONArray){
	// JSONArray
	// jsonArrayTable=jsonObj.getJSONObject("GetVisitDataResponse").getJSONObject("GetVisitDataResult").getJSONArray("a:VisitMetaData");
	// for(int i=0;i<jsonArrayTable.length();i++){
	// JSONObject json= jsonArrayTable.getJSONObject(i);
	// EDCVisitDataTable edcTabelData= new
	// EDCVisitDataTable(json.getInt("a:forms"),json.getString("a:visitname"));
	// visitDataTableList.add(edcTabelData);
	// }
	// }else{
	// JSONObject json=jsonGetTable.getJSONObject("a:SubjectMetaData");
	// EDCVisitDataTable edcTabelData= new
	// EDCVisitDataTable(json.getInt("a:forms"),json.getString("a:visitname"));
	// visitDataTableList.add(edcTabelData);
	//
	// }
	//
	// } catch (JSONException e) {
	// Log.e("JSON exception", e.getMessage());
	// e.printStackTrace();
	// }
	//
	//
	// return visitDataTableList;
	// }
	//
	// public ArrayList<EDCMasterViewTable> GetForomData(String url) throws
	// ClientProtocolException, IOException{
	// ArrayList<EDCMasterViewTable> foromDataTableList= new
	// ArrayList<EDCMasterViewTable>() ;
	//
	// String result;
	//
	//
	// result=httpGetMethod(url);
	//
	//
	// JSONObject jsonObj = null;
	// try {
	// jsonObj = XML.toJSONObject(result);
	// JSONObject jsonGetTable=
	// jsonObj.getJSONObject("GetFormDataResponse").getJSONObject("GetFormDataResult");
	//
	//
	// if(jsonGetTable.get("a:string") instanceof JSONArray){
	// JSONArray
	// jsonArrayTable=jsonObj.getJSONObject("GetFormDataResponse").getJSONObject("GetFormDataResult").getJSONArray("a:string");
	// for(int i=0;i<jsonArrayTable.length();i++){
	//
	// EDCMasterViewTable edcTabelData= new
	// EDCMasterViewTable(jsonArrayTable.get(i).toString(),i);
	// foromDataTableList.add(edcTabelData);
	// }
	// }else{
	// JSONObject json=jsonGetTable.getJSONObject("a:string");
	// EDCMasterViewTable edcTabelData= new EDCMasterViewTable(json+"",0);
	// foromDataTableList.add(edcTabelData);
	//
	// }
	//
	// } catch (JSONException e) {
	// Log.e("JSON exception", e.getMessage());
	// e.printStackTrace();
	// }
	//
	//
	// return foromDataTableList;
	// }
	//
	//
	// public ArrayList<EDCMainDetailsTable> GetDetailsForomData(String url)
	// throws ClientProtocolException, IOException{
	// ArrayList<EDCMainDetailsTable> foromDataTableList= new
	// ArrayList<EDCMainDetailsTable>() ;
	//
	// String result;
	//
	//
	// result=httpGetMethod(url);
	//
	//
	// JSONObject jsonObj = null;
	// try {
	// jsonObj = XML.toJSONObject(result);
	// JSONObject jsonGetTable=
	// jsonObj.getJSONObject("GetXMLsResponse").getJSONObject("GetXMLsResult").getJSONObject("a:FormMetaData");
	//
	// String jsonResult= jsonGetTable.getString("a:formdata");
	// JSONObject formJson= XML.toJSONObject(jsonResult);
	// String form= formJson.getJSONObject("form").getString("fieldset");
	// JSONObject jsonObject= new JSONObject(form);
	// JSONArray fieldJsonArray=jsonObject.getJSONArray("field");
	// JSONObject fieldJsonObject;
	// for (int i=0;i<fieldJsonArray.length();i++){
	// fieldJsonObject= fieldJsonArray.getJSONObject(i);
	// int minLength;
	// String gui_label;
	// String charecteristics;
	// String visible;
	// int grp_name;
	// String indent;
	// String ctrl_name;
	// String mantissa;
	// String ed_chk;
	// String cd_lst;
	// String allowpartial;
	// int maxlength;
	// String data_type;
	// String ctrl_type;
	// String description;
	// String src;
	// String required;
	// String dt_st_label;
	//
	// if(fieldJsonObject.has("minlength")&&!fieldJsonObject.isNull("minlength")){
	// minLength=fieldJsonObject.getInt("minlength");
	// }else{
	// minLength=0;
	// }
	//
	// if(fieldJsonObject.has("gui_label")&&!fieldJsonObject.isNull("gui_label")){
	// gui_label=fieldJsonObject.getString("gui_label");
	// }else{
	// gui_label="default";
	// }
	// if(fieldJsonObject.has("charecteristics")&&!fieldJsonObject.isNull("charecteristics")){
	// charecteristics=fieldJsonObject.getString("charecteristics");
	// }else{
	// charecteristics="default";
	// }
	// if(fieldJsonObject.has("visible")&&!fieldJsonObject.isNull("visible")){
	// visible=fieldJsonObject.getString("visible");
	// }else{
	// visible="default";
	// }
	// if(fieldJsonObject.has("grp_name")&&!fieldJsonObject.isNull("grp_name")){
	// grp_name=fieldJsonObject.getInt("grp_name");
	// }else{
	// grp_name=-1;
	// }
	// if(fieldJsonObject.has("indent")&&!fieldJsonObject.isNull("indent")){
	// indent=fieldJsonObject.getString("indent");
	// }else{
	// indent="default";
	// }
	// if(fieldJsonObject.has("ctrl_name")&&!fieldJsonObject.isNull("ctrl_name")){
	// ctrl_name=fieldJsonObject.getString("ctrl_name");
	// }else{
	// ctrl_name="default";
	// }
	// if(fieldJsonObject.has("mantissa")&&!fieldJsonObject.isNull("mantissa")){
	// mantissa=fieldJsonObject.getString("mantissa");
	// }else{
	// mantissa="default";
	// }
	// if(fieldJsonObject.has("ed_chk")&&!fieldJsonObject.isNull("ed_chk")){
	// ed_chk=fieldJsonObject.getString("ed_chk");
	// }else{
	// ed_chk="default";
	// }
	// if(fieldJsonObject.has("cd_lst")&&!fieldJsonObject.isNull("cd_lst")){
	// cd_lst=fieldJsonObject.getString("cd_lst");
	// }else{
	// cd_lst="default";
	// }
	// if(fieldJsonObject.has("allowpartial")&&!fieldJsonObject.isNull("allowpartial")){
	// allowpartial=fieldJsonObject.getString("allowpartial");
	// }else{
	// allowpartial="default";
	// }
	// if(fieldJsonObject.has("maxlength")&&!fieldJsonObject.isNull("maxlength")){
	// maxlength=fieldJsonObject.getInt("maxlength");
	// }else{
	// maxlength=-1;
	// }
	// if(fieldJsonObject.has("data_type")&&!fieldJsonObject.isNull("data_type")){
	// data_type=fieldJsonObject.getString("data_type");
	// }else{
	// data_type="default";
	// }
	// if(fieldJsonObject.has("ctrl_type")&&!fieldJsonObject.isNull("ctrl_type")){
	// ctrl_type=fieldJsonObject.getString("ctrl_type");
	// }else{
	// ctrl_type="default";
	// }
	// if(fieldJsonObject.has("description")&&!fieldJsonObject.isNull("description")){
	// description=fieldJsonObject.getString("description");
	// }else{
	// description="default";
	// }
	// if(fieldJsonObject.has("src")&&!fieldJsonObject.isNull("src")){
	// src=fieldJsonObject.getString("src");
	// }else{
	//
	// src="default";
	// }
	// if(fieldJsonObject.has("required")&&!fieldJsonObject.isNull("required")){
	// required=fieldJsonObject.getString("required");
	// }else{
	// required="default";
	// }
	// if(fieldJsonObject.has("dt_st_label")&&!fieldJsonObject.isNull("dt_st_label")){
	// dt_st_label=fieldJsonObject.getString("dt_st_label");
	// }else{
	// dt_st_label="default";
	// }
	//
	//
	// EDCMainDetailsTable edcTableData= new EDCMainDetailsTable( minLength,
	// gui_label,
	// charecteristics, visible, grp_name,
	// indent, ctrl_name, mantissa, ed_chk,
	// cd_lst, allowpartial, maxlength,
	// data_type, ctrl_type, description, src,
	// required, dt_st_label);
	// foromDataTableList.add(edcTableData);
	// }
	//
	// } catch (JSONException e) {
	// Log.e("JSON exception", e.getMessage());
	// e.printStackTrace();
	// }
	//
	//
	// return foromDataTableList;
	// }
	//
}
