package com.github.javon.labassistant.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.events.NetworkConnectedEvent;
import com.github.javon.labassistant.events.NetworkOfflineEvent;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.spinner_course) Spinner mSpinnerCourse;
    @Bind(R.id.coordinatorLayout) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.fab) FloatingActionButton mFab;
    @Bind(R.id.date) TextView tvDate;

    private EventBus bus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        bus.register(this);

        setupToolbar();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

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

        ArrayAdapter<CharSequence> mAdapter = ArrayAdapter
                .createFromResource(this, R.array.course_array, R.layout.spinner_toolbar_dropdown_title);
        mAdapter.setDropDownViewResource(R.layout.spinner_toolbar_item);
        mSpinnerCourse.setAdapter(mAdapter);

        Calendar now = Calendar.getInstance();

        String currentDate = now.get(Calendar.MONTH) + "/"
                + now.get(Calendar.DAY_OF_MONTH) + "/" +
                + now.get(Calendar.YEAR);

        tvDate.setText(currentDate);

        tvDate.setOnClickListener(v -> {

            DatePickerDialog dialog = DatePickerDialog.newInstance(
                    HomeActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show(getFragmentManager(), "Datepickerdialog");
        });

//        CourseSpinnerAdapter adapter = new CourseSpinnerAdapter();
//        mSpinnerCourse.setAdapter(adapter);
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }
}
