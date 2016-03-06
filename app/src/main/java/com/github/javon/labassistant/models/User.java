package com.github.javon.labassistant.models;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by shane on 3/5/16.
 *
 * This class represents users who can authenticate successfully using
 * the system, namely Lab Technicians.
 */
public class User extends RealmObject{

    private String registrationNumber;
    private String password;

    // These are grades that labtech's have assigned
    private List<Grade> grades;

    public User() {}

    public User(String registrationNumber, String password) {
        this.registrationNumber = registrationNumber;
        this.password = password;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
