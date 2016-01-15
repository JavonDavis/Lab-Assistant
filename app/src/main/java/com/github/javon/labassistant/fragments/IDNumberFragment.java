package com.github.javon.labassistant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javon.labassistant.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link onStudentFoundListener} interface
 * to handle interaction events.
 * Use the {@link IDNumberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IDNumberFragment extends Fragment {

    private static final String ARG_COURSE = "course";
    private static final String ARG_LAB_COUNT = "lab count";
    private static final String ARG_GRADE_TABLE = "table name";

    private String mCourse;
    private String mGradeTableName;
    private int mLabCount;

    private onStudentFoundListener mListener;

    public IDNumberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param course The name of the course.
     * @param labCount Number of labs for the course.
     * @param tableName The name of the remote table name where data is stored.
     * @return A new instance of fragment IDNumberFragment.
     */
    public static IDNumberFragment newInstance(String course, int labCount, String tableName) {
        IDNumberFragment fragment = new IDNumberFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COURSE, course);
        args.putString(ARG_GRADE_TABLE, tableName);
        args.putInt(ARG_LAB_COUNT, labCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourse = getArguments().getString(ARG_COURSE);
            mGradeTableName = getArguments().getString(ARG_GRADE_TABLE);
            mLabCount = getArguments().getInt(ARG_LAB_COUNT);
        }
        setRetainInstance(true);
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
                String idNumber = idNumberField.getText().toString();
                checkID(idNumber);

            }
        });
        return view;
    }

    private void checkID(final String idNumber)
    {
        //lookup logic here
        ParseQuery<ParseObject> query = ParseQuery.getQuery(mGradeTableName);
        query.whereEqualTo("id_number",idNumber);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null)
                {
                    mListener.onStudentFound(mCourse,parseObject,mLabCount);
                }
                else
                {
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND)
                    {
                        Log.d("Student not found",idNumber+" not found in "+mGradeTableName+" table");
                        Toast.makeText(getActivity(),"Student "+idNumber+" Not found",Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        //Some other error
                        Log.d("Parse error","Code:"+e.getCode());
                        Toast.makeText(getActivity(),"Internal error",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onStudentFoundListener) {
            mListener = (onStudentFoundListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onStudentFoundListener");
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
    public interface onStudentFoundListener {
        void onStudentFound(String coursename, ParseObject object, int labCount);
    }
}
