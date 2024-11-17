package com.jmc.libsystem.Information;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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

    public static User createUserFromResultSet(ResultSet rs) {
        try {
            String user_id = rs.getString("user_id");
            String fullName = rs.getString("fullName");
            String email = rs.getString("email");
            String password = rs.getString("password");
            String state = rs.getString("state");

            return new User(user_id, fullName, email, password, state);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Book from ResultSet", e);
        }
    }

}
