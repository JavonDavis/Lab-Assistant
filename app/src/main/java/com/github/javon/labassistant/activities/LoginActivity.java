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
import com.github.javon.labassistant.activities.students.ListStudentsActivity;
import com.github.javon.labassistant.events.auth.LoginFailedEvent;
import com.github.javon.labassistant.events.auth.LoginSuccessfulEvent;
import com.github.javon.labassistant.models.Session;
import com.github.javon.labassistant.models.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity {

    public final String TAG = LoginActivity.this.getClass().getName();

    @Bind(R.id.et_id_number) EditText etIdNumber;
    @Bind(R.id.et_password) EditText etPassword;
    @Bind(R.id.btn_next) Button btnNext;

    Firebase myRef = new Firebase("https://labtech.firebaseio.com");

    private boolean mConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Session session = new Session(this);

        if (session.isLoggedIn()) {
            startActivity(new Intent(this, ListStudentsActivity.class));
            finish();
        }

        btnNext.setOnClickListener(v -> attemptManualLogin());
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
    public void onMessageEvent(LoginSuccessfulEvent event) {
        Session session = new Session(this);

        session.setUsername(event.getUsername());
        session.setPassword(event.getPassword());
        session.setLoggedIn(true);

        loginSuccessful();
    }

    @Subscribe
    public void onMessageEvent(LoginFailedEvent event) {
        Toast.makeText(this, "Invalide credentials", Toast.LENGTH_LONG).show();
    }

    public void loginSuccessful() {
        Intent intent = new Intent(this, ListStudentsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void attemptManualLogin() {
        final String username = etIdNumber.getText().toString();
        final String password = etPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Both the username and password must be entered", Toast.LENGTH_LONG).show();
            return;
        }

        myRef.push().setValue(new User(username, password));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.getChildrenCount() + " users");

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (username.equals(user.getUsername())) {
                        EventBus.getDefault().post(new LoginSuccessfulEvent(user.getUsername(), user.getPassword()));
                        Log.d(TAG, "Here with username: " + username);
                        return;
                    }
                }
                EventBus.getDefault().post(new LoginFailedEvent());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "The read failed: " + firebaseError.getMessage());
            }
        });
    }
}
