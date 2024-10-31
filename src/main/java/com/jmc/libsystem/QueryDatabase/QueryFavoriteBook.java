package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Models.DatabaseDriver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryFavoriteBook {
    public static ResultSet getTotalFavorite(String userId) {
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

    public static ResultSet getListFavorite(String userId) {
        ResultSet resultSet = null;
        String query = "SELECT book.google_book_id, title, authors, thumbnail_url " +
                "FROM favorite " +
                "INNER JOIN book on book.google_book_id = favorite.google_book_id " +
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
