package edu.byu.cs.rtblaser.classscheduler.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.byu.cs.rtblaser.classscheduler.R;

/**
 * Created by RyanBlaser on 10/26/17.
 */

public class CourseInfoDialogFragment extends DialogFragment {

    String courseInfo;

    public CourseInfoDialogFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = (View) inflater.inflate(R.layout.course_info_dialogue_layout, null);
        TextView courseInfoView = (TextView)v.findViewById(R.id.course_info_text);
        courseInfoView.setText(courseInfo);
        builder.setMessage("Course Info")
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setCourseInfo(String courseInfo) {
        this.courseInfo = courseInfo;
    }
}
