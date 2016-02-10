package com.github.javon.labassistant.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.Session;
import com.github.javon.labassistant.events.LogoutEvent;
import com.github.javon.labassistant.events.NetworkConnectedEvent;
import com.github.javon.labassistant.events.NetworkOfflineEvent;
import com.github.javon.labassistant.fragments.NewGradeFragment;
import com.github.javon.labassistant.models.Grade;
import com.github.javon.labassistant.utility.NetworkUtil;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    @Bind(R.id.grades_list) RecyclerView rvGrades;
    @Bind(R.id.fragment_container) FrameLayout frameLayout;

    private boolean isConnected = false;
    private GradeAdapter mAdapter;

    private EventBus bus = EventBus.getDefault();

    int labIndex = 0, courseIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        bus.register(this);

        isConnected = NetworkUtil.isNetworkAvailable(this);

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
                bus.post(new LogoutEvent());
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<CharSequence> mLabAdapter = ArrayAdapter
                .createFromResource(this, R.array.lab_array, R.layout.spinner_toolbar_dropdown_title);
        mLabAdapter.setDropDownViewResource(R.layout.spinner_toolbar_item);
        mSpinnerLab.setAdapter(mLabAdapter);
        mSpinnerLab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                labIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvGrades.setLayoutManager(layoutManager);
        mAdapter = new GradeAdapter();
        rvGrades.setAdapter(mAdapter);

        mFab.setOnClickListener(v -> getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), NewGradeFragment.newInstance(courseIndex, labIndex))
                .addToBackStack(NewGradeFragment.TAG)
                .commit());
    }

    /*
        Events
     */
    @Subscribe
    public void onEvent(NetworkConnectedEvent event) {
        isConnected = true;

        displaySnackBarWithMessage("Connected to the internet").setAction("Action", null).show();
    }

    @Subscribe
    public void onEvent(NetworkOfflineEvent event) {
        isConnected = false;

        displaySnackBarWithMessage("No connection").setAction("Reconnect", null).show();
    }

    @Subscribe
    public void onEvent(CourseChangedEvent event) {
        courseIndex = event.getIndex();

        String courseCode = getStringFromResources(courseIndex, R.array.course_array);
        int lab = Integer
                .valueOf(getStringFromResources(labIndex, R.array.lab_array));

        ParseQuery<Grade> query = ParseQuery.getQuery(Grade.class);
        query.whereEqualTo("code", courseCode);
        query.whereEqualTo("labNumber", lab);

        query.findInBackground((grades, e) -> {
            if (e != null) {
                displaySnackBarWithMessage("Error loading grades").show();
                return;
            }

            bus.post(new GradesLoadedEvent(grades));
        });
    }

    @Subscribe
    public void onEvent(LabChangedEvent event) {

        labIndex = event.getIndex();

        String courseCode = getStringFromResources(courseIndex, R.array.course_array);
        int lab = Integer
                .valueOf(getStringFromResources(labIndex, R.array.lab_array));

        ParseQuery<Grade> query = ParseQuery.getQuery(Grade.class);
        query.whereEqualTo("code", courseCode);
        query.whereEqualTo("labNumber", lab);

        query.findInBackground((grades, e) -> {
            if (e != null) {
                displaySnackBarWithMessage("Error loading grades").show();
            }

            bus.post(new GradesLoadedEvent(grades));
        });
    }

    @Subscribe
    public void onEvent(GradeSavedEvent event) {
        displaySnackBarWithMessage("Saved successful").show();
    }

    public void onEvent(LogoutEvent event) {
        displaySnackBarWithMessage("Connected to the internet").show();
        finish();
    }

    private String getStringFromResources(int index, int resource) {
        return getResources().getStringArray(R.array.lab_array)[index];
    }

    private Snackbar displaySnackBarWithMessage(String message) {
        return Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG);
    }

    @Override
    public void cancelSubmission() {
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void attemptSubmission(Grade grade) {
        ParseUser user = ParseUser.getCurrentUser();
        Session session = new Session(this);

        if (isConnected) {
            if (null != user)
                attemptToSaveGrade(grade, user);
            else {
                ParseUser.logInInBackground(session.getUsername(), session.getPassword(), (parseUser, e) -> {
                    if (null == e) {
                        displaySnackBarWithMessage("Could not verify your credentials").show();
                        return;
                    }
                    attemptToSaveGrade(grade, parseUser);
                });
            }
        } else {
            // check if the user is logged into parse
            if (null != user) {
                grade.setUser(user);
                grade.saveEventually();
                return;
            }

            // only occurs if the user isn't verified
            grade.pinInBackground();
        }

    }

    private void attemptToSaveGrade(Grade grade, ParseUser user) {
        grade.setUser(user);
        grade.saveInBackground(e -> {
            if (e != null) {
                displaySnackBarWithMessage("Unable to save grade").setAction("Retry", v -> attemptSubmission(grade)).show();
                return;
            }
            bus.post(new GradeSavedEvent(grade));
        });

    }

    public class CourseChangedEvent {

        private int index;

        public CourseChangedEvent(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public class LabChangedEvent {

        private int index;

        public LabChangedEvent(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public class GradesLoadedEvent {

        private List<Grade> gradeList;

        public GradesLoadedEvent(List<Grade> grades) {
            gradeList = grades;
        }

        public List<Grade> getGrades() {
            return gradeList;
        }
    }

    public class GradeSavedEvent {

        private final Grade grade;

        public GradeSavedEvent(Grade grade) {
            this.grade = grade;
        }

        public Grade getGrade() {
            return grade;
        }
    }

    /*
        Adapters
     */
    public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

        List<Grade> gradeList;

        public GradeAdapter() {
            gradeList = new ArrayList<>();
        }

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
            holder.registrationNumber.setText(gradeList.get(position).getRegistrationNumber());
            holder.grade.setText(gradeList.get(position).getMark());
        }

        @Override
        public int getItemCount() {
            return gradeList.size();
        }


        public class GradeViewHolder extends RecyclerView.ViewHolder{

            TextView registrationNumber;
            TextView grade;

            public GradeViewHolder(View itemView) {
                super(itemView);

                registrationNumber = (TextView) itemView.findViewById(R.id.registration_number);
                grade = (TextView) itemView.findViewById(R.id.grade);
            }
        }
    }
}
