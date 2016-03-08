package com.github.javon.labassistant.events.auth;

/**
 * Created by shane on 3/5/16.
 */
public class LoginEvent {

    private final String username, password, token;

    public LoginEvent(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
