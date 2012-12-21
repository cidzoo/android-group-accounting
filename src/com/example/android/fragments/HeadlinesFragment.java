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
import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HeadlinesFragment extends ListFragment {
    HeadlinesFragmentListener mListener;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface HeadlinesFragmentListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onExpenseSelected(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        update(getArguments().getBoolean("isGroupSetupMode"));
        //This has menus
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }
    
    

//    @Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		
//    	return inflater.inflate(R.layout.news_articles, container, false);
//	}

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mListener = (HeadlinesFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    
//    @Override
//	public void onPrepareOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		menu.getItem(0).setVisible(true);
//		menu.getItem(1).setVisible(false);
//		super.onPrepareOptionsMenu(menu);
//	}

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mListener.onExpenseSelected(position);
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
    
    public void update(Boolean isGroupSetupMode){
    	int layout =  android.R.layout.simple_list_item_activated_1;
    	if(isGroupSetupMode){
			setListAdapter(new ArrayAdapter<String>(getActivity(), layout, Participant.getDescriptionStringArray()));
    	}else{
    		setListAdapter(new ArrayAdapter<String>(getActivity(), layout, Expense.getDescriptionStringArray()));
    	}
		super.onResume();
    }

}