package com.github.javon.labassistant;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

@Table
public class OfflineGrade extends SugarRecord {
    String id;
    String course;
    String grade;
    String lab;

    public OfflineGrade() {}

    public OfflineGrade(String id, String course, String grade, String lab) {
        this.id = id;
        this.course = course;
        this.grade = grade;
        this.lab = lab;
    }
}
