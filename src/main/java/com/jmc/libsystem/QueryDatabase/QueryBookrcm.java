package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Models.DatabaseDriver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryBookrcm {
    public static ResultSet getAllPropse() {
        ResultSet resultSet = null;
        try {
            String query = "SELECT book.google_book_id, title, authors, bookrcm.state FROM bookrcm\n" +
                    "join book on bookrcm.google_book_id = book.google_book_id\n";
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void updateStatePropose(String id, String state) {
        String query = "UPDATE bookrcm SET state = ? WHERE google_book_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, state);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getProposeForFilter(String type) {
        ResultSet resultSet = null;
        // no limit num of book displayed
        String query = "SELECT book.google_book_id, title, authors, bookrcm.state FROM bookrcm\n" +
                "join book on bookrcm.google_book_id = book.google_book_id\n" +
                "where bookrcm.state = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, type);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
