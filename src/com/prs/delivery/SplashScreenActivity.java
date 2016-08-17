package com.prs.delivery;


import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class SplashScreenActivity extends ActionBarActivity {
private static final int DIALOG_LOADING = 0;
EDCWebservice serviceObject;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		Thread background = new Thread() {
	          public void run() {
	               
	              try {
	                  // Thread will sleep for 5 seconds
	                  sleep(2*1000);
	                   
	                  // After 5 seconds redirect to another intent
	                 
	                  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	                  startActivity(intent);
	                  //Remove activity
	                  finish();
	                   
	              } catch (Exception e) {
	               
	              }
	          }
	      };
	       FetchXML operObj = new FetchXML();
	      // operObj.execute("");
	       
	       showDialog(DIALOG_LOADING);
	      // start thread
	     background.start();
		
	}
	@Override   
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case DIALOG_LOADING:
	        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent);          
	        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        dialog.setContentView(R.layout.loading);
	        dialog.setCancelable(true);
	        dialog.setOnCancelListener(new OnCancelListener() {             
	            @Override
	            public void onCancel(DialogInterface dialog) {
	                //onBackPressed();
	            }
	        });
	    return dialog;  

	    default:
	        return null;
	    }
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
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
	//post execute class
	
	class FetchXML extends AsyncTask<String, Void, String>{
		ProgressDialog progressDialog;
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			//
			
		
				serviceObject = new EDCWebservice(getApplicationContext());
			    try{
			    	
			   
			    	
			    serviceObject.GetAllData("",1);
//			    List<CategoryModel> categories = new ArrayList<CategoryModel>();
//			  
			    
			    
			  //Toast.makeText(getApplicationContext(), jsonFile, Toast.LENGTH_LONG).show();
			    } catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "";
					
			
			
		}
	
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
	
			    	try{
						 
			    		 Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		                  startActivity(intent);
		                  dismissDialog(DIALOG_LOADING);
						
						}
						catch(Exception e){
							
						}
						 
		}
		
	}
}
