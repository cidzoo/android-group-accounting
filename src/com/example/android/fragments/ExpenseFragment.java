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
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class ExpenseFragment extends Fragment implements OnItemSelectedListener, OnClickListener {
	
	ExpenseFragmentListener mListener;
	
    // The container Activity must implement this interface so the frag can deliver messages
    public interface ExpenseFragmentListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onExpenseSelected(int position);

		public void onMenuDoneCallback(Expense expense);
    }
	
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
	private Expense currentExpense;
	private Button btnDate;
	private Button btnTime;
	private Spinner spinnerMembers;
	private EditText editWhat;
	private NumberPicker npDecade;
	private NumberPicker npUnit;
	private NumberPicker npHundred;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }
        
        //Inflate the layout for this fragment        
        View view = inflater.inflate(R.layout.expense_view, container, false);
        
        /* get widgets */
    	btnDate = (Button) view.findViewById(R.id.btnDate);
    	btnTime = (Button) view.findViewById(R.id.btnTime);
    	spinnerMembers = (Spinner) view.findViewById(R.id.members_spinner);
    	npHundred = (NumberPicker) view.findViewById(R.id.numberPickerHundred);
    	npDecade = (NumberPicker) view.findViewById(R.id.numberPickerDecade);
    	npUnit = (NumberPicker) view.findViewById(R.id.numberPickerUnit);
    	editWhat = (EditText) view.findViewById(R.id.textboxWhat);
    	
        /* Time and Date */
    	btnDate.setOnClickListener((OnClickListener) this);
        btnTime.setOnClickListener((OnClickListener) this);
        
        /* Price */
        
//        String[] nums = new String[20];
//        for(int i=0; i<nums.length; i++)
//               nums[i] = Integer.toString(i);

        npHundred.setMinValue(0); npHundred.setMaxValue(9); npHundred.setWrapSelectorWheel(false); npHundred.setValue(0);
        npDecade.setMinValue(0); npDecade.setMaxValue(9); npDecade.setWrapSelectorWheel(false); npDecade.setValue(0);
        npUnit.setMinValue(0); npUnit.setMaxValue(9); npUnit.setWrapSelectorWheel(false); npUnit.setValue(0);
        
        /* Payer */
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
		    R.array.members_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinnerMembers.setAdapter(adapter);
		spinnerMembers.setOnItemSelectedListener((OnItemSelectedListener) this);
		
        //allow to define action bar menus for this fragment
        setHasOptionsMenu(true);
        
        return view;
    }

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.getItem(0).setVisible(false);
		menu.getItem(1).setVisible(true);
	}

	@Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
        	currentExpense = (Expense)Expense.getExpenses().get(args.getInt(ARG_POSITION));
            // Set article based on argument passed in
            updateExpenseView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateExpenseView(mCurrentPosition);
        }
        else{
        	currentExpense = new Expense();
        }
        
        //Setup button text
        btnDate.setText(currentExpense.getDateToString());
        btnTime.setText(currentExpense.getTimeToString());
    }

    public void updateExpenseView(int position) {
    	
    	btnDate.setText(currentExpense.getDateToString());
    	btnTime.setText(currentExpense.getTimeToString());
    	spinnerMembers.setSelection(currentExpense.getPayer(), true);
    	
    	int price = currentExpense.getPrice();
    	String strPrice = String.valueOf(price);
    	npHundred.setValue(Integer.valueOf(String.valueOf(strPrice.toCharArray()[0])));
    	npDecade.setValue(Integer.valueOf(String.valueOf(strPrice.toCharArray()[1])));
    	npUnit.setValue(Integer.valueOf(String.valueOf(strPrice.toCharArray()[2])));
    	
    	editWhat.setText(currentExpense.getDescription());
    	
        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }

    public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
    	//payer=(CharSequence)parent.getItemAtPosition(pos);
    }
    
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
        case R.id.menu_done:
        	/* Payer */
        	currentExpense.setPayerId(spinnerMembers.getSelectedItemPosition());
        	
        	/* Price */
        	currentExpense.setPrice(npHundred.getValue()*100 + npDecade.getValue()*10 + npUnit.getValue());
//    		try{
//    			currentExpense.setPrice(Integer.valueOf(editHowMuch.getText().toString()));
//    		}catch(NumberFormatException e){
//    			Toast.makeText(getActivity(), getString(R.string.toast_no_price), Toast.LENGTH_SHORT).show();
//    			return true; //consumed
//    		}
    		
    		/* Description */
    		if(editWhat.getText().length() > 0)
    			currentExpense.setDescription(editWhat.getText().toString());
        	
    		//Expense correctly fill up, pass it to listener
            mListener.onMenuDoneCallback(currentExpense);
            return true;
        
        default:
            // Not one of ours. Perform default menu processing
            return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mListener = (ExpenseFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
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
    		currentExpense.time.year=year;
    		currentExpense.time.month = month;
    		currentExpense.time.monthDay = day;
            btnDate.setText(month + "/" + day + "/" + year);
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
    		currentExpense.time.hour = hourOfDay;
    		currentExpense.time.minute = minute;
            btnTime.setText(hourOfDay + ":" + minute);
    	}
    }

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btnDate:	        
	        DialogFragment newDateFragment = new DatePickerFragment();
	        newDateFragment.show(getFragmentManager(), "datePicker");
	        break;
		case R.id.btnTime:
	        DialogFragment newTimeFragment = new TimePickerFragment();
	        newTimeFragment.show(getFragmentManager(), "timePicker");
	        break;
		}
		
	}
}