package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Controllers.User.DashboardController;
import com.jmc.libsystem.Models.DatabaseDriver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryUserBook {
    public static ResultSet Borrow (String userId) {
        ResultSet resultSet = null;
        String query = "SELECT COUNT(*) AS total_borrows " +
                "FROM borrow_history " +
                "WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, userId);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet Return (String userId) {
        ResultSet resultSet = null;
        String query = "SELECT COUNT(CASE WHEN return_date IS NOT NULL THEN 1 END) AS total_returns " +
                "FROM borrow_history " +
                "WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, userId);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
