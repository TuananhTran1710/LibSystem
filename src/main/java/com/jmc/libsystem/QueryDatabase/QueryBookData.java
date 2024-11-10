package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Models.DatabaseDriver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryBookData {
    public static ResultSet getBookForSearch(String keyWord, String typeSearch) {
        ResultSet resultSet = null;
        // no limit num of book displayed
        String query = "SELECT * FROM book WHERE " + typeSearch + " COLLATE utf8mb4_general_ci LIKE ? and state = 'publishing' ";
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
        String query = "SELECT distinct title, authors, category FROM book where state = 'publishing'";
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
                "        WHEN b.quantity > b.numBorrowing THEN 'Available' " +
                "        ELSE 'Out of stock' " +
                "    END AS status " +
                "FROM " +
                "    Book b " +
                "where b.state = 'publishing'";
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
            String query = "SELECT COUNT(*) as count FROM book where state = 'publishing'";

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
                    "where state = 'publishing' " +
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


    public static boolean isExist(String book_id) {
        String query = "SELECT EXISTS (SELECT 1 FROM book WHERE google_book_id = ?)";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, book_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getBoolean(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}

