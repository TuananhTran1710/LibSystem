package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Models.DatabaseDriver;
import com.jmc.libsystem.Views.AccountType;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HandleAccountDatabase {
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
            if (!resultSet.isBeforeFirst()) {
                String query2 = "SELECT * FROM user WHERE user_id = ?";
                preparedStatement = DatabaseDriver.getConn().prepareStatement(query2);
                preparedStatement.setString(1, id);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.isBeforeFirst()) {
                    System.out.println("User already exist!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("You can't use this id!");
                    alert.show();
                }
            } else {
                System.out.println("User already exist!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You can't use this email!");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
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
}
