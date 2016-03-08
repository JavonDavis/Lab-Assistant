package com.github.javon.labassistant.activities.students;

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
import com.github.javon.labassistant.activities.grades.NewGradeActivity;
import com.github.javon.labassistant.models.Student;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListStudentsActivity extends AppCompatActivity {

    @Bind(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @Bind(R.id.recycler_view_grades) RecyclerView recyclerViewStudents;
    @Bind(R.id.fab_new_grade) FloatingActionButton fabNewStudent;
    @Bind(R.id.et_empty_list) TextView emptyText;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private FirebaseRecyclerAdapter<Student, StudentViewHolder> mAdapter;
    private Firebase refStudent = new Firebase("https://labtech.firebaseio.com/students");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_grades);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FirebaseRecyclerAdapter<Student, StudentViewHolder>(Student.class, android.R.layout.simple_list_item_1, StudentViewHolder.class, refStudent) {
            @Override
            protected void populateViewHolder(StudentViewHolder studentViewHolder, Student student, int i) {
                final String username = student.getRegistrationNumber();

                studentViewHolder.idText.setText(username);
                studentViewHolder.idText.setOnClickListener(v -> {

                    openStudentDetailsActivity(username);

//                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    Fragment prev = getSupportFragmentManager().findFragmentByTag(NewGradeDialog.TAG);
//
//                    if (null != prev) transaction.remove(prev);
//
//                    transaction.addToBackStack(null);
//
//                    NewGradeDialog dialog = NewGradeDialog.newInstance(student.getRegistrationNumber());
//                    dialog.show(transaction, NewGradeDialog.TAG);
                });
            }
        };

        recyclerViewStudents.setAdapter(mAdapter);

        fabNewStudent.setOnClickListener(v -> openStudentDetailsActivity(""));
    }

    private void openStudentDetailsActivity(String username) {
        Intent intent = new Intent(ListStudentsActivity.this, NewGradeActivity.class);
        intent.putExtra(NewGradeActivity.ARG_USERNAME, username);
        startActivity(intent);
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        @Bind(android.R.id.text1) TextView idText;

        public StudentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    protected void onDestroy() {
        mAdapter.cleanup(); // release references
        super.onDestroy();
    }
}
