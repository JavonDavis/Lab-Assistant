package com.github.javon.labassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.github.javon.labassistant.R;
import com.github.javon.labassistant.Session;
import com.github.javon.labassistant.models.User;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.et_id_number) EditText etIdNumber;
    @Bind(R.id.et_password) EditText etPassword;
    @Bind(R.id.btn_next) Button btnNext;

    Firebase myFirebaseRef = new Firebase("https://labtech.firebaseio.com/");

    private boolean mConnected = false;
    private Session mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnNext.setOnClickListener(v -> {
            String id = etIdNumber.getText().toString();
            String password = etPassword.getText().toString();

            User user = new User(id, password);

            Firebase userRef = myFirebaseRef.child("users").child(id);
            userRef.setValue(user);
        });

//        if (ParseUser.getCurrentUser() != null) onLoginSuccessful(); // maybe move this before bindings to no waste memory
//        mConnected = NetworkUtil.isNetworkAvailable(this);
//        mSession = new Session(this);
//
//        btnNext.setOnClickListener(v -> {
//            final String id = etIdNumber.getText().toString();
//            final String password = etPassword.getText().toString();
//
//            if (password.isEmpty() || id.isEmpty()) {
//                Toast.makeText(LoginActivity.this, "Please fill out all fields", Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            attemptLogin(id, password);
//
//        });
    }

    /**
     * There will be a sync issue between session and parse user object
     *
     * @param id
     * @param password
     */
    private void attemptLogin(String id, String password) {
        if (mConnected) {
            ParseUser.logInInBackground(id, password, (user, e) -> {
                if (e != null || user == null) {
                    Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_LONG).show();
                    return;
                }
                mSession.logout();
            });
        } else {
            mSession.login(id, password);
        }

        if (mSession.isLoggedIn() || ParseUser.getCurrentUser() != null)
            onLoginSuccessful();
    }

    public void onLoginSuccessful() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
