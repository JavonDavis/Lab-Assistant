package com.github.javon.labassistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by shane on 2/8/16.
 */
public class Session {

    private SharedPreferences prefs;

    public Session(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setUsername(String username) {
        save("username", username);
    }

    public void setPassword(String password) {
        save("password", password);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        save("loggedIn", isLoggedIn);
    }

    public String getUsername() {
        return retrieveString("username");
    }

    public String getPassword() {
        return retrieveString("password");
    }

    private void save(String label, String content) {
        prefs.edit().putString(label, content).apply();
    }

    private void save(String label, boolean content) {
        prefs.edit().putBoolean(label, content).apply();
    }

    private String retrieveString(String label, String def) {
        return prefs.getString(label, def);
    }

    private String retrieveString(String label) {
        return retrieveString(label, "");
    }

    private boolean retrieveBoolean(String label) {
        return prefs.getBoolean(label, false);
    }
}
