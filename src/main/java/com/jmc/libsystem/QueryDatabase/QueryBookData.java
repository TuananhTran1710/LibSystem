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

    public static ResultSet getDataForSuggest() {
        ResultSet resultSet = null;
        String query = "SELECT distinct title, authors, category FROM book";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // truy van co van de ?
    public static ResultSet getBookStatistic() {
        ResultSet resultSet = null;
        String query = "SELECT b.google_book_id, b.thumbnail, b.title, b.authors, " +
                "    b.quantity AS total_books, " +
                "    b.totalLoan AS total_borrowed_books, " +
                "    CASE " +
                "        WHEN b.quantity > b.totalLoan THEN 'Available' " +
                "        ELSE 'Over' " +
                "    END AS status " +
                "FROM " +
                "    Book b";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getCountBook() {
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

    public static ResultSet getCountBookLoan() {
        ResultSet resultSet = null;
        try {
            String query = "SELECT sum(totalLoan) as count FROM book;";

            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getCategorySort3() {
        ResultSet resultSet = null;
        try {
            String query = "SELECT category, sum(quantity) AS total_books " +
                    "FROM book " +
                    "GROUP BY category " +
                    "ORDER BY total_books DESC " +
                    "LIMIT 3;";

            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

}

