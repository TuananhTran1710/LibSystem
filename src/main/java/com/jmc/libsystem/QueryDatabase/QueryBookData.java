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
        String query = "SELECT title, authors, thumbnail_url FROM book WHERE " + type + " COLLATE utf8mb4_general_ci LIKE ?";
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
}

