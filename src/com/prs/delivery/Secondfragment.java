package com.prs.delivery;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Secondfragment extends Fragment {
	List<RestaurantModel> restList;
	public String rest = "";
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {

	        // If activity recreated (such as from screen rotate), restore
	        // the previous article selection set by onSaveInstanceState().
	        // This is primarily necessary when in the two-pane layout.
	      
	        // Inflate the layout for this fragment
		 
		 View rootView = inflater.inflate(R.layout.activity_secondfragment, container, false);
		 restList = new ArrayList<RestaurantModel>();
		 SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity());
		 RestaurantModel restObject=null;
	        String location = saved_values.getString("location","");
	        
	        TextView tvObj = (TextView)rootView.findViewById(R.id.itemPrice);
	        
	        if(rest.equalsIgnoreCase("all"))
	        tvObj.setText("Showing all restaurants");
	        else
	        	tvObj.setText("Showing restaurants in : "+location);
		 EDCWebservice serviceObject = new EDCWebservice(getActivity());
			try{
				
				if(rest.equalsIgnoreCase("all"))
					restList = serviceObject.ReadRestaurantsDataOnLocation();
				else
					restList = serviceObject.ReadRestaurantsDataOnLocation(location);

			}
			catch(Exception e){
				
			}
		 
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
			tbl = (TableLayout) rootView.findViewById(R.id.table1);
		 for(int i=0;i<restList.size();i++)
		   {
			 RestaurantModel rObj = restList.get(i);
		       // Inflate your row "template" and fill out the fields.
		       TableRow row = (TableRow)LayoutInflater.from(getActivity()).inflate(R.layout.my_table_row, null);
		      // ((ImageView)row.findViewById(R.id.restImg)).setImageDrawable(R.drawable.samplerest)
		       		       ((TextView)row.findViewById(R.id.restName)).setText(rObj.getName());
		       ((TextView)row.findViewById(R.id.restLoc)).setText(rObj.getLocation());
		      // ((ImageView)row.findViewById(R.id.re)).setText(r.getLocation());
		       
		       new DownloadImageTask((ImageView)row.findViewById(R.id.restImg))
		        .execute(rObj.getLogoname());
		       
		       row.setClickable(true);
		       row.setId(rObj.getId());
		       row.setOnClickListener(tablerowOnClickListener);
		       tbl.addView(row);
		   }
		 
		 
		 
	        return rootView;
	    }

	    @Override
	    public void onStart() {
	        super.onStart();

	        // During startup, check if there are arguments passed to the fragment.
	        // onStart is a good place to do this because the layout has already been
	        // applied to the fragment at this point so we can safely call the method
	        // below that sets the article text.
	       
	    }

	    public void updateArticleView(int position) {
	       // TextView article = (TextView) getActivity().findViewById(R.id.article2);
	       // article.setText("new frag replaces fragment");
	      //  mCurrentPosition = position;
	    }

	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);

	        // Save the current article selection in case we need to recreate the fragment
	       // outState.putInt(ARG_POSITION, mCurrentPosition);
	    }
	    
	    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	        ImageView bmImage;

	        public DownloadImageTask(ImageView bmImage) {
	            this.bmImage = bmImage;
	        }

	        protected Bitmap doInBackground(String... urls) {
	            String urldisplay = urls[0];
	            urldisplay= "http://deliverydoots.com/"+urldisplay;
	            Bitmap mIcon11 = null;
	            try {
	                InputStream in = new java.net.URL(urldisplay).openStream();
	                mIcon11 = BitmapFactory.decodeStream(in);
	            } catch (Exception e) {
	                Log.e("Error", e.getMessage());
	                e.printStackTrace();
	            }
	            return mIcon11;
	        }

	        protected void onPostExecute(Bitmap result) {
	            bmImage.setImageBitmap(result);
	        }    
	    }
	    private OnClickListener tablerowOnClickListener = new OnClickListener() {
	        public void onClick(View v) {
	            //GET TEXT HERE
	           // String currenttext = ((TextView)v).getText().toString());
	        	Activity myact = getActivity();
	        	
	        	 SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
	     	    SharedPreferences.Editor editor=saved_values.edit();
	     	        editor.putInt("restid",v.getId());
	     	        editor.commit();
	        	
	     	       FragmentManager fragmentManager = getFragmentManager();
	     			Restaurantpage newFragment = new Restaurantpage();
	     	        
	     	        FragmentTransaction transaction = getFragmentManager().beginTransaction();
	     	        // Replace whatever is in the fragment_container view with this fragment,
	     	        // and add the transaction to the back stack so the user can navigate back
	     	        transaction.replace(R.id.container, newFragment);
	     	        transaction.addToBackStack(null);

	     	        // Commit the transaction
	     	       transaction.commit();
	        }
	    }; 
	    
}
