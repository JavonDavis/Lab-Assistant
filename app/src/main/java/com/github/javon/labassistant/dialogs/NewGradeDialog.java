package com.github.javon.labassistant.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.firebase.client.Firebase;
import com.github.javon.labassistant.R;
import com.github.javon.labassistant.models.Grade;
import com.github.javon.labassistant.models.Session;
import com.github.javon.labassistant.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shane on 3/7/16.
 */
public class NewGradeDialog extends DialogFragment {

    public static final String TAG = "new_grade_dialog";
    public static final String ARG_USERNAME = "username";

    // text
    @Bind(R.id.et_username) EditText etUsername;

    // spinners
    @Bind(R.id.spinner_course) Spinner spinnerCourse;
    @Bind(R.id.spinner_grade) Spinner spinnerGrade;
    @Bind(R.id.spinner_lab) Spinner spinnerLab;

    // buttons
    @Bind(R.id.btn_cancel) Button btnCancel;
    @Bind(R.id.btn_confirm) Button btnConfirm;

    private Firebase refStudents = new Firebase("https://labtech.firebaseio.com/students");
    private String username;

    public static NewGradeDialog newInstance(String username) {
        NewGradeDialog dialog = new NewGradeDialog();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        dialog.setArguments(args);
        return dialog;
    }

    public static NewGradeDialog newInstance() {
        return new NewGradeDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getArguments()) username = getArguments().getString(ARG_USERNAME);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_new_grade, container, false);
        ButterKnife.bind(this, rootView);

        etUsername.setText(username);

        spinnerCourse.setAdapter(createAdapter(R.array.course_array));
        spinnerGrade.setAdapter(createAdapter(R.array.grade_array));
        spinnerLab.setAdapter(createAdapter(R.array.lab_array));

        btnCancel.setOnClickListener(v -> NewGradeDialog.this.dismiss());
        btnConfirm.setOnClickListener(v -> {

            final String username = etUsername.getText().toString();
            final String course = (String) spinnerCourse.getSelectedItem();
            final int lab = (null == spinnerLab.getSelectedItem()) ? -1 : (int) spinnerLab.getSelectedItem();
            final int score = (null == spinnerGrade.getSelectedItem()) ? -1 : (int) spinnerGrade.getSelectedItem();

            if (!username.isEmpty() && !course.isEmpty() && (lab != -1) && (score != -1)) {
                // create a record of the particular student
                Firebase student = refStudents.child(username);

                // creating a timestamp
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final Date timestamp = new Date();

                Session session = new Session(getActivity());
                User marker = new User(session.getUsername());
                Grade newGrade = new Grade(score, lab, dateFormat.format(timestamp), marker);

                student.push().setValue(newGrade);

                NewGradeDialog.this.dismiss();
            }
        });

        return rootView;
    }

    private ArrayAdapter createAdapter(int stringArrayResource) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(getActivity(), stringArrayResource, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }
}
