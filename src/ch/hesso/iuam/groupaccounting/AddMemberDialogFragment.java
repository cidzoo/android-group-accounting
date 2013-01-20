package ch.hesso.iuam.groupaccounting;

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
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class AddMemberDialogFragment extends DialogFragment implements OnEditorActionListener {

    public interface NewMemberFragmentListener {
        void onFinishEditDialog(String inputText);
    }

    private EditText mEditText;
    private Member mCurrentMember;

    public AddMemberDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public AddMemberDialogFragment(Member p) {
        super();
        mCurrentMember=p;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_member_view, container);
        mEditText = (EditText) view.findViewById(R.id.participant_name);
        
        if(mCurrentMember!=null) mEditText.setText(mCurrentMember.getName());
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

    		for(int i = 0;i<Member.getDescriptionStringArray().length;i++){
    			if (mEditText.getText().toString().equals(Member.getDescriptionStringArray()[i])){
    				Toast t = Toast.makeText(this.getActivity(), "Member already exists", Toast.LENGTH_SHORT);
        			t.setGravity(Gravity.CENTER_VERTICAL, 0, -20);
        			t.show();
        			return true;
    			}
    		}
    		
            // Return input text to activity
    		if(mCurrentMember!=null) mCurrentMember.setName(mEditText.getText().toString());
        	NewMemberFragmentListener activity = (NewMemberFragmentListener) getActivity();
            activity.onFinishEditDialog(mEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }
}
