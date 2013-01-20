package ch.hesso.iuam.groupaccounting;

import iuam.group.accounting.R;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutDialogFragment extends DialogFragment {

    public AboutDialogFragment() {
        // Empty constructor required for DialogFragment
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_view, container);

        getDialog().setTitle(R.string.menu_about);
        return view;
    }
}
