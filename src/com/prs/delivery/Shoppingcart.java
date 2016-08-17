package com.prs.delivery;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Shoppingcart extends Fragment {
	final List<SubMenu> listOfmenuitem = new ArrayList<SubMenu>();
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {

	        // If activity recreated (such as from screen rotate), restore
	        // the previous article selection set by onSaveInstanceState().
	        // This is primarily necessary when in the two-pane layout.
	      
	        // Inflate the layout for this fragment
		 
		 View rootView = inflater.inflate(R.layout.fragment_shoppingbasket, container, false);
		// restList = new ArrayList<RestaurantModel>();
		 SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity());
		 RestaurantModel restObject=null;
		 List<Cart> listOfcart = new ArrayList<Cart>();
		
		 {
		};
		 int restid = saved_values.getInt("restid",-1);
		 int menuid = saved_values.getInt("menuid",-1);
	         String restidInString = String.valueOf(restid);
	         String submenuname = null;
		 EDCWebservice serviceObject = new EDCWebservice(getActivity());
			try{
				
				listOfcart = serviceObject.readCart();
				
				if(listOfcart==null)
					return rootView;
				restObject = serviceObject.ReadRestaurantDataOnId(restidInString);
				
				
				// SharedPreferences.Editor editor=saved_values.edit();
	     	       // editor.putString("submenuname",submenuname);
			
			//assign restaurant parameteres
		int total=0;
				 for(int i=0;i<listOfcart.size();i++)
				   {
					 total = total + listOfcart.get(i).getCount()*listOfcart.get(i).getPrice();
					 
				   }
				   
			 ((TextView)rootView.findViewById(R.id.restaurantNameInCart)).setText("RESTAURANT:"+restObject.getName());
			
			 ((TextView)rootView.findViewById(R.id.prietotal)).setText("TOTAL PAYABLE AMOUNT Rs "+total);

			
			
			 
//		 Button b21=(Button)rootView.findViewById(R.id.button21);
//			b21.setOnClickListener(new View.OnClickListener() {
//			   @Override
//			   public void onClick(View v) {  
//			   
//			   
//			    //showToast("Button Clicked");
//			  
//			
//			//
//			FragmentManager fragmentManager = getFragmentManager();
//			ThirdfragActivity newFragment = new ThirdfragActivity();
//	        
//	        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//	        // Replace whatever is in the fragment_container view with this fragment,
//	        // and add the transaction to the back stack so the user can navigate back
//	        transaction.replace(R.id.container, newFragment);
//	        transaction.addToBackStack(null);
//
//	        // Commit the transaction
//	        transaction.commit();
//			
//			//
//			   }
//			  });
		 TableLayout tbl;
			tbl = (TableLayout) rootView.findViewById(R.id.tableforcart);
		 for(int i=0;i<listOfcart.size();i++)
		   {
			
		       // Inflate your row "template" and fill out the fields.
		      // ((ImageView)row.findViewById(R.id.restImg)).setImageDrawable(R.drawable.samplerest)
			 TableRow row = (TableRow)LayoutInflater.from(getActivity()).inflate(R.layout.cart_row, null);
			 
			 String value = " = "+(listOfcart.get(i).getCount()*listOfcart.get(i).getPrice())+"";
			 String text = "     "+listOfcart.get(i).getCount()+"X"+listOfcart.get(i).getPrice();
		   ((TextView)row.findViewById(R.id.itemname)).setText(listOfcart.get(i).getFoodname()); 
		   ((TextView)row.findViewById(R.id.priceinfo)).setText(text+value); 

		       		   		    	

		       		    	
		       row.setClickable(true);
		       row.setId(i);
		       row.setOnClickListener(tablerowOnClickListener);
		       tbl.addView(row);
		   }
		 
			}catch(Exception e){
				
				Log.d("XXX", e.getMessage());
				
			}
		 
	        return rootView;
	    }
	
	public List<SubMenu> getmenulist() {
		 return listOfmenuitem;
		
	}
	
	
	 private OnClickListener tablerowOnClickListener = new OnClickListener() {
	        public void onClick(View v) {
	            //GET TEXT HERE
	           // String currenttext = ((TextView)v).getText().toString());
	        	Activity myact = getActivity();
	        	
	        	 SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
	     	    SharedPreferences.Editor editor=saved_values.edit();
	     	    
				 //((TextView)v.findViewById(R.id.restaurantName)).getText();
	   		 EDCWebservice serviceObject = new EDCWebservice(getActivity());
	   		 int id = v.getId();
	   		 
	   	
	   		 String menuname = (String) ((TextView)v.findViewById(R.id.submenuname)).getText();
	   		 float price = Float.valueOf(((TextView)v.findViewById(R.id.itemPrice)).getText().toString());
//	     	    SubMenu submenuObj = .get(v.getId());
	     	        editor.putString("dishname",menuname);
	     	        editor.putFloat("dishprice",price);

	     	        editor.commit();
	        	
	     	       FragmentManager fragmentManager = getFragmentManager();
	     			FragmentItemselection newFragment = new FragmentItemselection();
	     	        
	     	        FragmentTransaction transaction = getFragmentManager().beginTransaction();
	     	       //transaction.setCustomAnimations(0,1);
	     	       transaction.setCustomAnimations(R.layout.slide_in_left, R.layout.slide_out_right);

	     	        // Replace whatever is in the fragment_container view with this fragment,
	     	        // and add the transaction to the back stack so the user can navigate back
	     	        transaction.replace(R.id.container, newFragment);
	     	        transaction.addToBackStack(null);

	     	        // Commit the transaction
	     	       transaction.commit();
	        }
	    }; 
	 
}
