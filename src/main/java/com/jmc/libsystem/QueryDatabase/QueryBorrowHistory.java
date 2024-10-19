package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Models.DatabaseDriver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryBorrowHistory {
    public static boolean isBanned(String user_id) {
        String query = "SELECT * FROM borrowhistory WHERE user_id = ? AND DATEDIFF(CURDATE(), borrow_date) > 120";
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

    public static void noticeBookOverdue(String user_id) {
        String query = "SELECT count(*) FROM borrowhistory WHERE user_id = ? AND DATEDIFF(CURDATE(), borrow_date) > 60";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
