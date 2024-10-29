package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Models.DatabaseDriver;
import com.jmc.libsystem.Views.StateAccount;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryBookLoans {
    public static boolean isBanned(String user_id) {
        String query = "SELECT * FROM bookloans WHERE user_id = ? AND DATEDIFF(CURDATE(), borrow_date) > 120";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Nếu có bản ghi trả về, có nghĩa là có lần mượn quá hạn
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateUserBanned() {
        String query = "SELECT user_id FROM bookloans WHERE DATEDIFF(CURDATE(), borrow_date) > 120";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                QueryAccountData.updateState(StateAccount.BANNED.toString(), resultSet.getString(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void noticeBookOverdue(String user_id) {
        String query = "SELECT count(*) FROM bookloans WHERE user_id = ? AND DATEDIFF(CURDATE(), borrow_date) > 60";

        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                int cnt = resultSet.getInt(1);

                Alert alert = new Alert(Alert.AlertType.WARNING);

                alert.setContentText("You have " + cnt + " overdue books. Please visit MyBook section and return the books to the library soon!");

                alert.setTitle("Notice");

                alert.show();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet Borrow(String userId) {
        ResultSet resultSet = null;
        String query = "SELECT COUNT(*) AS total_borrows " +
                "FROM bookloans " +
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

    public static ResultSet Return(String userId) {
        ResultSet resultSet = null;
        String query = "SELECT COUNT(CASE WHEN return_date IS NOT NULL THEN 1 END) AS total_returns " +
                "FROM bookloans " +
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
    
    public static ResultSet getListBorrow (String userId) {
        ResultSet resultSet = null;
        String query = "SELECT title, authors, thumbnail_url " +
                "FROM bookloans " +
                "INNER JOIN book on book.google_book_id = bookloans.google_book_id " +
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
