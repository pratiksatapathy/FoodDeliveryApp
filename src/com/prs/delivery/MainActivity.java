package com.prs.delivery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		

NavigationDrawerFragment.NavigationDrawerCallbacks {
	public String cleanCart="";

    public String getCartFlag() {
        return cleanCart;
    }

    public void setCartFlag(String flag) {
        this.cleanCart = flag;
    }
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	public void openDraw(View v) {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		drawer.openDrawer(Gravity.LEFT);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		drawer.closeDrawer(Gravity.LEFT);
		
		
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		EDCWebservice edcObj = new EDCWebservice(getApplicationContext());
		
		int size=0;
		
		try {
			size = edcObj.readCart().size();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(this.cleanCart.equals("Y") && size!=0){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MainActivity.this);
 
			// set title
			alertDialogBuilder.setTitle("Warning");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("You will loose your food basket if you go out of current restaurant menu. Do you want to go back ?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						
						EDCWebservice edcObj = new EDCWebservice(getApplicationContext());
						edcObj.deleteCart();
						 MainActivity.super.onBackPressed();
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						
						dialog.cancel();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	
		}
		else{super.onBackPressed();}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		AlertDialog.Builder city;
		AlertDialog.Builder location;
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			List<String> locationList = new ArrayList<String>();
			city = new Builder(getActivity());
			location = new Builder(getActivity());
			EDCWebservice serviceObject = new EDCWebservice(getActivity());
			try {
				locationList = serviceObject.readLocations();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			city.setTitle("Choose Locality");
			location.setTitle("Choose Locality");
			final String[] arrForLocation = locationList
					.toArray(new String[locationList.size()]);
			final String[] arrForCity = { "Bhubaneswar", "Puri" };
			
			
			
			city.setItems(arrForCity, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Button b = (Button)rootView.findViewById(R.id.button1);
					b.setText(arrForCity[which]);
					dialog.dismiss();
					// Intent i=new
					
					// Intent(getBaseContext(),Restaurantactivity.class);
					// i.putExtra("index",which);
					// i.putExtra("type", "loc");
					// startActivity(i);
				}

			});
			location.setItems(arrForLocation, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					Button b = (Button)rootView.findViewById(R.id.Button2);
					b.setText(arrForLocation[which]);
					dialog.dismiss();
					// Intent i=new
					// Intent(getBaseContext(),Restaurantactivity.class);
					// i.putExtra("index",which);
					// i.putExtra("type", "loc");
					// startActivity(i);
				}

			});
			Button b = (Button) rootView.findViewById(R.id.button1);
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					city.show();

				}
			});

			
			
			
			Button b1 = (Button) rootView.findViewById(R.id.Button2);
			b1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					location.show();

				}
			});
			
			//
			
			ImageView iv = (ImageView) rootView.findViewById(R.id.imageView1);
			 SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
	     	    SharedPreferences.Editor editor=saved_values.edit();
	     	   int fr = saved_values.getInt("firstrun",-1);
	     	   
	     	   if(fr==1)
	     	   {
	     		   iv.setVisibility(View.GONE);
	     	   }
	     	  iv.setOnTouchListener(new OnTouchListener()
		        {

		            @Override
		            public boolean onTouch(View v, MotionEvent event)
		            {
		               v.setVisibility(View.GONE);
		               SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
			     	    SharedPreferences.Editor editor=saved_values.edit();
			     	        editor.putInt("firstrun",1);
			     	        editor.commit();
		                return false;
		            }
		       });
	     	   
			//
			
			
			
			Button submit = (Button) rootView.findViewById(R.id.buttonclicker);
			submit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					
					 SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
			     	    SharedPreferences.Editor editor=saved_values.edit();
			     	    
			     	    
			     	   Button locButton = (Button) rootView.findViewById(R.id.Button2);
			     	   
			     	   if(((String)locButton.getText()).equals("Location"))
			     	   {
			     		   Toast.makeText(getActivity(), "Please select location to proceed", 3000).show();
			     		   return;
			     	   }
			     	        editor.putString("location",(String) locButton.getText());
			     	        editor.commit();
					
					
					 FragmentManager fragmentManager = getFragmentManager();
					 Secondfragment newFragment = new Secondfragment();
					
					 FragmentTransaction transaction =
					 getFragmentManager().beginTransaction();
					
					 // Replace whatever is in the fragment_container view with this
					 //fragment,
					 // and add the transaction to the back stack so the user can
					 //navigate back
					 transaction.replace(R.id.container, newFragment);
					 transaction.addToBackStack(null);
					
					 // Commit the transaction
					 transaction.commit();

				}
			});
			

			//

			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
