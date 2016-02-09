package com.github.javon.labassistant.models;

import com.parse.ParseObject;

/**
 *
 * Created by shane on 2/8/16.
 */
public class Grade extends ParseObject{
    private String code;
    private String registrationNumber;
    private int labNumber;
    private int mark;

    public Grade(String registrationNumber, int mark, String code, int lab) {
        add("code", code);

        this.registrationNumber = registrationNumber;
        this.mark = mark;
        this.code = code;
        this.labNumber = lab;
    }
}
