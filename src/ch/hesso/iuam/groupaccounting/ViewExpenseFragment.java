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
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewExpenseFragment extends Fragment {
		
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
	private Expense currentExpense;
	private TextView textViewDescription;
	private TextView textViewPrice;
	private TextView textViewBody;

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
        
        textViewDescription = (TextView) view.findViewById(R.id.description);
        textViewPrice = (TextView) view.findViewById(R.id.price);
        textViewBody = (TextView) view.findViewById(R.id.body);
		
        //allow to define action bar menus for this fragment
        setHasOptionsMenu(true);

        return view;
    }

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.getItem(0).setVisible(false);
		menu.getItem(1).setVisible(false);
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
            //getActivity().setTitle(R.string.title_edit_expense);
        }

    }

    public void updateExpenseView(int position) {
    	
    	textViewDescription.setText(currentExpense.getDescription());
    	textViewPrice.setText(String.valueOf(currentExpense.getPrice())+".-");
    	textViewBody.setText(
    			"Payer \t:\t\t" + currentExpense.getPayerName() + "\n" +
    			"Date  \t:\t\t" + currentExpense.getDateToString() + "\n" +
    			"Time  \t:\t\t" + currentExpense.getTimeToString() + "\n"
    			);
    	
        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }
	
}