package com.github.javon.labassistant.models;

import java.util.List;

/**
 * Created by shane on 3/5/16.
 */
public class Student {

    private String registrationNumber;
    private List<Grade> grades;

    // Required by both firebase and realm
    public Student() {}

    public Student(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
