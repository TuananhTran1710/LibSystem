package com.jmc.libsystem.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseDriver {
    private static Connection conn;

    public static Connection getConn() {
        return conn;
    }

    public DatabaseDriver() {
        try {
            //ket noi project voi database
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/lib_system", "root", "tuananh1710");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
