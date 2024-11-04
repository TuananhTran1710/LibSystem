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

    // truy van co van de ?
    public static ResultSet getBookStatistic() {
        ResultSet resultSet = null;
        String query = "SELECT" +
                "  b.google_book_id, b.thumbnail_url,b.title,b.authors, b.quantity AS total_books" +
                "    ,COUNT(bl.history_id) AS total_borrowing," +
                "    CASE " +
                "        WHEN b.quantity > COUNT(bl.history_id) THEN 'Còn sách'\n" +
                "        ELSE 'Hết sách'\n" +
                "    END AS status\n" +
                "FROM \n" +
                "    Book b\n" +
                "LEFT JOIN \n" +
                "    BookLoans bl ON b.google_book_id = bl.google_book_id AND bl.return_date IS NULL\n" +
                "GROUP BY \n" +
                "    b.google_book_id, b.title, b.authors, b.thumbnail_url, b.quantity;\n";
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

