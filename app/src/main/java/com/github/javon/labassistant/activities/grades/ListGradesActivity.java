package com.github.javon.labassistant.activities.grades;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.github.javon.labassistant.R;
import com.github.javon.labassistant.models.Grade;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListGradesActivity extends AppCompatActivity {

    @Bind(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @Bind(R.id.recycler_view_grades) RecyclerView gradesRecyclerView;
    @Bind(R.id.fab_new_grade) FloatingActionButton fabGrade;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private FirebaseRecyclerAdapter<Grade, GradeViewHolder> mAdapter;
    private Firebase refGrade = new Firebase("https://labtech.firebaseio.com/grades");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_grades);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        gradesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FirebaseRecyclerAdapter<Grade, GradeViewHolder>(Grade.class, android.R.layout.two_line_list_item, GradeViewHolder.class, refGrade) {
            @Override
            protected void populateViewHolder(GradeViewHolder gradeViewHolder, Grade grade, int i) {
                gradeViewHolder.idText.setText("Student Id");
                gradeViewHolder.gradeText.setText(grade.getValue());
            }
        };

        gradesRecyclerView.setAdapter(mAdapter);

        fabGrade.setOnClickListener(v -> startActivity(new Intent(ListGradesActivity.this, NewGradeActivity.class)));

    }

    public static class GradeViewHolder extends RecyclerView.ViewHolder {
        @Bind(android.R.id.text1) TextView idText;
        @Bind(android.R.id.text2) TextView gradeText;

        public GradeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup(); // release references
    }
}
