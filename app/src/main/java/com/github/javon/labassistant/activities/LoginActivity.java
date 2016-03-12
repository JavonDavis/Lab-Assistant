package com.github.javon.labassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkUserLogin(); // skips setting up the view if the user is already logs in

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

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
        final Session session = new Session(this);

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
        final Intent intent = new Intent(this, ListStudentsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void checkUserLogin() {
        if (! Session.newInstance(this).isLoggedIn()) return; // guard

        startActivity(new Intent(this, ListStudentsActivity.class));
        finish();
    }

    private void attemptManualLogin() {
        final String username = etIdNumber.getText().toString();
        final String password = etPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Enter username and password", Toast.LENGTH_LONG).show();
            return;
        }

        final Firebase userRef = new Firebase("https://labtech.firebaseio.com/users");

        Query queryRef = userRef.orderByChild("username").equalTo(username);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);

                if (! user.getPassword().equals(password)) {
                    EventBus.getDefault().post(new LoginFailedEvent());
                    return;
                }

                Toast.makeText(LoginActivity.this, "Username " + user.getUsername() + " password " + user.getPassword() , Toast.LENGTH_LONG).show();
                EventBus.getDefault()
                        .post(new LoginSuccessfulEvent(user.getUsername(), user.getPassword()));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // nothing
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // nothing
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // nothing
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // nothing
            }
        });
    }
}
