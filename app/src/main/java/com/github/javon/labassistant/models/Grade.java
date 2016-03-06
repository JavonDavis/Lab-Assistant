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
    private Student student;
    private User labtech; // grader

    public Grade() {}

    public Grade(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
