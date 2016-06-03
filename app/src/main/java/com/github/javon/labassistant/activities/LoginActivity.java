package com.github.javon.labassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.classes.helpers.Constants;
import com.github.javon.labassistant.classes.helpers.OfflineGrade;
import com.github.javon.labassistant.classes.helpers.dialogs.OfflineDialogFragment;
import com.github.javon.labassistant.fragments.LoginFragment;
import com.github.javon.labassistant.fragments.ProgressFragment;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnLoginAttemptedListener,
        ProgressFragment.OnLoginProgressListener,OfflineDialogFragment.OfflineDialogListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        if(Constants.isNetworkAvailable(this)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ProgressFragment.newInstance(username, password))
                    .commit();
        }
        else
        {
            Toast.makeText(this,"No network Connection",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onOfflineGradeClick() {
        OfflineDialogFragment dialogFragment = new OfflineDialogFragment();
        dialogFragment.show(getSupportFragmentManager(),"dialog");
    }


    @Override
    public void onLoginSuccessful() {
        syncOfflineGrades();
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void syncOfflineGrades()
    {
        List<OfflineGrade>  offlineGrades = OfflineGrade.listAll(OfflineGrade.class);
        for(final OfflineGrade offlineGrade:offlineGrades)
        {
            String idNumber = offlineGrade.idNumber;
            String mGradeTableName = String.format(Locale.US,"%s_Grades",offlineGrade.course);
            final String lab_key = String.format(Locale.US,"lab_%s",offlineGrade.lab);
            ParseQuery<ParseObject> query = ParseQuery.getQuery(mGradeTableName);
            query.whereEqualTo("id_number",idNumber);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if(e == null)
                    {
                        parseObject.put(lab_key,offlineGrade.grade);
                        parseObject.saveInBackground();
                    }
                    else
                    {
                       Log.e("Error syncing","Error:"+e.getCode()+"-"+e.getMessage());
                    }
                }
            });
        }

    }
    @Override
    public void onLoginFailed(int code) {
        //TODO - give proper message based on code
        Toast.makeText(this,"No internet connection or invalid Username or password",Toast.LENGTH_LONG).show();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,LoginFragment.newInstance("",""))
                .commit();
    }

    @Override
    public void onDialogPositiveClick(String id, String course, String grade, String lab) {
        OfflineGrade offlineGrade = new OfflineGrade(id,course,grade,lab);
        offlineGrade.save();
    }

    @Override
    public void showAllGrades() {
        Intent intent  =  new Intent(this,OfflineActivity.class);
        startActivity(intent);
    }
}
