package com.jmc.libsystem.Information;

public class User extends Person {
    private String state;


    public User(String id, String fullName, String email, String password, String state) {
        super(id, fullName, email, password);
        this.state = state;

    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
