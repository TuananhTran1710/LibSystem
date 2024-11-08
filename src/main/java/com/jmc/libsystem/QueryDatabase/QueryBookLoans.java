package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Models.DatabaseDriver;
import com.jmc.libsystem.Models.Model;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class QueryBookLoans {

    public static void noticeBookOverdue(String user_id) {
        String query = "SELECT count(*) FROM bookloans WHERE user_id = ? AND DATEDIFF(CURDATE(), borrow_date) > 60";

        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                int cnt = resultSet.getInt(1);

                Alert alert = new Alert(Alert.AlertType.WARNING);

                alert.setContentText("You have " + cnt + " overdue bookSearch. Please visit MyBook section and return the bookSearch to the library soon!");

                alert.setTitle("Notice");

                alert.show();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet getTotalLoaned(String userId) {
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

    public static ResultSet getTotalReturned(String userId) {
        ResultSet resultSet = null;
        String query = "SELECT COUNT(*) AS total_returns " +
                "FROM bookloans " +
                "WHERE user_id = ? AND return_date IS NOT NULL";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, userId);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getListBorrowing(String userId) {
        ResultSet resultSet = null;
        String query = "SELECT * " +
                "FROM bookloans " +
                "natural JOIN book " +
                "WHERE user_id = ? and DATEDIFF(CURDATE(), borrow_date) < 60 and return_date is null";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, userId);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static boolean isBorrowing(String book_id) {
        ResultSet resultSet;
        String query = "SELECT * " +
                "FROM bookloans " +
                "WHERE user_id = ? and google_book_id = ? and return_date is null";
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
        String query = "INSERT INTO bookloans (user_id, google_book_id, borrow_date, due_date) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, Model.getInstance().getMyUser().getId());
            preparedStatement.setString(2, book_id);
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now().plusDays(60)));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateReturnBook(String book_id) {
        String query = "update bookloans set return_date = ? where google_book_id = ? and user_id = ? and return_date is null";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
            preparedStatement.setString(2, book_id);
            preparedStatement.setString(3, Model.getInstance().getMyUser().getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getCountOverdue() {
        ResultSet resultSet = null;
        String query = "SELECT COUNT(*) AS count " +
                "FROM bookloans " +
                "WHERE return_date IS NULL  " +
                "  AND DATEDIFF(CURDATE(), borrow_date) > 60;";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void setRemainingTime(Label time_lbl, String book_id, String user_id) {
        ResultSet resultSet = null;
        String query = "SELECT * " +
                "FROM bookloans " +
                "WHERE user_id = ? and google_book_id = ? and return_date is null";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, user_id);
            preparedStatement.setString(2, book_id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (resultSet.next()) {
                LocalDate dueDate = resultSet.getDate("due_date").toLocalDate();

                // Tính thời gian còn lại
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime dueDateTime = dueDate.atStartOfDay();
                Duration duration = Duration.between(now, dueDateTime);

                // Lấy ngày và giờ còn lại
                long daysRemaining = duration.toDays();
                long hoursRemaining = duration.toHoursPart();

                // Cập nhật Label
                // Kiểm tra nếu đã quá hạn
                if (duration.isNegative())
                    time_lbl.setText("Overdue");
                else
                    time_lbl.setText("Remaining: " + daysRemaining + " days " + hoursRemaining + " times");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
