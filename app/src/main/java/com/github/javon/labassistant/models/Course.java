package com.github.javon.labassistant.models;

/**
 * Created by shane on 3/5/16.
 *
 * This class represents the each course that has accompanying labs.
 * It acts similarly to a pivot table, in that it links to both the
 * Student and Grade entities in dependent relationship.
 */
public class Course {

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
