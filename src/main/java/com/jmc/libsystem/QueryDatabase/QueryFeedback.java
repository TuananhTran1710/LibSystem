package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Models.DatabaseDriver;
import com.jmc.libsystem.Models.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryFeedback {
    public static ResultSet isCommented(String book_id) {
        ResultSet resultSet = null;
        String query = "select * from feedback where user_id = ? and google_book_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, Model.getInstance().getMyUser().getId());
            preparedStatement.setString(2, book_id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet setAllComment(String book_id) {
        ResultSet resultSet = null;
        String query = "select * from feedback where google_book_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, book_id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void modifyComment(String book_id, String text, int rating) {
        String query = "update feedback set comment = ?, rating = ? where user_id = ? and google_book_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, text.trim());
            preparedStatement.setInt(2, rating);
            preparedStatement.setString(3, Model.getInstance().getMyUser().getId());
            preparedStatement.setString(4, book_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteComment(String book_id) {
        String query = "delete from feedback where user_id = ? and google_book_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, Model.getInstance().getMyUser().getId());
            preparedStatement.setString(2, book_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertNewComment(String book_id, String text, int rating) {
        String query = "insert into feedback (google_book_id, user_id, rating, comment) values (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, book_id);
            preparedStatement.setString(2, Model.getInstance().getMyUser().getId());
            preparedStatement.setInt(3, rating);
            preparedStatement.setString(4, text.trim());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
