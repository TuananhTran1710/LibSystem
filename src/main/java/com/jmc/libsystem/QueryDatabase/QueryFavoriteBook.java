package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Models.DatabaseDriver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryFavoriteBook {
    public static ResultSet Favorite (String userId) {
        ResultSet resultSet = null;
        String query = "SELECT COUNT(*) AS total_favorite " +
                "FROM favorite " +
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

    public static ResultSet getListFavorite (String userId) {
        ResultSet resultSet = null;
        String query = "SELECT title, authors, thumbnail_url " +
                "FROM favorite " +
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
