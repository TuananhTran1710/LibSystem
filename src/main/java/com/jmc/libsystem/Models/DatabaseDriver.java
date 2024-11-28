package com.jmc.libsystem.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseDriver {
    private static Connection conn;

    public static Connection getConn() {
        try {
            if (conn == null || conn.isClosed()) {

                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lib_system", "cuong", "cuongchelsea");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public DatabaseDriver() {
        try {
            //ket noi project voi database

            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lib_system", "cuong", "cuongchelsea");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
