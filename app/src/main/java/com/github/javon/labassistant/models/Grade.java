package com.github.javon.labassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 *
 * Created by shane on 2/8/16.
 */
@ParseClassName("Grade")
public class Grade extends ParseObject {

    public Grade() {
    }

    public Grade(String registrationNumber, int mark, String code, int lab, ParseUser user) {
        put("registrationNumber", registrationNumber);
        put("mark", mark);
        put("code", code);
        put("labNumber", lab);
        put("creator", user);
    }

    public Grade(String registrationNumber, int mark, String code, int lab) {
        put("registrationNumber", registrationNumber);
        put("mark", mark);
        put("code", code);
        put("labNumber", lab);
    }

    public String getRegistrationNumber() {
        return getString("registrationNumber");
    }

    public String getCourseCode() {
        return getString("code");
    }

    public int getMark() {
        return getInt("mark");
    }

    public int getLabNumber() {
        return getInt("labNumber");
    }

    public ParseUser getUser() {
        return getParseUser("creator");
    }

    public void setUser(ParseUser user) {
        put("creator", user);
    }
}
