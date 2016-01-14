package com.github.javon.labassistant.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.javon.labassistant.R;
import com.parse.ParseObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnIDNumberFoundListener} interface
 * to handle interaction events.
 * Use the {@link IDNumberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IDNumberFragment extends Fragment {

    private static final String ARG_COURSE = "course";

    private String mCourse;

    private OnIDNumberFoundListener mListener;

    public IDNumberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param course The name of the course.
     * @return A new instance of fragment IDNumberFragment.
     */
    public static IDNumberFragment newInstance(String course) {
        IDNumberFragment fragment = new IDNumberFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COURSE, course);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourse = getArguments().getString(ARG_COURSE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_idnumber, container, false);

        TextView title = (TextView) view.findViewById(R.id.course_title);
        title.setText(mCourse);

        Button manualButton = (Button) view.findViewById(R.id.manual);
        manualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText idNumberField = (EditText) view.findViewById(R.id.id_number);

            }
        });
        return view;
    }

    private boolean lookupID(String idNumber)
    {
        //lookup logic here
        return true;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnIDNumberFoundListener) {
            mListener = (OnIDNumberFoundListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnIDNumberFoundListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnIDNumberFoundListener {
        void onIDNumberFound(String coursename,ParseObject object);
    }
}
