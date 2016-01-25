package com.github.javon.labassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.javon.labassistant.OfflineGrade;
import com.github.javon.labassistant.R;
import com.github.javon.labassistant.dialogs.OfflineDialogFragment;
import com.github.javon.labassistant.fragments.LoginFragment;
import com.github.javon.labassistant.fragments.ProgressFragment;
import com.parse.ParseUser;

<<<<<<< HEAD
public class LoginActivity extends AppCompatActivity
        implements LoginFragment.OnLoginAttemptedListener, ProgressFragment.OnLoginProgressListener,
        OfflineDialogFragment.OfflineDialogListener {

    OfflineDialogFragment dialog = null;
=======
public class LoginActivity extends AppCompatActivity implements LoginFragment.OnLoginAttemptedListener,
        ProgressFragment.OnLoginProgressListener {
>>>>>>> dd58d7e59c3846e6df8d48d2d144117795b65df2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button offlineBtn = (Button) findViewById(R.id.offlineSave);

        offlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfflineDialogFragment fragment = new OfflineDialogFragment();
                fragment.show(getSupportFragmentManager(), "offline_save");
            }
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,LoginFragment.newInstance("",""))
                .commit();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
                onLoginSuccessful();
        }
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onLoginAttempted(String username, String password) {
        //animations can be done here

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ProgressFragment.newInstance(username, password))
                .commit();
    }

    @Override
    public void onOfflineSaveAttempt() {

    }


    @Override
    public void onLoginSuccessful() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailed() {
        Toast.makeText(this,"Invalid Username or password",Toast.LENGTH_LONG).show();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,LoginFragment.newInstance("",""))
                .commit();
    }

    @Override
    public void onDialogNegativeClick() {

    }

    @Override
    public void onDialogPositiveClick(String id, String course, String grade, String lab) {
        OfflineGrade offlineGrade = new OfflineGrade(id, course, grade, lab);
        offlineGrade.save();
    }
}
