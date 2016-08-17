package com.prs.delivery;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Payment extends Fragment {

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
			
		 
	        return rootView;
	    }
	
}
