package com.prs.delivery;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Restaurantpage extends Fragment {
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		((MainActivity)getActivity()).setCartFlag("Y");
		super.onStart();
	}



	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {
		
	        // If activity recreated (such as from screen rotate), restore
	        // the previous article selection set by onSaveInstanceState().
	        // This is primarily necessary when in the two-pane layout.
	      
	        // Inflate the layout for this fragment
		 
		 View rootView = inflater.inflate(R.layout.fragment_restaurantpage, container, false);
		// restList = new ArrayList<RestaurantModel>();
		 SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity());
		 RestaurantModel restObject=null;
		 List<String> listOfmenu = new ArrayList<String>(); {
		};
		 int restid = saved_values.getInt("restid",-1);
	         String restidInString = String.valueOf(restid);
		        
		 EDCWebservice serviceObject = new EDCWebservice(getActivity());
			try{
				restObject = serviceObject.ReadRestaurantDataOnId(restidInString);
				listOfmenu = serviceObject.ReadRestaurantsAndMenu(restidInString);
			
			
			
			//assign restaurant parameteres
			 new DownloadImageTask((ImageView)rootView.findViewById(R.id.restaurantImg))
		        .execute(restObject.getLogoname());
			 
			
			 ((TextView)rootView.findViewById(R.id.restaurantName)).setText(restObject.getName());
		     ((TextView)rootView.findViewById(R.id.restaurantLoc)).setText(restObject.getLocation());
			
			
		     ImageView img = (ImageView)rootView.findViewById(R.id.imageButton3);
		     img.setOnClickListener(new OnClickListener() {
		         public void onClick(View v) {
		        	 
		        	 FragmentManager fragmentManager = getFragmentManager();
		 			Shoppingcart newFragment = new Shoppingcart();
		 	        
		 	        FragmentTransaction transaction = getFragmentManager().beginTransaction();

		 	        // Replace whatever is in the fragment_container view with this fragment,
		 	        // and add the transaction to the back stack so the user can navigate back
		 	        transaction.replace(R.id.container, newFragment);
		 	        transaction.addToBackStack(null);

		 	        // Commit the transaction
		 	        transaction.commit();
		            // your code here
		         }
		     });
			
			 
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
			tbl = (TableLayout) rootView.findViewById(R.id.tableforsubmenu);
		 for(int i=0;i<listOfmenu.size();i++)
		   {
			
		       // Inflate your row "template" and fill out the fields.
		      // ((ImageView)row.findViewById(R.id.restImg)).setImageDrawable(R.drawable.samplerest)
			 TableRow row = (TableRow)LayoutInflater.from(getActivity()).inflate(R.layout.menu_row, null);
		       		       ((TextView)row.findViewById(R.id.submenuname)).setText(listOfmenu.get(i));
		       
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
	
	
	
	 /* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		((MainActivity)getActivity()).setCartFlag("N");
		super.onDestroyView();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		((MainActivity)getActivity()).setCartFlag("N");
		super.onDetach();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
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
	     	        editor.putInt("menuid",v.getId());
	     	        String submenuname =  ((TextView)v.findViewById(R.id.submenuname)).getText().toString();
	     	       editor.putString("submenuname",submenuname);
	     	        editor.commit();
	        	
	     	       FragmentManager fragmentManager = getFragmentManager();
	     			Foodmenu newFragment = new Foodmenu();
	     	        
	     	        FragmentTransaction transaction = getFragmentManager().beginTransaction();
	     	       //transaction.setCustomAnimations(0,1);
	     	        // Replace whatever is in the fragment_container view with this fragment,
	     	        // and add the transaction to the back stack so the user can navigate back
	     	        transaction.replace(R.id.container, newFragment);
	     	        transaction.addToBackStack(null);

	     	        // Commit the transaction
	     	       transaction.commit();
	        }
	    }; 
	    
	    
	
	}
