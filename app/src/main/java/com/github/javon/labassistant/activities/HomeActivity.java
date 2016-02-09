package com.github.javon.labassistant.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.Session;
import com.github.javon.labassistant.events.LogoutEvent;
import com.github.javon.labassistant.events.NetworkConnectedEvent;
import com.github.javon.labassistant.events.NetworkOfflineEvent;
import com.github.javon.labassistant.fragments.NewGradeFragment;
import com.github.javon.labassistant.models.Grade;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements NewGradeFragment.OnGradeCreatedListener {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.spinner_course) Spinner mSpinnerCourse;
    @Bind(R.id.spinner_lab) Spinner mSpinnerLab;
    @Bind(R.id.coordinatorLayout) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.fab) FloatingActionButton mFab;
    @Bind(R.id.grades_list) RecyclerView mRecyclerView;

    private EventBus bus = EventBus.getDefault();

    int labIndex = 0, courseIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        bus.register(this);

        setupToolbar();
        setupRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Session session = new Session(this);

                if (ParseUser.getCurrentUser() == null) ParseUser.logOut();
                if (session.isLoggedIn()) session.logout();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> mCourseAdapter = ArrayAdapter
                .createFromResource(this, R.array.course_array, R.layout.spinner_toolbar_dropdown_title);
        mCourseAdapter.setDropDownViewResource(R.layout.spinner_toolbar_item);
        mSpinnerCourse.setAdapter(mCourseAdapter);
        mSpinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                courseIndex = position;

                String[] courses = getResources().getStringArray(R.array.course_array);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> mLabAdapter = ArrayAdapter
                .createFromResource(this, R.array.lab_array, R.layout.spinner_toolbar_dropdown_title);
        mLabAdapter.setDropDownViewResource(R.layout.spinner_toolbar_item);
        mSpinnerLab.setAdapter(mLabAdapter);
        mSpinnerLab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                labIndex = position;

                String[] labs = getResources().getStringArray(R.array.lab_array);

                Toast.makeText(HomeActivity.this, labs[labIndex], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setupRecyclerView() {
        mRecyclerView.setAdapter();

        mFab.setOnClickListener(v -> getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, NewGradeFragment.newInstance(courseIndex, labIndex))
                .addToBackStack(NewGradeFragment.TAG)
                .commit());
    }

    /*
        Events
     */
    @Subscribe
    public void onEvent(NetworkConnectedEvent event) {
        Snackbar.make(mCoordinatorLayout, "Connected to the internet", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Subscribe
    public void onEvent(NetworkOfflineEvent event) {
        Snackbar.make(mCoordinatorLayout, "No connection", Snackbar.LENGTH_LONG)
                .setAction("Reconnect", null).show();
    }

    public void onEvent(LogoutEvent event) {
        Snackbar.make(mCoordinatorLayout, "Connected to the internet", Snackbar.LENGTH_LONG)
                .show();

        finish();
    }

    @Override
    public void cancelSubmission() {
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void attemptSubmission(Grade grade) {

    }


    public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

        List<Grade> gradeList;

        public GradeAdapter(List<Grade> gradeList) {
            this.gradeList = gradeList;
        }

        public void addGrades(List<Grade> newGrades) {
            gradeList.addAll(newGrades);
            notifyDataSetChanged();
        }

        public void addGrade(Grade grade) {
            gradeList.add(grade);
            notifyDataSetChanged();
        }

        @Override
        public GradeAdapter.GradeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_grade, parent, false);

            return new GradeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GradeAdapter.GradeViewHolder holder, int position) {
            View view = LayoutInflater.from()
        }

        @Override
        public int getItemCount() {
            return 0;
        }


        public class GradeViewHolder extends RecyclerView.ViewHolder{

            TextView

            public GradeViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
