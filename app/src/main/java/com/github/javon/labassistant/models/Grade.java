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
    private Student student;
    private User grader;

    public Grade() {}

    public Grade(int value, int labNumber, Student student, User grader) {
        this.value = value;
        this.labNumber = labNumber;
        this.student = student;
        this.grader = grader;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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
}
