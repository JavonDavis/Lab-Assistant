package com.github.javon.labassistant.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.classes.helpers.Constants;
import com.github.javon.labassistant.fragments.CourseFragment;
import com.github.javon.labassistant.fragments.GradeFragment;
import com.github.javon.labassistant.fragments.IDNumberFragment;
import com.github.javon.labassistant.fragments.dialogs.AddStudentDialogFragment;
import com.google.android.gms.vision.barcode.Barcode;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.codec.binary.StringUtils;

public class MainActivity extends AppCompatActivity implements CourseFragment.OnCourseSelectedListener,
        IDNumberFragment.onStudentFoundListener, GradeFragment.OnGradesSavedListener {

    private static final String ARG_ID = "id";
    private static final String ARG_TABLE = "table";

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

        if(Constants.isNetworkAvailable(this)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, idNumberFragment)
                    .addToBackStack("Id Number")
                    .commit();
        }
        else
        {
            Toast.makeText(this,"No network Connection",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
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

        if(Constants.isNetworkAvailable(this)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, gradeFragment)
                    .addToBackStack("Grades")
                    .commit();
        }
        else
        {
            Toast.makeText(this,"No network Connection",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStudentNotFound(String courseName, final String idNumber, final String tableName) {
        new AlertDialog.Builder(this)
                .setTitle("Student not found")
                .setMessage("Student not found in database. This is because student was no" +
                        "t enrolled for"+ courseName +" at the last time of population of the database. Would " +
                        "you like to manually add the student for now?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddStudentDialogFragment addDialog = new AddStudentDialogFragment();
                        Bundle args = new Bundle();
                        args.putString(ARG_ID,idNumber);
                        args.putString(ARG_TABLE,tableName);
                        addDialog.setArguments(args);
                        addDialog.show(getSupportFragmentManager(),"Add Student");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onGradeSaved() {
        if(Constants.isNetworkAvailable(this)) {
            super.onBackPressed();
        }
        else
        {
            Toast.makeText(this,"No network Connection",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
    }
}
