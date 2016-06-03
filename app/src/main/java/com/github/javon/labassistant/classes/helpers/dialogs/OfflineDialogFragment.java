package com.github.javon.labassistant.classes.helpers.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.javon.labassistant.R;

/**
 * Created by shane on 1/25/16.
 */
public class OfflineDialogFragment extends DialogFragment{

    OfflineDialogListener mListener;



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View rootView = inflater.inflate(R.layout.dialog_offline_fragment, null);

        final EditText etNumber = (EditText) rootView.findViewById(R.id.student_id);
        final Spinner coursesSpinner = (Spinner) rootView.findViewById(R.id.course_codes);
        final Spinner gradeSpinner = (Spinner) rootView.findViewById(R.id.grades);
        final Spinner labSpinner = (Spinner) rootView.findViewById(R.id.labs);

        ArrayAdapter<CharSequence> labAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.lab_array, android.R.layout.simple_spinner_item);
        labAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        labSpinner.setAdapter(labAdapter);

        ArrayAdapter<CharSequence> gradeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.grade_array, android.R.layout.simple_spinner_item);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(gradeAdapter);

        final ArrayAdapter<CharSequence> courseAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.course_array, android.R.layout.simple_spinner_item);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coursesSpinner.setAdapter(courseAdapter);


        builder.setView(rootView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = etNumber.getText().toString();
                        String course = coursesSpinner.getSelectedItem().toString();
                        String grade = gradeSpinner.getSelectedItem().toString();
                        String lab = labSpinner.getSelectedItem().toString();

                        mListener.onDialogPositiveClick(id, course, grade, lab);
                    }
                })
                .setNegativeButton("Cancel",null)
                .setNeutralButton("View All grades", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.showAllGrades();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (OfflineDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface OfflineDialogListener {
        void onDialogPositiveClick(String id, String course, String grade, String lab);
        void showAllGrades();
    }
}
