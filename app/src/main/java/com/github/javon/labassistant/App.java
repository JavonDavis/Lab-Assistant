package com.github.javon.labassistant;

import android.app.Application;

import com.firebase.client.Firebase;
import com.github.javon.labassistant.models.parse.Grade;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/29/15.
 */
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

        String appId = getResources().getString(R.string.application_id);
        String clientId = getResources().getString(R.string.client_id);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, appId, clientId);

        ParseObject.registerSubclass(Grade.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }
}
