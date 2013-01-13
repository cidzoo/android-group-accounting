package com.example.android.fragments;

import iuam.group.accounting.R;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class AddParticipantDialogFragment extends DialogFragment implements OnEditorActionListener {

    public interface NewParticipantFragmentListener {
        void onFinishEditDialog(String inputText);
    }

    private EditText mEditText;
    private Participant mCurrentParticipant;

    public AddParticipantDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public AddParticipantDialogFragment(Participant p) {
        super();
        mCurrentParticipant=p;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_member_view, container);
        mEditText = (EditText) view.findViewById(R.id.participant_name);
        
        if(mCurrentParticipant!=null) mEditText.setText(mCurrentParticipant.getName());
        getDialog().setTitle(R.string.title_new_participant);

        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        
    	if (EditorInfo.IME_ACTION_DONE == actionId) {
    		if (mEditText.length() == 0){
    			Toast t = Toast.makeText(this.getActivity(), "Please enter a name", Toast.LENGTH_SHORT);
    			t.setGravity(Gravity.CENTER_VERTICAL, 0, -20);
    			t.show();
    			return true;
    		}
            // Return input text to activity
    		if(mCurrentParticipant!=null) mCurrentParticipant.setName(mEditText.getText().toString());
        	NewParticipantFragmentListener activity = (NewParticipantFragmentListener) getActivity();
            activity.onFinishEditDialog(mEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }
}
