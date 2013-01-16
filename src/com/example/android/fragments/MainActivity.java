/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.fragments;

import iuam.group.accounting.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.fragments.AddExpenseFragment.ViewExpenseFragmentListener;
import com.example.android.fragments.AddParticipantDialogFragment.NewParticipantFragmentListener;
import com.example.android.fragments.HeadlinesFragment.HeadlinesFragmentListener;

public class MainActivity extends Activity implements HeadlinesFragmentListener, ViewExpenseFragmentListener, NewParticipantFragmentListener {

	private boolean modifing;
	private boolean isGroupSetupMode;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.headlines_view);

        //If first launch = add participants mode
        isGroupSetupMode=true;
        setTitle(R.string.title_setup_group);
        ((ImageView)findViewById(R.id.background_image)).setVisibility(View.GONE); 
        
        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // If activity is restored, no need to create the fragment again
            if (savedInstanceState != null) { return; }

            // Create an instance of HeadlinesFragment
            HeadlinesFragment firstFragment = new HeadlinesFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            Bundle args = new Bundle();
            args.putBoolean("isGroupSetupMode", isGroupSetupMode);
            firstFragment.setArguments(args);
            //firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment, "headlines").commit();
        }  
    }

	@Override
	public void onBackPressed() {
		FragmentManager fm = getFragmentManager();
		for(int i = fm.getBackStackEntryCount(); i > 0 ; i--) {    
			fm.popBackStack(i,FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
		super.onBackPressed();
		((HeadlinesFragment)getFragmentManager().findFragmentByTag("headlines")).update(isGroupSetupMode);
		((ImageView)findViewById(R.id.background_image)).setVisibility(View.VISIBLE);
		setTitle(R.string.title_expenses);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
		menu.getItem(0).setVisible(true);
		if(isGroupSetupMode)
			if (Participant.getDescriptionStringArray().length==0)
				menu.getItem(1).setVisible(false);
			else
				menu.getItem(1).setVisible(true);
		else
			menu.getItem(1).setVisible(false);
		return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.menu_add:
    		FragmentManager fm = getFragmentManager();
    		
    		if(isGroupSetupMode){
    			
    			AddParticipantDialogFragment addParticipantDialog = new AddParticipantDialogFragment();
    			addParticipantDialog.show(fm, "fragment_add_participant");
    		}else{
    			modifing = false;
    			((ImageView)findViewById(R.id.background_image)).setVisibility(View.GONE);
    			((ImageView)findViewById(R.id.helpImage)).setVisibility(View.GONE);
        		// Create fragment and give it an argument for the selected article        		
        		//TODO: into function
                AddExpenseFragment newFragment = new AddExpenseFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();	
    		}
    		return true;
    	
    	case R.id.menu_done:
    		setTitle(R.string.title_expenses);
    		if(isGroupSetupMode){
    			isGroupSetupMode=false;
    			item.setVisible(false);
    			((HeadlinesFragment)getFragmentManager().findFragmentByTag("headlines")).update(isGroupSetupMode);
    			((ImageView)findViewById(R.id.helpImage)).setImageResource(R.drawable.help_expenses);
        		((ImageView)findViewById(R.id.helpImage)).setVisibility(View.VISIBLE);
    		}else{
    			return super.onOptionsItemSelected(item);
    		}
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }


	public void onHeadlineSelected(int position) {
    	modifing = true;
    	
    	if(!isGroupSetupMode){
	    	// The user selected the headline of an article from the HeadlinesFragment
	    	
	        // Capture the article fragment from the activity layout
	        AddExpenseFragment expenseFragment = (AddExpenseFragment)
	                getFragmentManager().findFragmentById(R.id.article_fragment);
	
	        if (expenseFragment != null) {
	            // If article frag is available, we're in two-pane layout...
	
	            // Call a method in the ArticleFragment to update its content
	            expenseFragment.updateExpenseView(position);
	
	        } else {
	        	((ImageView)findViewById(R.id.background_image)).setVisibility(View.GONE);
	            // If the frag is not available, we're in the one-pane layout and must swap frags...
	
	            // Create fragment and give it an argument for the selected article
	            ViewExpenseFragment newFragment = new ViewExpenseFragment();
	            Bundle args = new Bundle();
	            args.putInt(AddExpenseFragment.ARG_POSITION, position);
	            newFragment.setArguments(args);
	            FragmentTransaction transaction = getFragmentManager().beginTransaction();
	
	            // Replace whatever is in the fragment_container view with this fragment,
	            // and add the transaction to the back stack so the user can navigate back
	            transaction.replace(R.id.fragment_container, newFragment);
	            transaction.addToBackStack(null);
	
	            // Commit the transaction
	            transaction.commit();
	        }
    	}
    	else{
    		FragmentManager fm = getFragmentManager();
    		AddParticipantDialogFragment addParticipantDialog = new AddParticipantDialogFragment(Participant.getParticipants().get(position));
			addParticipantDialog.show(fm, "fragment_add_participant");
    	}
    }

	@Override
	public void onMenuDoneCallback(Expense expense) {
		    	
		if(!modifing)
			Expense.addExpense(expense);
		
		((ImageView)findViewById(R.id.background_image)).setVisibility(View.VISIBLE);
        
		HeadlinesFragment newFragment2 = new HeadlinesFragment();
        FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
        
		Bundle args = new Bundle();
        args.putBoolean(AddExpenseFragment.ARG_POSITION, isGroupSetupMode);
        newFragment2.setArguments(args);

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction2.replace(R.id.fragment_container, newFragment2, "headlines");
        transaction2.addToBackStack(null);

        // Commit the transaction
        transaction2.commit();
	}

	@Override
	public void onFinishEditDialog(String inputText) {
		
		if(!modifing){
			Toast.makeText(this, inputText + " has been added to the group", Toast.LENGTH_SHORT).show();
			Participant.addParticipants(new Participant(inputText));
		}
		
		((HeadlinesFragment)getFragmentManager().findFragmentByTag("headlines")).update(isGroupSetupMode);
		
		invalidateOptionsMenu(); //to force menu refresh to display done button
		findViewById(R.id.helpImage).setVisibility(View.GONE);
	}

}