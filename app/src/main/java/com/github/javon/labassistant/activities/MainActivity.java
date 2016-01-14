package com.github.javon.labassistant.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.classes.views.CustomViewPager;
import com.github.javon.labassistant.fragments.CourseFragment;
import com.github.javon.labassistant.fragments.GradeFragment;
import com.github.javon.labassistant.fragments.IDNumberFragment;
import com.parse.ParseObject;

public class MainActivity extends AppCompatActivity implements CourseFragment.OnCourseSelectedListener,IDNumberFragment.OnIDNumberFoundListener, GradeFragment.OnGradesSavedListener {

    private static final int NUM_PAGES = 3;

    private String mCourseName = "";
    private CourseFragment courseFragment;
    private IDNumberFragment idNumberFragment;
    private GradeFragment gradeFragment;
    private ParseObject object;
    private boolean overlap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        courseFragment = CourseFragment.newInstance();

        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,courseFragment)
                    .commit();
        }
    }

    @Override
    public void onCourseSelected(String courseName) {
        mCourseName = courseName;
        idNumberFragment = IDNumberFragment.newInstance(courseName);

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
                .replace(R.id.container,idNumberFragment)
                .addToBackStack("Id Number")
                .commit();
    }

    @Override
    public void onIDNumberFound(String courseName, ParseObject object) {
        mCourseName = courseName;
        gradeFragment = GradeFragment.newInstance(courseName);

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
                .replace(R.id.container,gradeFragment)
                .addToBackStack("Grades")
                .commit();
    }

    @Override
    public void onGradesSaved() {

    }

    private interface ActivityOptions
    {
        int COURSE = 0;
        int ID_NUMBER= 1;
        int GRADES = 2;
    }
}