package com.github.javon.labassistant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.models.parse.Grade;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewGradeFragment extends Fragment {

    public static final String TAG = NewGradeFragment.class.getName();

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.spinner_course) Spinner mSpinnerCourse;
    @Bind(R.id.spinner_grade) Spinner mSpinnerGrade;
    @Bind(R.id.spinner_lab) Spinner mSpinnerLab;
    @Bind(R.id.et_registration_number) EditText etRegistrationNo;
    @Bind(R.id.btn_save) Button btnSave;
    @Bind(R.id.btn_cancel) Button btnCancel;

    private static final String ARG_COURSE_INDEX = "courseIndex";
    private static final String ARG_LAB_INDEX = "labIndex";

    private int mCourseIndex = 0;
    private int mLabIndex = 0;
    private int mGradeIndex = 0;

    private OnGradeCreatedListener mListener;

    public NewGradeFragment() {
        // Required empty public constructor
    }

    public static NewGradeFragment newInstance(int courseIndex, int labIndex) {
        NewGradeFragment fragment = new NewGradeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COURSE_INDEX, courseIndex);
        args.putInt(ARG_LAB_INDEX, labIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourseIndex = getArguments().getInt(ARG_COURSE_INDEX, 0);
            mLabIndex = getArguments().getInt(ARG_LAB_INDEX, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_grade, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitle("New Grade");


        setupView();

        return view;
    }

    private void setupView() {

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();


        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> mCourseAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.course_array, R.layout.spinner_toolbar_dropdown_title);
        mCourseAdapter.setDropDownViewResource(R.layout.spinner_toolbar_item);
        mSpinnerCourse.setAdapter(mCourseAdapter);
        mSpinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCourseIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<CharSequence> mLabAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.lab_array, R.layout.spinner_toolbar_dropdown_title);
        mLabAdapter.setDropDownViewResource(R.layout.spinner_toolbar_item);
        mSpinnerLab.setAdapter(mLabAdapter);
        mSpinnerLab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLabIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<CharSequence> mGradeAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.grade_array, R.layout.spinner_toolbar_dropdown_title);
        mGradeAdapter.setDropDownViewResource(R.layout.spinner_toolbar_item);
        mSpinnerGrade.setAdapter(mCourseAdapter);
        mSpinnerGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGradeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnSave.setOnClickListener(v -> {
            String registrationNo = etRegistrationNo.getText().toString();
            String code = getResources().getStringArray(R.array.course_array)[mCourseIndex];
            String lab = getResources().getStringArray(R.array.lab_array)[mLabIndex];
            String mark = getResources().getStringArray(R.array.grade_array)[mGradeIndex];

            Grade grade = new Grade(registrationNo, Integer.valueOf(mark), code, Integer.valueOf(lab));

            mListener.attemptSubmission(grade);
        });

        btnCancel.setOnClickListener(v -> mListener.cancelSubmission());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGradeCreatedListener) {
            mListener = (OnGradeCreatedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_new_grade, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface OnGradeCreatedListener {
        void cancelSubmission();
        void attemptSubmission(Grade grade);
    }
}
