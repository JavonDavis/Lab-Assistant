package com.github.javon.labassistant.activities.grades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.github.javon.labassistant.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewGradeActivity extends AppCompatActivity {

    @Bind(R.id.et_registration_number) EditText etRegistrationNumber;
    @Bind(R.id.et_grade) EditText etGrade;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_grade);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // set up navigation
        if (null != getSupportActionBar()) getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

}
