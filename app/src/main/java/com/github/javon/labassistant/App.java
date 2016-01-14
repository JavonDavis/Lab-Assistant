package com.github.javon.labassistant;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/29/15.
 */
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();


        String appId = getResources().getString(R.string.application_id);
        String clientId = getResources().getString(R.string.client_id);

        // Enable Local Datastore.
        //Parse.enableLocalDatastore(this);

        Parse.initialize(this, appId, clientId);
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }
}
