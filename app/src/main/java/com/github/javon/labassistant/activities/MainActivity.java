package com.github.javon.labassistant.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.fragments.CourseFragment;
import com.github.javon.labassistant.fragments.GradeFragment;
import com.github.javon.labassistant.fragments.IDNumberFragment;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements CourseFragment.OnCourseSelectedListener,IDNumberFragment.onStudentFoundListener, GradeFragment.OnGradesSavedListener {

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CourseFragment courseFragment = CourseFragment.newInstance();

        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, courseFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                ParseUser.logOut();
                Intent intent = new Intent(this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCourseSelected(String courseName, int labCount, String gradeTableName) {
        IDNumberFragment idNumberFragment = IDNumberFragment.newInstance(courseName, labCount, gradeTableName);

        /*
        //added a little transition
        //TODO - add more transition animations
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slideTransition = new Slide(Gravity.RIGHT);
            slideTransition.setDuration(1000);
            Slide slideTransitionExit = new Slide(Gravity.LEFT);
            slideTransition.setDuration(1000);
            courseFragment.setExitTransition(slideTransitionExit);
            idNumberFragment.setEnterTransition(slideTransition);
            idNumberFragment.setAllowEnterTransitionOverlap(overlap);
            courseFragment.setAllowReturnTransitionOverlap(overlap);
        }*/

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, idNumberFragment)
                .addToBackStack("Id Number")
                .commit();
    }

    @Override
    public void onStudentFound(String courseName, ParseObject object, int lab_count) {
        GradeFragment gradeFragment = GradeFragment.newInstance(courseName, lab_count);
        gradeFragment.setmObject(object);

        /*
        //added a little transition
        //TODO - add more transition animations
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slideTransition = new Slide(Gravity.RIGHT);
            slideTransition.setDuration(1000);
            Slide slideTransitionExit = new Slide(Gravity.LEFT);
            slideTransition.setDuration(1000);
            idNumberFragment.setExitTransition(slideTransitionExit);
            gradeFragment.setEnterTransition(slideTransition);
            gradeFragment.setAllowEnterTransitionOverlap(overlap);
            idNumberFragment.setAllowReturnTransitionOverlap(overlap);
        }*/

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, gradeFragment)
                .addToBackStack("Grades")
                .commit();
    }

    @Override
    public void onGradeSaved() {
        super.onBackPressed();
    }
}
