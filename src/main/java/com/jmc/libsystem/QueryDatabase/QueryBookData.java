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
            String query = "WITH category_split AS (\n" +
                    "    SELECT \n" +
                    "        TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(category, ',', numbers.n), ',', -1)) AS genre,\n" +
                    "        quantity\n" +
                    "    FROM \n" +
                    "        book\n" +
                    "    JOIN \n" +
                    "        (SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 \n" +
                    "         UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10) numbers\n" +
                    "        ON CHAR_LENGTH(category) - CHAR_LENGTH(REPLACE(category, ',', '')) >= numbers.n - 1\n" +
                    ")\n" +
                    "SELECT \n" +
                    "    genre as category,\n" +
                    "    SUM(quantity) AS total_books\n" +
                    "FROM \n" +
                    "    category_split\n" +
                    "GROUP BY \n" +
                    "    genre\n" +
                    "ORDER BY \n" +
                    "    total_books DESC\n" +
                    "limit 3;\n";

            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

}

