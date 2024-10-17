package com.jmc.libsystem.Models;

import com.jmc.libsystem.Information.Admin;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Views.AccountType;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvaluateInfo {

    // su dung de xac nhan thong tin login co thanh cong ?
    public static void evaluateInfoToLogin(String email, String password, AccountType type) {
        ResultSet resultSet = DatabaseDriver.getDataForLogin(email, password, type);
        try {
            if (resultSet.isBeforeFirst()) { // isBeforeFirst check xem co it nhat 1 dong la khach hang hay khong
                Model.getInstance().setLoginFlag(true);
                resultSet.next();
                if (type == AccountType.USER) {
                    Model.getInstance().setMyUser(new User(resultSet.getString("user_id"), resultSet.getString("fullName"), email, password,
                            resultSet.getInt("attendance_score"), resultSet.getInt("reputation_score"), resultSet.getInt("max_books"), resultSet.getString("state"),
                            resultSet.getString("borrow_table_name"), resultSet.getString("favorite_table_name")));
                } else {
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
        ResultSet resultSet = DatabaseDriver.getUserDataForSignUp(email, user_id);
        try {
            if (!resultSet.isBeforeFirst()) { // check xem email ton tai chua?
                Model.getInstance().setLoginFlag(true);

                String borrow_table_name = "Issued" + user_id;
                String favorite_table_name = "Favorite" + user_id;

                String queryInsert = "Insert into user (email, password, user_id, fullName, borrow_table_name, favorite_table_name) values (?, ?, ?, ?, ?, ?)";

                Model.getInstance().setMyUser(new User(user_id, fullName, email, password, 0, 100, 20, "active", borrow_table_name, favorite_table_name));

                try (PreparedStatement preparedStatementInsert = DatabaseDriver.getConn().prepareStatement(queryInsert)) {
                    preparedStatementInsert.setString(1, email);
                    preparedStatementInsert.setString(2, password);
                    preparedStatementInsert.setString(3, user_id);
                    preparedStatementInsert.setString(4, fullName);
                    preparedStatementInsert.setString(5, borrow_table_name);
                    preparedStatementInsert.setString(6, favorite_table_name);

                    preparedStatementInsert.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
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

        ResultSet resultSet = DatabaseDriver.getDataForForgetPassword(email, id, type);
        try {
            if (resultSet.isBeforeFirst()) { // check xem email ton tai chua?
                resultSet.next();
                String my_password = resultSet.getString("password");
                System.out.println("Get password successfully!");
                Alert notice = new Alert(Alert.AlertType.INFORMATION);
                notice.setContentText("Your password is " + my_password);
                notice.show();
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
