package edu.byu.cs.rtblaser.classscheduler.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import edu.byu.cs.rtblaser.classscheduler.R;

/**
 * Created by RyanBlaser on 10/26/17.
 */

public class EmailDialogFragment extends DialogFragment {

    public interface EmailDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String emailAddress);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    EmailDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the EmailDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EmailDialogListener so we can send events to the host
            mListener = (EmailDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement EmailDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = (View) inflater.inflate(R.layout.email_dialogue_layout, null);
        builder.setMessage("Email Schedule")
                .setView(v)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String emailAddress = ((EditText)v.findViewById(R.id.email_input)).getText().toString();
                        if (emailAddress.contains("@")) {
                            mListener.onDialogPositiveClick(EmailDialogFragment.this, emailAddress);
                        } else {
                            Toast.makeText(getActivity(), "Please enter a valid email addresss.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(EmailDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
