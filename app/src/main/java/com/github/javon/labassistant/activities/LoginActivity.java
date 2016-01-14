package com.github.javon.labassistant.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.TextView;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.fragments.LoginFragment;
import com.github.javon.labassistant.fragments.ProgressFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnLoginAttemptedListener, ProgressFragment.OnLoginProgressListener {

    private TextView usernameField;
    private TextView passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,LoginFragment.newInstance())
                .commit();
    }

    @Override
    public void onLoginAttempted(String username, String password) {
        //animations can be done here
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ProgressFragment())
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

    }
}
