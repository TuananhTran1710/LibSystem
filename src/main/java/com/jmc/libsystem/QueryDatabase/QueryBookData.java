package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Controllers.User.DashboardController;
import com.jmc.libsystem.Models.DatabaseDriver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryBookData {
    public static ResultSet getBookForSearch(String keyWord) {
        ResultSet resultSet = null;
        String type = DashboardController.typeSearch.toString();
        // no limit num of book displayed
        String query = "SELECT * FROM book WHERE " + type + " COLLATE utf8mb4_general_ci LIKE ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            // Thêm % vào từ khóa tìm kiếm cho LIKE
            preparedStatement.setString(1, "%" + keyWord + "%");
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getAllBook(String keyWord) {
        ResultSet resultSet = null;
        String type = DashboardController.typeSearch.toString();
        // no limit num of book displayed
        String query = "SELECT * FROM book";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getBookStatistic() {
        ResultSet resultSet = null;
        String query = "SELECT b.thumbnail_url, b.title, b.authors, b.quantity AS total_books, " +
                "COUNT(bl.history_id) AS total_borrowed_books " +
                "FROM book b LEFT JOIN bookloans bl ON b.google_book_id = bl.google_book_id " +
                "GROUP BY b.google_book_id, b.title, b.authors;";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getCountBook(){
        ResultSet resultSet = null;
        try {
            String query = "SELECT COUNT(*) as count FROM book;";

            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

}

