package com.github.javon.labassistant.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by shane on 2/8/16.
 */
public class Session {

    private final static String KEY_TOKEN = "token";
    private final static String KEY_USERNAME = "username";
    private final static String KEY_PASSWORD = "password";
    private final static String KEY_LOGGED_IN = "loggedIn";

    private SharedPreferences prefs;

    public Session(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Session newInstance(Context ctx) {
        return new Session(ctx);
    }

    public void setUsername(String username) {
        save(KEY_USERNAME, username);
    }

    public void setPassword(String password) {
        save(KEY_PASSWORD, password);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        save(KEY_LOGGED_IN, isLoggedIn);
    }

    public void setToken(String token) {
        save(KEY_TOKEN, token);
    }

    public String getUsername() {
        return retrieveString(KEY_USERNAME);
    }

    public String getPassword() {
        return retrieveString(KEY_PASSWORD);
    }

    public boolean isLoggedIn() {
        return retrieveBoolean(KEY_LOGGED_IN);
    }

    public String getToken() {
        return retrieveString(KEY_TOKEN);
    }

    public void logout() {
        prefs.edit().clear().apply();
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

    public void login(String id, String password) {
        setUsername(id);
        setPassword(password);
        setLoggedIn(true);
    }

}
