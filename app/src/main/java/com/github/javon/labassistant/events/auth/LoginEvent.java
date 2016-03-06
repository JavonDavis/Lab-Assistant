package com.github.javon.labassistant.events.auth;

/**
 * Created by shane on 3/5/16.
 */
public class LoginEvent {

    public final String registrationNumber, password;

    public LoginEvent(String registrationNumber, String password) {
        this.registrationNumber = registrationNumber;
        this.password = password;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getPassword() {
        return password;
    }
}
