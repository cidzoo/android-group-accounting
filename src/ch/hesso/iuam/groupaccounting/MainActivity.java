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
package ch.hesso.iuam.groupaccounting;

import iuam.group.accounting.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import ch.hesso.iuam.groupaccounting.AddExpenseFragment.ViewExpenseFragmentListener;
import ch.hesso.iuam.groupaccounting.AddMemberDialogFragment.NewMemberFragmentListener;
import ch.hesso.iuam.groupaccounting.HeadlinesFragment.HeadlinesFragmentListener;

public class MainActivity extends Activity implements HeadlinesFragmentListener, ViewExpenseFragmentListener, NewMemberFragmentListener {

	private boolean modifing;
	private boolean isGroupSetupMode;
	PopupWindow popUp;
	LinearLayout layout;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.headlines_view);

        //If first launch = add participants mode
        isGroupSetupMode=true;
        setTitle(R.string.title_setup_group);
        
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
        
        /*
         * Allow to display an help overlay for first run
         */
        
        popUp = new PopupWindow(this);
        layout = new LinearLayout(this);
        
        findViewById(android.R.id.content).post( new Runnable() {

            @SuppressWarnings("deprecation")
			@Override
            public void run() {
                layout.setBackgroundResource(R.drawable.help_group);
                popUp.setContentView(layout);
                popUp.setBackgroundDrawable(null);
                popUp.setOutsideTouchable(true);
                popUp.setBackgroundDrawable(new BitmapDrawable());
                
            	popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
        		
        		Display display = getWindowManager().getDefaultDisplay();
        		Point size = new Point();
        		display.getSize(size);
        		int width = size.x;
        		int height = size.y;
        		
        		popUp.update(0, 0, width, height);
        				
        		popUp.setTouchInterceptor(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						popUp.dismiss();
						return false;
					}
				});
            }
        } );
        
        
    }

	@Override
	public void onBackPressed() {
		FragmentManager fm = getFragmentManager();
		
		for(int i = fm.getBackStackEntryCount(); i > 0 ; i--) {    
			fm.popBackStack(i,FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
		super.onBackPressed();
		((HeadlinesFragment)getFragmentManager().findFragmentByTag("headlines")).update(isGroupSetupMode);
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
		menu.findItem(R.id.menu_add).setVisible(true);
		if(isGroupSetupMode){
			menu.findItem(R.id.menu_add).setIcon(R.drawable.ic_member_add);
			if (Member.getDescriptionStringArray().length==0){
				menu.findItem(R.id.menu_done).setVisible(false);
			}else{
				menu.findItem(R.id.menu_done).setVisible(true);
			}
		}else{
			menu.findItem(R.id.menu_add).setIcon(R.drawable.ic_content_new);
			menu.findItem(R.id.menu_done).setVisible(false);
		}
		return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	FragmentManager fm = getFragmentManager();
    	
    	switch (item.getItemId()) {
    	case R.id.menu_add:
    		
    		if(isGroupSetupMode){
    			
    			AddMemberDialogFragment addParticipantDialog = new AddMemberDialogFragment();
    			addParticipantDialog.show(fm, "fragment_add_participant");
    		}else{
    			modifing = false;
        		// Create fragment and give it an argument for the selected article        		
        		//TODO: into function
                AddExpenseFragment newFragment = new AddExpenseFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

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
    			invalidateOptionsMenu();
    			((HeadlinesFragment)getFragmentManager().findFragmentByTag("headlines")).update(isGroupSetupMode);
        		
        		//Show help popup
        		layout.setBackgroundResource(R.drawable.help_expenses);
        		popUp.showAtLocation(layout, Gravity.CENTER, 0, 0);

    		}else{
    			return super.onOptionsItemSelected(item);
    		}
    		return true;
    	
    	case R.id.menu_settings:
//    		getFragmentManager().beginTransaction()
//            .replace(android.R.id.content, new SettingsFragment())
//            .commit();
    		SettingsFragment newFragment = new SettingsFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();	
    		return true;
    		
    	case R.id.menu_about:
    		AboutDialogFragment aboutDialog = new AboutDialogFragment();
    		aboutDialog.show(fm, "fragment_about");
    		return true;
    		
    	case R.id.menu_help:
    		HelpDialogFragment helpDialog = new HelpDialogFragment();
    		helpDialog.show(fm, "fragment_help");
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }


	public void onHeadlineSelected(int position) {
    	
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
	            // If the frag is not available, we're in the one-pane layout and must swap frags...
	
	            // Create fragment and give it an argument for the selected article
	            ViewExpenseFragment newFragment = new ViewExpenseFragment();
	            Bundle args = new Bundle();
	            args.putInt(AddExpenseFragment.ARG_POSITION, position);
	            newFragment.setArguments(args);
	            FragmentTransaction transaction = getFragmentManager().beginTransaction();
	            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	
	            // Replace whatever is in the fragment_container view with this fragment,
	            // and add the transaction to the back stack so the user can navigate back
	            transaction.replace(R.id.fragment_container, newFragment);
	            transaction.addToBackStack(null);
	
	            // Commit the transaction
	            transaction.commit();
	        }
    	}

    }
	
	public void onHeadlineEdit(int position) {
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
	            // If the frag is not available, we're in the one-pane layout and must swap frags...
	
	            // Create fragment and give it an argument for the selected article
	            AddExpenseFragment newFragment = new AddExpenseFragment();
	            Bundle args = new Bundle();
	            args.putInt(AddExpenseFragment.ARG_POSITION, position);
	            newFragment.setArguments(args);
	            FragmentTransaction transaction = getFragmentManager().beginTransaction();
	            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	
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
    		AddMemberDialogFragment addParticipantDialog = new AddMemberDialogFragment(Member.getMembers().get(position));
			addParticipantDialog.show(fm, "fragment_add_participant");
    	}
    }

	@Override
	public void onMenuDoneCallback(Expense expense) {
		    	
		if(!modifing)
			Expense.addExpense(expense);
        
		HeadlinesFragment newFragment2 = new HeadlinesFragment();
        FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
        transaction2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        
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
			Member.addMembers(new Member(inputText));
		}
		
		((HeadlinesFragment)getFragmentManager().findFragmentByTag("headlines")).update(isGroupSetupMode);
		
		invalidateOptionsMenu(); //to force menu refresh to display done button
	}

	public boolean isGroupSetupMode() {
		return isGroupSetupMode;
	}

}