package com.prs.delivery;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentItemselection extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {

		 
		 final View rootView = inflater.inflate(R.layout.fragment_itemselection, container, false);
		// restList = new ArrayList<RestaurantModel>();
		 SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity());
	
		 final String dishname = saved_values.getString("dishname","");
		 final int restid = saved_values.getInt("restid",0);
		 final int count = 0;
		 final String submenuname = saved_values.getString("submenuname","");
		 int prevCount;
		 final Float dishprice = saved_values.getFloat("dishprice",(float) 0.0);
		 int menuid = saved_values.getInt("menuid",-1);
	         String restidInString = String.valueOf(restid);
		 final EDCWebservice serviceObject = new EDCWebservice(getActivity());
		
			//assign restaurant parameteres
  		  try {
  			prevCount =serviceObject.getcount(dishname);
  			
  			if(prevCount!=-1)
  			  
  			{	 ((TextView)rootView.findViewById(R.id.wotax)).setText(String.valueOf(prevCount));
  				 ((TextView)rootView.findViewById(R.id.TextView01)).setText(String.valueOf(prevCount*dishprice));

  			}
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

			 
		 ((TextView)rootView.findViewById(R.id.TextView01)).setText("");
			 ((TextView)rootView.findViewById(R.id.textView1)).setText(dishname);
			 ((TextView)rootView.findViewById(R.id.tax)).setText("0");

			 ((Button)rootView.findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View v) {
			        	
			        	
			        	
			        	
			        	
			        	
			        	
			        	
			        	
			        	
			        	Animation anim = AnimationUtils.loadAnimation(getActivity(), R.layout.fly);
			        	
			        	
			        	
			        	((TextView)rootView.findViewById(R.id.TextView01)).setText(((TextView)rootView.findViewById(R.id.tax)).getText());
			        	((TextView)rootView.findViewById(R.id.TextView01)).startAnimation(anim);
			        	
			        	
			        	
			        	
			        	
			        	 SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity());
			        		
			    		  String dishnameC = saved_values.getString("dishname","");
			    		  int restidC = saved_values.getInt("restid",0);
			    		  String submenunameC = saved_values.getString("submenuname","");
			    		  final Float dishpriceC = saved_values.getFloat("dishprice",(float) 0.0);
			    		 int countC =  Integer.valueOf((String) ((TextView)rootView.findViewById(R.id.wotax)).getText());
			        	try {
							serviceObject.upsert(String.valueOf(restidC), submenunameC, dishnameC, countC, dishpriceC.intValue());
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
			            animation.setRepeatCount(5);                        // rate
			           // animation.setRepeatCount(Animation.INFINITE); // Repeat animation
			                                                            // infinitely
			            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
			                                                        // end so the button will
			                                                        // fade back in
			            image.startAnimation(animation);
			        	 Toast.makeText(getActivity(), "Item Added to your cart", 100).show();
			        	 
			        	 
			        	 
			        	 
			        	 new Timer().schedule(new TimerTask() {          
			        		    @Override
			        		    public void run() {
			        		        // this code will be executed after 2 seconds    
			        		    	getFragmentManager().popBackStack();
			        		    }
			        		}, 1900);
			        	// update cart
			        	
			        	//getFragmentManager().popBackStack();        
			        	}
			 });
			 //Animation anim = AnimationUtils.loadAnimation(this, R.anim.my_anim);
			// button.startAnimation(anim);
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
			 ((ImageButton)rootView.findViewById(R.id.imageButton2)).setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View v) {
			        	int count = Integer.parseInt((String)((TextView)rootView.findViewById(R.id.wotax)).getText());

			        	
			        	count++;
			        	String countval = String.valueOf(count);
			        	((TextView)rootView.findViewById(R.id.wotax)).setText(countval);
			        	((TextView)rootView.findViewById(R.id.tax)).setText(String.valueOf((Float.valueOf(countval)*dishprice)));
			        	 //SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(get);
			    		 
			 	        //String restname = saved_values.getString("restname", "none");
				        //String submenuheading = saved_values.getString("submenuname", "none");
			 	        //String foodname = saved_values.getString("submenufoodname", "none");
			 	        //int price = saved_values.getInt("priceforfood",-1);

			 	       
			 	     int foodcount = Integer.parseInt((String) ((TextView)rootView.findViewById(R.id.wotax)).getText());
			        }
			    });	    
				
			 
			 
			 ((ImageButton)rootView.findViewById(R.id.imageButton1)).setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View v) {
			        	
			        	int count =Integer.parseInt((String) ((TextView)rootView.findViewById(R.id.wotax)).getText());
			        	count--;
			        	if(count<0)count=0;
			        	String countval = String.valueOf(count);
			        	((TextView)rootView.findViewById(R.id.wotax)).setText(countval);
			        	((TextView)rootView.findViewById(R.id.tax)).setText(String.valueOf((Float.valueOf(countval)*dishprice)));

			        	//b.show();
			        	
				 	     int foodcount = Integer.parseInt((String) ((TextView)rootView.findViewById(R.id.wotax)).getText());
			        }
			    });	    

				
			

		 return rootView;
	}
	
	
}
