package com.example.android.fragments;

import iuam.group.accounting.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Menu;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        setHasOptionsMenu(true);
    }
    
    @Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.menu_add).setVisible(false);
		menu.findItem(R.id.menu_done).setVisible(false);
	}
}
