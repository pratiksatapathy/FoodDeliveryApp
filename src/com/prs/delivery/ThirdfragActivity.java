package com.prs.delivery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ThirdfragActivity extends Fragment {

	 public View onCreateView(LayoutInflater inflater, ViewGroup container, 
		        Bundle savedInstanceState) {

		        // If activity recreated (such as from screen rotate), restore
		        // the previous article selection set by onSaveInstanceState().
		        // This is primarily necessary when in the two-pane layout.
		      
		        // Inflate the layout for this fragment
			 
			 View rootView = inflater.inflate(R.layout.fragment_thirdfrag, container, false);
			 
			
			 
			 
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
}
