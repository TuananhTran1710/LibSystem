package com.jmc.libsystem.Models;

import java.sql.*;

public class DatabaseDriver {
    private Connection conn;

    Connection getConn(){return conn;}

    public DatabaseDriver() {
        try {
            //ket noi project voi database
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/lib_system", "root", "tuananh1710");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // User Section
    public ResultSet getUserDataForLogin(String email, String password) {
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM user WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    //lam the nay tuc la tren csdl, cac email phai khac nhau (PK) --> Can chinh lai table trong csdl
    public ResultSet getUserDataForSignUp(String email) {
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM user WHERE email = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(query);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

}
