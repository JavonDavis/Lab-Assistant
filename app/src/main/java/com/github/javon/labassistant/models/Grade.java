package com.github.javon.labassistant.models;

import io.realm.RealmObject;

/**
 * Created by shane on 3/5/16.
 *
 * This class represents each grade assigned to a student for each lab
 * attempted.
 */
public class Grade extends RealmObject {

    private int value;
    private int labNumber;
    private String timestamp;
    private User grader;

    public Grade() {}

    public Grade(int value, int labNumber, String timestamp, User grader) {
        this.value = value;
        this.labNumber = labNumber;
        this.timestamp = timestamp;
        this.grader = grader;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getLabNumber() {
        return labNumber;
    }

    public void setLabNumber(int lab) {
        this.labNumber = lab;
    }

    public User getGrader() {
        return grader;
    }

    public void setGrader(User grader) {
        this.grader = grader;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
