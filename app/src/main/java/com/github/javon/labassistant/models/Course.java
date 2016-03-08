package com.github.javon.labassistant.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by shane on 3/5/16.
 *
 * This class represents the each course that has accompanying labs.
 * It acts similarly to a pivot table, in that it links to both the
 * Student and Grade entities in dependent relationship.
 */
public class Course extends RealmObject {

    private String name;

    // Relationships
    private RealmList<Student> students;
    private RealmList<Grade> grades;

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

    public RealmList<Student> getStudents() {
        return students;
    }

    public void setStudents(RealmList<Student> students) {
        this.students = students;
    }

    public RealmList<Grade> getGrades() {
        return grades;
    }

    public void setGrades(RealmList<Grade> grades) {
        this.grades = grades;
    }
}
