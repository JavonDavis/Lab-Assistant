package com.github.javon.labassistant.models;

import io.realm.RealmObject;

/**
 * Created by shane on 3/5/16.
 *
 * This class represents users who can authenticate successfully using
 * the system, namely Lab Technicians.
 */
public class User extends RealmObject{

    private String username;
    private String password;

    // These are grades that labtech's have assigned

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username) {
        this(username, "");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String registrationNumber) {
        this.username = registrationNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

