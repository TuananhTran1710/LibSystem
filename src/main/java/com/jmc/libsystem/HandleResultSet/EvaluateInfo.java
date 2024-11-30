package com.jmc.libsystem.HandleResultSet;

import com.jmc.libsystem.Information.Admin;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.DatabaseDriver;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryAccountData;
import com.jmc.libsystem.Views.AccountType;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvaluateInfo {

    // su dung de xac nhan thong tin login co thanh cong ?
    public static void evaluateInfoToLogin(String email, String password, AccountType type) {
        ResultSet resultSet = QueryAccountData.getDataForLogin(email, password, type);
        try {
            if (resultSet.isBeforeFirst()) { // isBeforeFirst check xem co it nhat 1 dong la khach hang hay khong
                resultSet.next();
                if (type == AccountType.USER) {
                    if (resultSet.getString("state").toLowerCase().equals("deleted")) {
                        System.out.println("Account is removed from library system!");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Account is deteted. Please sign up again with this id and this email to recover your account!");
                        alert.show();
                    } else if (resultSet.getString("state").toLowerCase().equals("banned")) {
                        System.out.println("Account is banned because you violated the library policy");
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Account is banned because you violated the library policy");
                        alert.show();
                    } else {
                        Model.getInstance().setLoginFlag(true);
                        Model.getInstance().setMyUser(new User(resultSet.getString("user_id"), resultSet.getString("fullName"),
                                email, password, resultSet.getString("state")));

                    }
                } else {
                    Model.getInstance().setLoginFlag(true);
                    Model.getInstance().setMyAdmin(new Admin(resultSet.getString("admin_id"), resultSet.getString("fullName"), email, password));
                }
            } else {
                System.out.println("Account isn't found in the database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
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

    //su dung de xac nhan thong tin dang ky thanh cong ?
    //(email, id are all unique)
    public static void evaluateUserCredToSignup(String email, String password, String user_id, String fullName) {

        //String password duoc truyen vao nham muc dich co du lieu de Insert vao database khi thong tin sign-up hop le
        ResultSet resultSet = QueryAccountData.getUserDataForSignUp(email, user_id);
        try {
            if (!resultSet.isBeforeFirst()) { // check xem email ton tai chua?
                Model.getInstance().setLoginFlag(true);

                String queryInsert = "Insert into user (email, password, user_id, fullName) values (?, ?, ?, ?)";
                Model.getInstance().setMyUser(new User(user_id, fullName, email, password, "Active"));

                try (PreparedStatement preparedStatementInsert = DatabaseDriver.getConn().prepareStatement(queryInsert)) {
                    preparedStatementInsert.setString(1, email);
                    preparedStatementInsert.setString(2, password);
                    preparedStatementInsert.setString(3, user_id);
                    preparedStatementInsert.setString(4, fullName);

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
                    Model.getInstance().setLoginFlag(true);

                    String queryInsert = "update user set password = ?, fullName = ? where user_id = ?";
                    Model.getInstance().setMyUser(new User(user_id, fullName, email, password, "Active"));

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


    // su dung de xac nhan thong tin xu ly forget password
    public static void evaluateInfoToGetPassword(String email, String id, AccountType type) {

        ResultSet resultSet = QueryAccountData.getDataForForgetPassword(email, id, type);
        try {
            if (resultSet.isBeforeFirst()) { // check xem email va id ton tai chua?
                resultSet.next();
                if (type == AccountType.USER) {
                    String state = resultSet.getString("state").toLowerCase();
                    if (state.equals("Active")) {
                        String my_password = resultSet.getString("password");
                        System.out.println("Get password successfully!");
                        Alert notice = new Alert(Alert.AlertType.INFORMATION);
                        notice.setContentText("Your password is " + my_password);
                        notice.show();
                    } else if (state.equals("Deleted")) {
                        System.out.println("Account is deleted!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Account is deteted. Please sign up again with this id and this email to recover your account!");
                        alert.show();
                    } else {
                        System.out.println("Account is banned because you violated the library policy");
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Account is banned because you violated the library policy");
                        alert.show();
                    }
                } else {
                    String my_password = resultSet.getString("password");
                    System.out.println("Get password successfully!");
                    Alert notice = new Alert(Alert.AlertType.INFORMATION);
                    notice.setContentText("Your password is " + my_password);
                    notice.show();
                }
            } else {
                System.out.println("Your ID and Email aren't correct or available!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Your ID and Email aren't correct or available!");
                alert.show();
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
