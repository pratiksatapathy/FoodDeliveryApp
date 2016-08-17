package com.prs.delivery;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DD.db";

    
    //TABLE 1 CONSTANTS
    public static final String RESTAURANTS_TABLE_NAME = "RESTAURANTS";
    public static final String RESTAURANT_ID = "ID";
    public static final String RESTAURANT_LOGO = "LOGO";
    public static final String RESTAURANT_LOCATION  = "LOCATION";
    public static final String RESTAURANT_NAME  = "NAME";
    
    //TABLE 2 CONSTANTS
    public static final String RESTAURANT_MENU = "RESTAURANT_MENU";
    public static final String RESTAURANT_ID_FK = "RESTAURANT_ID_FK";
    public static final String RESTAURANT_MENU_NAME = "RESTAURANT_MENU_NAME";
    public static final String RESTAURANT_ITEM_NAME = "RESTAURANT_ITEM_NAME";
    public static final String RESTAURANT_ITEM_PRICE = "RESTAURANT_ITEM_PRICE";
    public static final String RESTAURANT_ITEM_TYPE = "RESTAURANT_ITEM_TYPE";
    public static final String RESTAURANT_ITEM_DESC = "RESTAURANT_ITEM_DESC";

    //TABLE 3 CONSTANTS
    public static final String CARTTABLE = "RESTAURANTS_CART";
    public static final String RESTID = "RESTID";
    public static final String FOODNAME = "FOODNAME";
    public static final String SUBMENUNAME = "SUBMENUNAME";
    public static final String COUNT = "COUNT";
    public static final String PRICE = "PRICE";
    
    
    private static final String SQL_CREATE_RESTAURANTS = "CREATE TABLE RESTAURANTS (ID INTEGER,LOGO TEXT,LOCATION TEXT,NAME TEXT)";
    private static final String SQL_CREATE_RESTAURANTS_MENU = "CREATE TABLE RESTAURANT_MENU (RESTAURANT_MENU_NAME TEXT,RESTAURANT_ID_FK INTEGER,RESTAURANT_ITEM_NAME TEXT,RESTAURANT_ITEM_PRICE TEXT,RESTAURANT_ITEM_TYPE TEXT,RESTAURANT_ITEM_DESC TEXT)";
    private static final String SQL_CREATE_CART = "CREATE TABLE RESTAURANTS_CART ("+RESTID+" TEXT,"+SUBMENUNAME+" TEXT,"+FOODNAME+" TEXT,"+COUNT+" INTEGER,"+PRICE+" INTEGER)";


    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + RESTAURANTS_TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES3 =
            "DROP TABLE IF EXISTS " + RESTAURANT_MENU;
    
    private static final String SQL_DELETE_ENTRIES4 =
            "DROP TABLE IF EXISTS "+ CARTTABLE;
    
    private static final String SQL_DELETE_ONLY_ENTRIES =
            "DELETE FROM " + RESTAURANTS_TABLE_NAME;

        private static final String SQL_DELETE_ONLY_ENTRIES3 =
                "DELETE FROM " + RESTAURANT_MENU;
        
        private static final String SQL_DELETE_ONLY_ENTRIES4 =
                "DELETE FROM "+ CARTTABLE;
    

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
    	
    	db.execSQL(SQL_DELETE_ENTRIES);
    	db.execSQL(SQL_DELETE_ENTRIES3);
    	db.execSQL(SQL_DELETE_ENTRIES4);


        db.execSQL(SQL_CREATE_RESTAURANTS);
        db.execSQL(SQL_CREATE_RESTAURANTS_MENU);
        db.execSQL(SQL_CREATE_CART);

    }
 public void clearData(SQLiteDatabase db) {
    	
    	db.execSQL(SQL_DELETE_ONLY_ENTRIES);
    	db.execSQL(SQL_DELETE_ONLY_ENTRIES3);
    	db.execSQL(SQL_DELETE_ONLY_ENTRIES4);



    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES3);
    	db.execSQL(SQL_DELETE_ENTRIES4);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
