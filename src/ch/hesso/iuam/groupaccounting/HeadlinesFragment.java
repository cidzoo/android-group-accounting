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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class HeadlinesFragment extends ListFragment {
    HeadlinesFragmentListener mListener;
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
        
//        Animation spinin = AnimationUtils.loadAnimation(this.getActivity(), android.R.anim.slide_in_left);
//        LayoutAnimationController controller =new LayoutAnimationController(spinin);   
//        getListView().setLayoutAnimation(controller);
        
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {

        	private LinkedList<Object> selectedItems = new LinkedList<Object>();
        	private LinkedList<Integer> selectedItemsId = new LinkedList<Integer>();
        	
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
            	Object o;
            	if(isGroupSetupMode)
            		o = Member.getMembers().get(position);
            	else
            		o = Expense.getExpenses().get(position);
            	
            	if (checked){
            		selectedItems.add(o);
            		selectedItemsId.add(position);
            		getListView().getChildAt(position).setBackgroundColor(0x6633b5e5);
        			//getResources().getColor(android.R.color.holo_blue_light));
            				
            	}else{
            		selectedItems.remove(o);
            		selectedItemsId.remove(position);
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
                    	int size = selectedItems.size();
                    	for (int i=0;i<size;i++){
                    		if(isGroupSetupMode)
                    			Member.getMembers().remove(selectedItems.remove());
                    		else
                    			Expense.getExpenses().remove(selectedItems.remove());
                    	}	
                    	mode.finish(); // Action picked, so close the CAB
                        return true;
                    
                    case R.id.menu_edit:
                    	// Notify the parent activity of selected item
                        mListener.onHeadlineEdit(selectedItemsId.removeFirst());
                        
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
            	for (Integer i : selectedItemsId){
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

    }
    

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

    public void addItem(Expense exp){
    	setListAdapter(expensesAdapter);
    	
    	item = new HashMap<String, String>();
    	
    	item.put("description", exp.getDescription());
        item.put("payer", exp.getPayerName());
        item.put("img", String.valueOf(R.drawable.ic_expense));
        item.put("price", String.valueOf(exp.getPrice())+".-");
        listItem.add(item);
    }
    public void update(Boolean isGroupSetupMode){
    	
    	this.isGroupSetupMode = isGroupSetupMode;
    	listItem.clear();
    	
    	if(isGroupSetupMode){    
    		setListAdapter(membersAdapter);
   		 
	        Iterator<Member> i = Member.getMembers().iterator();
	        while (i.hasNext()){
	        	Member m= i.next();
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