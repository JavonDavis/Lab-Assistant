package com.github.javon.labassistant.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.classes.GradesAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GradesDialogFragment extends DialogFragment {

    private List<GradeItem> grades;

    public GradesDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_grades_dialog, null);

        RecyclerView gradesList = (RecyclerView) view.findViewById(R.id.grades_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        gradesList.setLayoutManager(mLayoutManager);
        gradesList.setAdapter(new GradesAdapter(getGrades()));

        builder.setView(view)
                .setPositiveButton(android.R.string.ok, null);
        return builder.create();
    }

    public List<GradeItem> getGrades() {
        return grades;
    }

    public void setGrades(List<GradeItem> grades) {
        this.grades = grades;
    }

    public static class GradeItem
    {
        public String title;
        public int grade;
        public String marker;
    }
}
