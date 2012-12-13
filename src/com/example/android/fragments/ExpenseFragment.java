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

import com.example.android.fragments.HeadlinesFragment.HeadlinesFragmentListener;

import iuam.group.accounting.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ExpenseFragment extends Fragment implements OnItemSelectedListener {
	
	ExpenseFragmentListener mListener;
	
    // The container Activity must implement this interface so the frag can deliver messages
    public interface ExpenseFragmentListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onExpenseSelected(int position);

		public void onMenuDoneCallback(Expense expense);
    }
	
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
	private CharSequence payer;
	private Expense currentExpense;

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
        
        Spinner spinner = (Spinner) view.findViewById(R.id.members_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
		    R.array.members_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener((OnItemSelectedListener) this);
		
        //allow to define action bar menus for this fragment
        setHasOptionsMenu(true);
        
        return view;
    }
    
//    @Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		// TODO Auto-generated method stub
//		super.onCreateOptionsMenu(menu, inflater);
//		
//	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
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
        Button whenButton = (Button) getView().findViewById(R.id.btnWhen);
        whenButton.setText(currentExpense.getTimeToString());
    }

    public void updateExpenseView(int position) {
    	
    	Button btnWhen = (Button) getActivity().findViewById(R.id.btnWhen);
    	Spinner spinnerMembers = (Spinner) getActivity().findViewById(R.id.members_spinner);
    	EditText textboxHowMuch = (EditText) getActivity().findViewById(R.id.textboxHowMuch);
    	EditText textboxWhat = (EditText) getActivity().findViewById(R.id.textboxWhat);
    	
    	btnWhen.setText(currentExpense.getTimeToString());
    	//FIXME spinnerMembers.setId(1);
    	textboxHowMuch.setText(String.valueOf(currentExpense.getPrice()));
    	textboxWhat.setText(currentExpense.getDescription());
    	
        TextView article = (TextView) getActivity().findViewById(R.id.article);
        //article.setText(ex.toString());
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
    	payer=(CharSequence)parent.getItemAtPosition(pos);
    }
    
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_done:
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
}