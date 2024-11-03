package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Models.DatabaseDriver;
import com.jmc.libsystem.Models.Model;

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
        String query = "SELECT * " +
                "FROM favorite " +
                "natural JOIN book " +
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

    public static boolean isFavorite(String book_id) {
        ResultSet resultSet;
        String query = "SELECT * " +
                "FROM favorite " +
                "WHERE user_id = ? and google_book_id = ? ";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, Model.getInstance().getMyUser().getId());
            preparedStatement.setString(2, book_id);
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void insertNewRecord(String book_id) {
        String query = "INSERT INTO favorite (user_id, google_book_id) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, Model.getInstance().getMyUser().getId());
            preparedStatement.setString(2, book_id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRecord(String book_id) {
        String query = "DELETE FROM favorite WHERE user_id = ? AND google_book_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, Model.getInstance().getMyUser().getId());
            preparedStatement.setString(2, book_id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
