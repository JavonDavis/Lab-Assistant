package com.github.javon.labassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.fragments.LoginFragment;
import com.github.javon.labassistant.fragments.ProgressFragment;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnLoginAttemptedListener, ProgressFragment.OnLoginProgressListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,LoginFragment.newInstance())
                .commit();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            onLoginSuccessful();
        }
    }

    @Override
    public void onLoginAttempted(String username, String password) {
        //animations can be done here
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ProgressFragment.newInstance(username,password))
                .commit();
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
                .replace(R.id.container,LoginFragment.newInstance())
                .commit();
    }
}
