package com.github.javon.labassistant.classes.helpers;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

@Table
public class OfflineGrade extends SugarRecord {
    public String idNumber;
    public String course;
    public String grade;
    public String lab;

    public OfflineGrade() {}

    public OfflineGrade(String id, String course, String grade, String lab) {
        this.idNumber = id;
        this.course = course;
        this.grade = grade;
        this.lab = lab;
    }
}
