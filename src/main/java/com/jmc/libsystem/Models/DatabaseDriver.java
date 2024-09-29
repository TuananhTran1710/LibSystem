package com.jmc.libsystem.Models;

import java.sql.*;

public class DatabaseDriver {
    private Connection conn;

    public DatabaseDriver() {
        try {
            //ket noi project voi database
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/lib_system", "root", "tuananh1710");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // User Section
    public ResultSet getClientData(String username, String password) {
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

}
