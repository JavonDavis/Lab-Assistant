package com.github.javon.labassistant.activities.grades;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.github.javon.labassistant.R;
import com.github.javon.labassistant.dialogs.NewGradeDialog;
import com.github.javon.labassistant.models.Grade;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewGradeActivity extends AppCompatActivity {

    public static final String ARG_USERNAME = "username";

    @Bind(R.id.et_username) EditText etUsername;
    @Bind(R.id.tv_username) TextView tvUsername;
    @Bind(R.id.recycler_view_grades) RecyclerView rvGrades;
    @Bind(R.id.fab_new_grade) FloatingActionButton fabNewGrade;
    @Bind(R.id.et_grade) EditText etGrade;
    @Bind(R.id.btn_save) Button btnSave;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private Firebase refGrades = new Firebase("https://labtech.firebaseio.com/students");
    private FirebaseRecyclerAdapter<Grade, GradeViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_grade);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        fireUsernameUpdate(getIntent().getStringExtra(ARG_USERNAME));

        // set up navigation
        if (null != getSupportActionBar()) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvGrades.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FirebaseRecyclerAdapter<Grade, GradeViewHolder>(Grade.class, android.R.layout.two_line_list_item, GradeViewHolder.class, refGrades) {
            @Override
            protected void populateViewHolder(GradeViewHolder gradeViewHolder, Grade grade, int i) {
                gradeViewHolder.labText.setText("Lab: " + grade.getLabNumber());
                gradeViewHolder.gradeText.setText("Score: " + grade.getValue());
            }
        };

        rvGrades.setAdapter(mAdapter);

        tvUsername.setOnClickListener(v -> {
            v.setVisibility(View.GONE);
            etUsername.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
        });

        btnSave.setOnClickListener(v -> {
            fireUsernameUpdate(etUsername.getText().toString());

            v.setVisibility(View.GONE);
            etUsername.setVisibility(View.GONE);
            tvUsername.setVisibility(View.VISIBLE);
        });

        fabNewGrade.setOnClickListener(v -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag(NewGradeDialog.TAG);

            if (null != prev) transaction.remove(prev);

            transaction.addToBackStack(null);

            NewGradeDialog dialog = NewGradeDialog.newInstance();
            dialog.show(transaction, NewGradeDialog.TAG);
        });
    }

    private boolean fireUsernameUpdate(String username) {
        EventBus.getDefault().post(new UpdateUsernameEvent(username));

        // TODO change this to reflect validation responses
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mAdapter.cleanup();
        super.onDestroy();
    }

    public static class GradeViewHolder extends RecyclerView.ViewHolder {
        @Bind(android.R.id.text1) TextView labText;
        @Bind(android.R.id.text2) TextView gradeText;

        public GradeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Subscribe
    public void onMessageEvent(UpdateUsernameEvent event) {
        etUsername.setText(event.getUsername());
        tvUsername.setText(event.getUsername());
    }

    public class UpdateUsernameEvent {

        private String username;

        public UpdateUsernameEvent(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public boolean isValid() {
            try {
                Integer.parseInt(username); // ensure the username is a number
            } catch (NumberFormatException e) {
                return false;
            }

            return 9 == username.length();
        }
    }
}
