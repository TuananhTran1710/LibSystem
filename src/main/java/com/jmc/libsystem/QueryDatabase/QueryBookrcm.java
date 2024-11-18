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
}
