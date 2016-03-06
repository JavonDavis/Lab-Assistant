package com.github.javon.labassistant.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.github.javon.labassistant.models.Session;
import com.github.javon.labassistant.models.parse.Grade;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by shane on 2/10/16.
 */
public class ConnectionEstablishedReceiver extends BroadcastReceiver {

    private static String TAG = ConnectionEstablishedReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Session session = new Session(context);

        // user is already logged in as a parseuser
        // or not logged in at all
        if (! session.isLoggedIn() || null != ParseUser.getCurrentUser()) return;

        String username = session.getUsername();
        String password = session.getPassword();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) {
                mBuilder.setContentTitle("Failed to sync grades");
                mBuilder.setContentText("Invalid login credentials");
            } else {
                attemptToSyncGrades(user);
                mBuilder.setContentTitle("Grades synced");
            }

            final NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(999, mBuilder.build());
        });
    }

    private void attemptToSyncGrades(ParseUser parseUser) {
        final ParseQuery<Grade> localGradeQuery = ParseQuery.getQuery(Grade.class);

        localGradeQuery.fromLocalDatastore();
        localGradeQuery.findInBackground((grades, e) -> {
            if (e != null)
                Log.e(TAG, e.getMessage());
            else
                for (Grade grade : grades) {
                    saveGrade(parseUser, grade);
                    grade.unpinInBackground(); // remove grade
                }
        });
    }

    private void saveGrade(ParseUser user, Grade grade) {
        grade.setUser(user);
        grade.saveInBackground(e -> {
            if (e != null) {
                // attempt to save it again
            }
        });
    }
}
