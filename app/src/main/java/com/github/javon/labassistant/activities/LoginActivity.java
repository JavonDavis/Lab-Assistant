package com.github.javon.labassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.javon.labassistant.R;
import com.github.javon.labassistant.Session;
import com.github.javon.labassistant.activities.grades.ListGradesActivity;
import com.github.javon.labassistant.events.FailedAuthenticationEvent;
import com.github.javon.labassistant.events.LoginEvent;
import com.github.javon.labassistant.models.User;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity {

    public final String TAG = LoginActivity.this.getClass().getName();

    @Bind(R.id.et_id_number) EditText etIdNumber;
    @Bind(R.id.et_password) EditText etPassword;
    @Bind(R.id.btn_next) Button btnNext;

    Firebase ref = new Firebase("https://labtech.firebaseio.com/users");

    private boolean mConnected = false;
    private Session mSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mSession = new Session(this);

        if (mSession.isLoggedIn()) {
            startActivity(new Intent(this, ListGradesActivity.class));
            finish();
        }


        btnNext.setOnClickListener(v -> {
            final String id = etIdNumber.getText().toString();
            final String password = etPassword.getText().toString();

            if (id.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Both the username and password must be entered", Toast.LENGTH_LONG).show();
                return;
            }

//            ref.push().setValue(new User(id, password));

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, dataSnapshot.getChildrenCount() + " users");

                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (id.equals(user.getRegistrationNumber())) {
                            EventBus.getDefault().post(new LoginEvent(user.getRegistrationNumber(), user.getPassword()));
                            Log.d(TAG, "Here with id: " + id + " and " + user.getRegistrationNumber());
                            return;
                        }
                    }
                    EventBus.getDefault().post(new FailedAuthenticationEvent());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d(TAG, "The read failed: " + firebaseError.getMessage());
                }
            });
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


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onMessageEvent(LoginEvent event) {
        mSession.setUsername(event.getRegistrationNumber());
        mSession.setPassword(event.getPassword());
        mSession.setLoggedIn(true);

        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Subscribe
    public void onMessageEvent(FailedAuthenticationEvent event) {
        Toast.makeText(this, "Invalide credentials", Toast.LENGTH_LONG).show();
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
