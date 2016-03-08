package com.github.javon.labassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.security.token.TokenGenerator;
import com.github.javon.labassistant.R;
import com.github.javon.labassistant.activities.students.ListStudentsActivity;
import com.github.javon.labassistant.events.auth.FailedAuthenticationEvent;
import com.github.javon.labassistant.events.auth.LoginEvent;
import com.github.javon.labassistant.models.Session;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

        final String username = "620065739";
        final String password = "SR892";

        Map<String, Object> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("password", password);

        TokenGenerator generator = new TokenGenerator(getString(R.string.firebase_secret));
        String token = generator.createToken(new JSONObject(payload));

        myRef.authWithCustomToken(token, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                EventBus.getDefault().post(new LoginEvent(username, password, authData.getToken()));
            }

            @Override
            public void onAuthenticationError(FirebaseError error) {
                Toast.makeText(LoginActivity.this, "error sign in: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


//        mSession = new Session(this);
//
//        if (mSession.isLoggedIn()) {
//            startActivity(new Intent(this, ListStudentsActivity.class));
//            finish();
//        }
//
//
//        btnNext.setOnClickListener(v -> {
//            final String id = etIdNumber.getText().toString();
//            final String password = etPassword.getText().toString();
//
//            if (id.isEmpty() || password.isEmpty()) {
//                Toast.makeText(LoginActivity.this, "Both the username and password must be entered", Toast.LENGTH_LONG).show();
//                return;
//            }
//
////            ref.push().setValue(new User(id, password));
//
//            ref.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Log.d(TAG, dataSnapshot.getChildrenCount() + " users");
//
//                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
//                        User user = userSnapshot.getValue(User.class);
//                        if (id.equals(user.getRegistrationNumber())) {
//                            EventBus.getDefault().post(new LoginEvent(user.getRegistrationNumber(), user.getPassword()));
//                            Log.d(TAG, "Here with id: " + id + " and " + user.getRegistrationNumber());
//                            return;
//                        }
//                    }
//                    EventBus.getDefault().post(new FailedAuthenticationEvent());
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    Log.d(TAG, "The read failed: " + firebaseError.getMessage());
//                }
//            });
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
        Session session = new Session(this);

        session.setUsername(event.getUsername());
        session.setPassword(event.getPassword());
        session.setToken(event.getToken());
        session.setLoggedIn(true);

        loginSuccessful();
    }

    @Subscribe
    public void onMessageEvent(FailedAuthenticationEvent event) {
        Toast.makeText(this, "Invalide credentials", Toast.LENGTH_LONG).show();
    }

    public void loginSuccessful() {
        Intent intent = new Intent(this, ListStudentsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
