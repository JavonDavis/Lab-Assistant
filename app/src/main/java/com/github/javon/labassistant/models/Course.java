package com.github.javon.labassistant.models;

import io.realm.RealmObject;

/**
 * Created by shane on 3/5/16.
 */
public class Course extends RealmObject {

    private String name;

    public Course() {}

    public Course(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
