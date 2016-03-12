package com.github.javon.labassistant.models.builder;

import com.github.javon.labassistant.models.User;

/**
 * Created by shane on 3/12/16.
 */
public class UserBuilder {

    public final String username;
    public final String password;

    public String firstName;
    public String lastName;

    public UserBuilder(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public User build() {
        return new User(this);
    }
}
