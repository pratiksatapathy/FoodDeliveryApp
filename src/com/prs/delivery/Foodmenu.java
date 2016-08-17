package com.prs.delivery;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Foodmenu extends Fragment {
	final List<SubMenu> listOfmenuitem = new ArrayList<SubMenu>();
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {

	        // If activity recreated (such as from screen rotate), restore
	        // the previous article selection set by onSaveInstanceState().
	        // This is primarily necessary when in the two-pane layout.
	      
	        // Inflate the layout for this fragment
		 
		 final View rootView = inflater.inflate(R.layout.fragment_foodmenu, container, false);
		// restList = new ArrayList<RestaurantModel>();
		 SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity());
		 RestaurantModel restObject=null;
		 List<String> listOfmenu = new ArrayList<String>();
		 List<SubMenu> listOfmenuitem = new ArrayList<SubMenu>();
		 {
		};
		 int restid = saved_values.getInt("restid",-1);
		 int menuid = saved_values.getInt("menuid",-1);
	         String restidInString = String.valueOf(restid);
	         String submenuname = null;
		 EDCWebservice serviceObject = new EDCWebservice(getActivity());
			try{
				restObject = serviceObject.ReadRestaurantDataOnId(restidInString);
				listOfmenu = serviceObject.ReadRestaurantsAndMenu(restidInString);
				submenuname = listOfmenu.get(menuid);
				//submenuname = saved_values.getString("submenuname","");
				 SharedPreferences.Editor editor=saved_values.edit();
	     	        editor.putString("submenuname",submenuname);
	     	        editor.commit();
				listOfmenuitem = serviceObject.ReadRestaurantMenuForSubmenu(restidInString, submenuname);
			
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
		 for(int i=0;i<listOfmenuitem.size();i++)
		   {
			
		       // Inflate your row "template" and fill out the fields.
		      // ((ImageView)row.findViewById(R.id.restImg)).setImageDrawable(R.drawable.samplerest)
			 final TableRow row = (TableRow)LayoutInflater.from(getActivity()).inflate(R.layout.attrib_row, null);
		       		       ((TextView)row.findViewById(R.id.submenuname)).setText(listOfmenuitem.get(i).getName());
		       		    ((TextView)row.findViewById(R.id.itemPrice)).setText(String.valueOf(listOfmenuitem.get(i).getPrice()));
		       		
		       		    if((listOfmenuitem.get(i).gettype().equals("1")))
		       		    		
		       		    ((ImageView)row.findViewById(R.id.imageView1)).setImageResource(R.drawable.non_veg);
		       		    
		       		    else{
		       		    	
			       		    ((ImageView)row.findViewById(R.id.imageView1)).setImageResource(R.drawable.veg);

		       		    	
		       		    }
		       		  int foodcount =serviceObject.getcount(listOfmenuitem.get(i).getName());
		       		  
		       		  if(foodcount!=-1)
			       		 ((TextView)row.findViewById(R.id.wotax)).setText(String.valueOf(foodcount));

		       		  
		       		  
		       		 ((TextView)row.findViewById(R.id.description)).setText(listOfmenuitem.get(i).getDesc());
		       		 ((ImageButton)row.findViewById(R.id.imageButton2)).setOnClickListener(new View.OnClickListener() {
					        @Override
					        public void onClick(View v) {
					        	int count = Integer.parseInt((String)((TextView)row.findViewById(R.id.wotax)).getText());

					        	
					        	count++;
					        	String countval = String.valueOf(count);
					        	((TextView)row.findViewById(R.id.wotax)).setText(countval);
					        	//((TextView)v.findViewById(R.id.tax)).setText(String.valueOf((Float.valueOf(countval)*dishprice)));
					        	 //SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(get);
					    		 
					 	        //String restname = saved_values.getString("restname", "none");
						        //String submenuheading = saved_values.getString("submenuname", "none");
					 	        //String foodname = saved_values.getString("submenufoodname", "none");
					 	        //int price = saved_values.getInt("priceforfood",-1);

					 	       
					 	     int foodcount = Integer.parseInt((String) ((TextView)row.findViewById(R.id.wotax)).getText());
					        
					 	    SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity());
			        		
				    		  String dishnameC = ((TextView)row.findViewById(R.id.submenuname)).getText().toString();
				 	   		 float price = Float.valueOf(((TextView)row.findViewById(R.id.itemPrice)).getText().toString());

				    		  int restidC = saved_values.getInt("restid",0);
				    		  String submenunameC = saved_values.getString("submenuname","");
				    		 int countC =  Integer.valueOf((String) ((TextView)row.findViewById(R.id.wotax)).getText());
				        	try {
				        		EDCWebservice serviceObject = new EDCWebservice(getActivity());
								serviceObject.upsert(String.valueOf(restidC), submenunameC, dishnameC, countC, (int) price);
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        	
				        	ImageView image = (ImageView)rootView.findViewById(R.id.imageButton3);
				        	final Animation animation = new AlphaAnimation((float) 0.5, 0); // Change alpha from fully visible to invisible
				            animation.setDuration(200); // duration - half a second
				            animation.setInterpolator(new LinearInterpolator()); // do not alter
				                                                                    // animation
				            animation.setRepeatCount(2);                        // rate
				           // animation.setRepeatCount(Animation.INFINITE); // Repeat animation
				                                                            // infinitely
				            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
				                                                        // end so the button will
				                                                        // fade back in
				            image.startAnimation(animation);
					        }
					    });	    
						
					 
					 
					 ((ImageButton)row.findViewById(R.id.imageButton1)).setOnClickListener(new View.OnClickListener() {
					        @Override
					        public void onClick(View v) {
					        	
					        	int count =Integer.parseInt((String) ((TextView)row.findViewById(R.id.wotax)).getText());
					        	count--;
					        	if(count<0)count=0;
					        	String countval = String.valueOf(count);
					        	((TextView)row.findViewById(R.id.wotax)).setText(countval);
					        	//((TextView)v.findViewById(R.id.tax)).setText(String.valueOf((Float.valueOf(countval)*dishprice)));

					        	//b.show();
					        	
					        	  int foodcount = Integer.parseInt((String) ((TextView)row.findViewById(R.id.wotax)).getText());
							        
							 	    SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity());
					        		
						    		  String dishnameC = ((TextView)row.findViewById(R.id.submenuname)).getText().toString();
						 	   		 float price = Float.valueOf(((TextView)row.findViewById(R.id.itemPrice)).getText().toString());

						    		  int restidC = saved_values.getInt("restid",0);
						    		  String submenunameC = saved_values.getString("submenuname","");
						    		 int countC =  Integer.valueOf((String) ((TextView)row.findViewById(R.id.wotax)).getText());
						        	try {
						        		EDCWebservice serviceObject = new EDCWebservice(getActivity());
										serviceObject.upsert(String.valueOf(restidC), submenunameC, dishnameC, countC, (int) price);
									} catch (ClientProtocolException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}					        
						        	ImageView image = (ImageView)rootView.findViewById(R.id.imageButton3);
						        	final Animation animation = new AlphaAnimation((float) 0.5, 0); // Change alpha from fully visible to invisible
						            animation.setDuration(200); // duration - half a second
						            animation.setInterpolator(new LinearInterpolator()); // do not alter
						                                                                    // animation
						            animation.setRepeatCount(2);                        // rate
						           // animation.setRepeatCount(Animation.INFINITE); // Repeat animation
						                                                            // infinitely
						            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
						                                                        // end so the button will
						                                                        // fade back in
					        
					        }
					    });	  
			   		 
			   		 
			   		 
			   		 
			   		 
			   		 
		       		 
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
	     	    
				 //((TextView)v.findViewById(R.id.restaurantName)).getText();
	   		 EDCWebservice serviceObject = new EDCWebservice(getActivity());
	   		 int id = v.getId();
	   		 
	   		 
	   		 // action listner for inrow objects
	   		 
	   		 
	   	
	   		
	   		 
	   		 
	   		 
	   		 
	   		 
	   		 //
	   		 
	   		 String menuname = (String) ((TextView)v.findViewById(R.id.submenuname)).getText();
	   		 float price = Float.valueOf(((TextView)v.findViewById(R.id.itemPrice)).getText().toString());
//	     	    SubMenu submenuObj = .get(v.getId());
	     	        editor.putString("dishname",menuname);
	     	        editor.putFloat("dishprice",price);

	     	        editor.commit();
	        	
//	     	       FragmentManager fragmentManager = getFragmentManager();
//	     			FragmentItemselection newFragment = new FragmentItemselection();
//	     	        
//	     	        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//	     	       //transaction.setCustomAnimations(0,1);
//
//	     	        // Replace whatever is in the fragment_container view with this fragment,
//	     	        // and add the transaction to the back stack so the user can navigate back
//	     	        transaction.replace(R.id.container, newFragment);
//	     	        transaction.addToBackStack(null);
//
//	     	        // Commit the transaction
//	     	       transaction.commit();
	        }
	    }; 
	 
}
