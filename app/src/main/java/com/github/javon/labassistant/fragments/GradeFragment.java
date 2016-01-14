package com.github.javon.labassistant.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javon.labassistant.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnGradesSavedListener} interface
 * to handle interaction events.
 * Use the {@link GradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GradeFragment extends Fragment {

    private static final String ARG_COURSE = "course";
    private static final String ARG_LAB_COUNT = "lab_count";
    private static int MAX_GRADE = 10;

    private String mCourse;
    private ParseObject mObject;
    private NumberPicker labPicker;
    private NumberPicker gradePicker;
    private int mLabCount;

    private OnGradesSavedListener mListener;

    public GradeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param course The name of the course.
     * @return A new instance of fragment GradeFragment.
     */
    public static GradeFragment newInstance(String course, int labCount) {
        GradeFragment fragment = new GradeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COURSE, course);
        args.putInt(ARG_LAB_COUNT,labCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourse = getArguments().getString(ARG_COURSE);
            mLabCount = getArguments().getInt(ARG_LAB_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade, container, false);

        TextView nameView = (TextView) view.findViewById(R.id.student_name);
        TextView title = (TextView) view.findViewById(R.id.course_title);
        labPicker = (NumberPicker) view.findViewById(R.id.lab_number);
        gradePicker = (NumberPicker) view.findViewById(R.id.grade);
        Button saveButton = (Button) view.findViewById(R.id.save_button);

        saveButton.setOnClickListener(new SaveButtonListener());

        labPicker.setMaxValue(mLabCount-1);
        gradePicker.setMaxValue(10);

        title.setText(mCourse);

        String firstName = mObject.getString("first_name");
        String lastName = mObject.getString(("last_name"));

        String name = String.format(Locale.ENGLISH,"%s %s",firstName,lastName);
        nameView.setText(name);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGradesSavedListener) {
            mListener = (OnGradesSavedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGradesSavedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public ParseObject getmObject() {
        return mObject;
    }

    public void setmObject(ParseObject mObject) {
        this.mObject = mObject;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnGradesSavedListener {
        void onGradesSaved();
    }

    // Listener to save grades to parse
    private class SaveButtonListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            int lab_number = labPicker.getValue();
            int grade = gradePicker.getValue();

            final int final_grade = grade*10;

            String lab_field = String.format(Locale.ENGLISH,"lab_%d",lab_number);
            mObject.put(lab_field,final_grade);
            mObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null)
                    {
                        Toast.makeText(getActivity(),"Grade Saved",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Error saving grade:"+e.getCode(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
