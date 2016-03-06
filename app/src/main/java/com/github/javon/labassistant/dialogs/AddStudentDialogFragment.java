package com.github.javon.labassistant.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.javon.labassistant.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 9/16/15.
 */
public class AddStudentDialogFragment extends DialogFragment {

    private static final String ARG_ID = "id";
    private static final String ARG_TABLE = "table";

    public AddStudentDialogFragment()
    {
        //required empty constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.student_form, null);

        final EditText idnumField = (EditText) view.findViewById(R.id.id_number_new);
        final EditText firstnameField = (EditText) view.findViewById(R.id.first_name);
        final EditText lastnameField = (EditText) view.findViewById(R.id.last_name);


        String tableName = null;
        if(getArguments()!=null)
        {
            String idnum = getArguments().getString(ARG_ID);
            tableName = getArguments().getString(ARG_TABLE);
            idnumField.setText(idnum);
        }
        else
        {
            dismiss();
        }

        final String finalTableName = tableName;
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final String idNumber = idnumField.getText().toString();
                        final String firstName = firstnameField.getText().toString();
                        final String lastName = lastnameField.getText().toString();
                        if(idNumber.length()>0 && firstName.length()>0 && lastName.length()>0)
                        {
                            ParseObject student = new ParseObject(finalTableName);
                            student.put("id_number", idNumber);
                            student.put("first_name", firstName);
                            student.put("last_name", lastName);
                            student.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null)
                                    {
                                        Toast.makeText(getActivity(),"Successfully inserted student into database",Toast.LENGTH_LONG).show();
                                        dismiss();
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(),"Error inserting student into database",Toast.LENGTH_LONG).show();
                                        Log.d("Parse","Error adding student");
                                    }

                                }
                            });
                        }
                    }
                })
                .setNegativeButton("cancel", null);
        return builder.create();
    }
}
