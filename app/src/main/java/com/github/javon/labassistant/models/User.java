package com.github.javon.labassistant.models;

import com.github.javon.labassistant.models.builder.UserBuilder;

/**
 * Created by shane on 3/5/16.
 *
 * This class represents users who can authenticate successfully using
 * the system, namely Lab Technicians.
 */
public class User {

    private String firstName;
    private String lastName;
    private String username;
    private String password;

    public User() {
        // required by firebase
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(UserBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

