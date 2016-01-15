package com.github.javon.labassistant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.classes.helpers.RecyclerItemClickListener;
import com.github.javon.labassistant.classes.holders.CourseViewHolder;
import com.javon.parserecyclerviewadapter.ParseRecyclerQueryAdapter;
import com.parse.ParseObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCourseSelectedListener} interface
 * to handle interaction events.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {

    private OnCourseSelectedListener mListener;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CourseFragment.
     */
    public static CourseFragment newInstance() {
        return new CourseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.course_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        final ParseRecyclerQueryAdapter<ParseObject,CourseViewHolder> adapter = new ParseRecyclerQueryAdapter<>(getActivity(), CourseViewHolder.class,"Course");

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mListener.onCourseSelected(adapter.getItem(position).getString("title"),
                        adapter.getItem(position).getInt("lab_count"),
                        adapter.getItem(position).getString("grade_table_name"));
            }
        }));
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCourseSelectedListener) {
            mListener = (OnCourseSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCourseSelectedListener");
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
    public interface OnCourseSelectedListener {
        void onCourseSelected(String courseName, int labCount, String gradeTableName);
    }
}
