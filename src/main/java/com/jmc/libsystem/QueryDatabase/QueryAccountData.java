package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Controllers.User.DashboardController;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.DatabaseDriver;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.AccountType;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryAccountData {
    // User Section
    public static ResultSet getDataForLogin(String email, String password, AccountType type) {
        ResultSet resultSet = null;
        try {
            String tableName = "user";
            if (type == AccountType.ADMIN) tableName = "admin";
            String query = "SELECT * FROM " + tableName + " WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    //lam the nay tuc la tren csdl, cac email phai khac nhau (PK) --> Can chinh lai table trong csdl
    public static ResultSet getUserDataForSignUp(String email, String id) {
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM user WHERE email = ?";
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet getDataForForgetPassword(String email, String id, AccountType type) {
        ResultSet resultSet = null;
        try {
            String query;
            if (type == AccountType.ADMIN) {
                query = "SELECT * FROM admin WHERE email = ? and admin_id = ?";
            } else query = "SELECT * FROM user WHERE email = ? and user_id = ?";

            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, id);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getCountUser(){
        ResultSet resultSet = null;
        try {
            String query = "SELECT COUNT(*) as count FROM user;";

            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getAccountForSearch(String keyWord, String type) {
        ResultSet resultSet = null;
        String query = "SELECT * FROM user WHERE " + type + " COLLATE utf8mb4_general_ci LIKE ?";
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

    public static ResultSet getAllAccount() {
        ResultSet resultSet = null;
        String query = "SELECT * FROM user";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void insertAccount(String email, String password, String user_id, String fullName) {

        //String password duoc truyen vao nham muc dich co du lieu de Insert vao database khi thong tin sign-up hop le
        ResultSet resultSet = QueryAccountData.getUserDataForSignUp(email, user_id);
        try {
            if (!resultSet.isBeforeFirst()) { // check xem email ton tai chua?
                String queryInsert = "Insert into user (email, password, user_id, fullName, state) values (?, ?, ?, ?, ?)";

                try (PreparedStatement preparedStatementInsert = DatabaseDriver.getConn().prepareStatement(queryInsert)) {
                    preparedStatementInsert.setString(1, email);
                    preparedStatementInsert.setString(2, password);
                    preparedStatementInsert.setString(3, user_id);
                    preparedStatementInsert.setString(4, fullName);
                    preparedStatementInsert.setString(5, "Active");
                    preparedStatementInsert.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                resultSet.next();
                if (resultSet.getString("state").toLowerCase().equals("banned")) {
                    System.out.println("Account was banned!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Account was banned!");
                    alert.show();
                } else if (resultSet.getString("state").toLowerCase().equals("deleted")) {

                    String queryInsert = "update user set password = ?, fullName = ? where user_id = ?";

                    try (PreparedStatement preparedStatementInsert = DatabaseDriver.getConn().prepareStatement(queryInsert)) {
                        preparedStatementInsert.setString(1, password);
                        preparedStatementInsert.setString(2, fullName);
                        preparedStatementInsert.setString(3, user_id);

                        preparedStatementInsert.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
