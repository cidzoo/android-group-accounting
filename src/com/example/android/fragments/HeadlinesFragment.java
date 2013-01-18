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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class HeadlinesFragment extends ListFragment {
    HeadlinesFragmentListener mListener;
    
    /**
     * CAB 
     */
//    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
//
//        // Called when the action mode is created; startActionMode() was called
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            // Inflate a menu resource providing context menu items
//            MenuInflater inflater = mode.getMenuInflater();
//            inflater.inflate(R.menu.context_menu, menu);
//            return true;
//        }
//
//        // Called each time the action mode is shown. Always called after onCreateActionMode, but
//        // may be called multiple times if the mode is invalidated.
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false; // Return false if nothing is done
//        }
//
//        // Called when the user selects a contextual menu item
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.menu_delete:
//                	Toast.makeText(getActivity(), "Deleted!", Toast.LENGTH_SHORT).show();
//                    mode.finish(); // Action picked, so close the CAB
//                    return true;
//                default:
//                    return false;
//            }
//        }
//
//        // Called when the user exits the action mode
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//            mActionMode = null;
//        }
//    };
    
    ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
    

    // informations for one item
    HashMap<String, String> item;
    private SimpleAdapter expensesAdapter;
	private ListAdapter membersAdapter;

	protected Object mActionMode;

	private Boolean isGroupSetupMode;
	

    // The container Activity must implement this interface so the frag can deliver messages
    public interface HeadlinesFragmentListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onHeadlineSelected(int position);
        /** Called by HeadlinesFragment when a list item is selected for edit*/
        public void onHeadlineEdit(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        expensesAdapter = new SimpleAdapter (
				getActivity(), 
				listItem, 
				R.layout.listview_expense,
	            new String[] {"img", "description", "payer", "price"}, 
	            new int[] {R.id.img, R.id.description, R.id.payer, R.id.price} );
        
        membersAdapter = new SimpleAdapter (
				getActivity(), 
				listItem, 
				R.layout.listview_member,
	            new String[] {"img", "name"}, 
	            new int[] {R.id.img, R.id.name} );
        
        update(getArguments().getBoolean("isGroupSetupMode"));
        
        //This has menus
        setHasOptionsMenu(true);
    }
    


	@Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        
//        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//                    int arg2, long arg3) {
//                
//            	if (mActionMode != null) {
//                    return false;
//                }
//
//                // Start the CAB using the ActionMode.Callback defined above
//                mActionMode = getActivity().startActionMode(mActionModeCallback);
//                arg1.setSelected(true);
//                return true;
//            }
//        });
        
        getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {

        	private LinkedList<Integer> selectedItems = new LinkedList<Integer>();
        	
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
            	if (checked){
            		selectedItems.add(position);
            		getListView().getChildAt(position).setBackgroundColor(0x6633b5e5);
        			//getResources().getColor(android.R.color.holo_blue_light));
            				
            	}else{
            		selectedItems.remove(Integer.valueOf(position));
            		getListView().getChildAt(position).setBackgroundColor(0x5c5cff);
            	}
            	
            	
            	/* update interface */
            	mode.setTitle(String.valueOf(selectedItems.size())+" selected");
            	if(selectedItems.size() > 1)
            		mode.getMenu().findItem(R.id.menu_edit).setVisible(false);
            	else
            		mode.getMenu().findItem(R.id.menu_edit).setVisible(true);
            	
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                    	for (Integer i : selectedItems){
                    		if(isGroupSetupMode)
                    			Participant.getParticipants().remove(i.intValue());
                    		else
                    			Expense.getExpenses().remove(i.intValue());
                    	}	
                    	mode.finish(); // Action picked, so close the CAB
                        return true;
                    
                    case R.id.menu_edit:
                    	// Notify the parent activity of selected item
                        mListener.onHeadlineEdit(selectedItems.removeFirst());
                        
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                        
                        // Set the item as checked to be highlighted when in two-pane layout
                        //getListView().setItemChecked(position, true);
                        
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
            	for (Integer i : selectedItems){
            		getListView().getChildAt(i).setBackgroundColor(0x5c5cff);
            	}
            	
            	selectedItems.clear();
            	update(isGroupSetupMode);
            	getActivity().invalidateOptionsMenu();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    		
//        // When in two-pane layout, set the listview to highlight the selected list item
//        // (We do this during onStart because at the point the listview is available.)
//        if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
//            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        	
//        }
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

    
    public void update(Boolean isGroupSetupMode){
    	
    	this.isGroupSetupMode = isGroupSetupMode;
    	listItem.clear();
    	
    	if(isGroupSetupMode){    
    		setListAdapter(membersAdapter);
   		 
	        Iterator<Participant> i = Participant.getParticipants().iterator();
	        while (i.hasNext()){
	        	Participant m= i.next();
	        	item = new HashMap<String, String>();
	        	
	        	item.put("name", m.getName());
    	        item.put("img", String.valueOf(R.drawable.ic_member));
    	        listItem.add(item);
	        }
    	}else{
    		setListAdapter(expensesAdapter);
    		 
	        Iterator<Expense> i = Expense.getExpenses().iterator();
	        while (i.hasNext()){
	        	Expense exp= i.next();
	        	item = new HashMap<String, String>();
	        	
	        	item.put("description", exp.getDescription());
    	        item.put("payer", exp.getPayerName());
    	        item.put("img", String.valueOf(R.drawable.ic_expense));
    	        item.put("price", String.valueOf(exp.getPrice())+".-");
    	        listItem.add(item);
	        }
    	}
    
	    super.onResume();
    }

    @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if(!isGroupSetupMode){
			mListener.onHeadlineSelected(position);
		}
		super.onListItemClick(l, v, position, id);
	}
}