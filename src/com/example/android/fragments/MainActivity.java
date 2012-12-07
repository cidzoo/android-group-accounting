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

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class MainActivity extends Activity
        implements HeadlinesFragment.OnHeadlineSelectedListener, OnItemSelectedListener {

    protected static Time time = new Time();
	CharSequence payer;
	int price;
	String description;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_articles);

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            HeadlinesFragment firstFragment = new HeadlinesFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }    
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
		menu.getItem(1).setVisible(false);
		return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.menu_add:
    		// Comportement du bouton "Add" pour une dépense
    		//Depense.addDepense("Burgers", 45);
    		// Create fragment and give it an argument for the selected article
    		
    		//TODO: into function
            ExpenseFragment newFragment = new ExpenseFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
            
    		return true;
    	case R.id.menu_done:
    		EditText editTextHowMuch = (EditText) findViewById(R.id.textboxHowMuch);
    		price = Integer.valueOf(editTextHowMuch.getText().toString());
    		EditText editTextWhat = (EditText) findViewById(R.id.textboxWhat);
    		description = editTextWhat.getText().toString();
	    		
	    	if (!description.isEmpty()) Expense.addExpense(time, payer, price, description);
	    	else Expense.addExpense(time, payer, price);
    	    		
    		
    		HeadlinesFragment newFragment2 = new HeadlinesFragment();
            FragmentTransaction transaction2 = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction2.replace(R.id.fragment_container, newFragment2);
            transaction2.addToBackStack(null);

            // Commit the transaction
            transaction2.commit();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }

    
    public void onExpenseSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        ExpenseFragment articleFrag = (ExpenseFragment)
                getFragmentManager().findFragmentById(R.id.article_fragment);

        if (articleFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            articleFrag.updateExpenseView(position);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            ExpenseFragment newFragment = new ExpenseFragment();
            Bundle args = new Bundle();
            args.putInt(ExpenseFragment.ARG_POSITION, position);
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
    
    /*
     * LISTENERS
     */
    
    public void showDatePickerDialog(View v) {
    	DialogFragment newTimeFragment = new TimePickerFragment();
        newTimeFragment.show(getFragmentManager(), "timePicker");
        
        DialogFragment newDateFragment = new DatePickerFragment();
        newDateFragment.show(getFragmentManager(), "datePicker");
    }
    
    public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
    	payer=(CharSequence)parent.getItemAtPosition(pos);
    }
    
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
    /*
     * INNER CLASSES
     */
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    	@Override
    	public Dialog onCreateDialog(Bundle savedInstanceState) {
    		// Use the current date as the default date in the picker
    		final Calendar c = Calendar.getInstance();
    		int year = c.get(Calendar.YEAR);
    		int month = c.get(Calendar.MONTH);
    		int day = c.get(Calendar.DAY_OF_MONTH);
    		
    		// Create a new instance of DatePickerDialog and return it
    		return new DatePickerDialog(getActivity(), this, year, month, day);
    	}
    	
    	public void onDateSet(DatePicker view, int year, int month, int day) {
    		time.year=year;
    		time.month = month;
    		time.monthDay = day;
    		Button whenButton = (Button) findViewById(R.id.btnWhen);
            whenButton.setText(time.month + "/" + time.monthDay + "/" + time.year + " - " + time.hour + ":" + time.minute);
    	}
    }
    
    public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    	
    	@Override
    	public Dialog onCreateDialog(Bundle savedInstanceState) {
    		// Use the current time as the default values for the picker
    		final Calendar c = Calendar.getInstance();
    		int hour = c.get(Calendar.HOUR_OF_DAY);
    		int minute = c.get(Calendar.MINUTE);
    		
    		// Create a new instance of TimePickerDialog and return it
    		return new TimePickerDialog(getActivity(), this, hour, minute,
    		DateFormat.is24HourFormat(getActivity()));
    	}
    	
    	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    		time.hour = hourOfDay;
    		time.minute = minute;
    		Button whenButton = (Button) findViewById(R.id.btnWhen);
            whenButton.setText(time.month + "/" + time.monthDay + "/" + time.year + " - " + time.hour + ":" + time.minute);
    	}
    }

}