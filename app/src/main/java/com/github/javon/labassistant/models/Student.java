package com.github.javon.labassistant.models;

import java.util.List;

/**
 * Created by shane on 3/5/16.
 */
public class Student {

    private String registrationNumber;
    private String firstName;
    private String lastName;

    // Relationships
    private List<Grade> grades;
    private List<Course> courses;

    // Required by both firebase and realm
    public Student() {}

    public Student(String registrationNumber, String firstName, String lastName) {
        this.registrationNumber = registrationNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
