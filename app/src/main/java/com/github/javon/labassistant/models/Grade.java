package com.github.javon.labassistant.models;

import io.realm.RealmObject;

/**
 * Created by shane on 3/5/16.
 */
public class Grade extends RealmObject {

    private int value;

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
