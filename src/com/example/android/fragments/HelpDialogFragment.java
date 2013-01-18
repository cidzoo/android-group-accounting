package com.example.android.fragments;

import iuam.group.accounting.R;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;

public class HelpDialogFragment extends DialogFragment {

    public HelpDialogFragment() {
        // Empty constructor required for DialogFragment
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_view, container);

        getDialog().setTitle(R.string.menu_help);
        return view;
    }
}
