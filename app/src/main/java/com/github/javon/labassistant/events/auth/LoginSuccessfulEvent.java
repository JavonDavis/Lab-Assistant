package com.github.javon.labassistant.events.auth;

/**
 * Created by shane on 3/5/16.
 */
public class LoginSuccessfulEvent {

    private final String username, password;

    public LoginSuccessfulEvent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
