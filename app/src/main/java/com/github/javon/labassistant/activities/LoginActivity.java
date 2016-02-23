package com.github.javon.labassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.Session;
import com.github.javon.labassistant.dialogs.OfflineDialogFragment;
import com.github.javon.labassistant.fragments.LoginFragment;
import com.github.javon.labassistant.fragments.ProgressFragment;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity
        implements LoginFragment.OnLoginAttemptedListener, ProgressFragment.OnLoginProgressListener,
        OfflineDialogFragment.OfflineDialogListener {

    @Bind(R.id.et_id_number) EditText etIdNumber;
    @Bind(R.id.et_password) EditText etPassword;
    @Bind(R.id.btn_next) Button btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (ParseUser.getCurrentUser() != null) onLoginSuccessful(); // maybe move this before bindings to no waste memory

        btnNext.setOnClickListener(v -> {
            final String id = etIdNumber.getText().toString();
            final String password = etPassword.getText().toString();

            if (password.isEmpty() || id.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill out all fields", Toast.LENGTH_LONG).show();
                return;
            }

            attemptLogin();

        });

    }

    private void attemptLogin() {



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

    /**
     * Defers login but uses saves the user's credentials and attempts
     * to verify it on Parse when a connection becomes available
     *
     * @param username
     * @param password
     */
    @Override
    public void onOfflineConnectAttempt(String username, String password) {
        Session session = new Session(this);

        session.setUsername(username);
        session.setPassword(password);
        session.setLoggedIn(false);

        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
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
//        OfflineGrade offlineGrade = new OfflineGrade(id, course, grade, lab);
//        offlineGrade.save();
    }
}
