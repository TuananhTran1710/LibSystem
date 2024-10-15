package com.jmc.libsystem.Models;

import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Views.ViewFactory;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Model {
    private static Model model; // phai la static de dung chung khi goi o cac file
    private final ViewFactory viewFactory;
    private User myUser;
    private final DatabaseDriver databaseDriver;
    private boolean userLoginSuccessFlag;

    Model() {
        this.databaseDriver = new DatabaseDriver();
        this.viewFactory = new ViewFactory();
        this.userLoginSuccessFlag = false;
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }

    public boolean getUserLoginSuccessFlag() {
        return this.userLoginSuccessFlag;
    }

    public User getMyUser() {
        return myUser;
    }

    public void setUserLoginSuccessFlag(boolean x) {
        userLoginSuccessFlag = x;
    }

    // su dung de xac nhan thong tin login co thanh cong ?
    public void evaluateUserCredToLogin(String email, String password) {
        ResultSet resultSet = databaseDriver.getUserDataForLogin(email, password);
        try {
            if (resultSet.isBeforeFirst()) { // isBeforeFirst check xem co it nhat 1 dong la khach hang hay khong
                this.userLoginSuccessFlag = true;
                resultSet.next();
                myUser = new User(resultSet.getString("user_id"), resultSet.getString("fullName"), email, password,
                        resultSet.getInt("attendance_score"), resultSet.getInt("reputation_score"), resultSet.getInt("max_books"), resultSet.getString("state"),
                        resultSet.getString("borrow_table_name"), resultSet.getString("favorite_table_name"));
            }
            else{
                System.out.println("Account isn't found in the database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //su dung de xac nhan thong tin dang ky thanh cong ?
    public void evaluateUserCredToSignup(String email, String password, String user_id, String fullName) {

        //String password duoc truyen vao nham muc dich co du lieu de Insert vao database khi thong tin sign-up hop le
        ResultSet resultSet = databaseDriver.getUserDataForSignUp(email);
        try {
            if (!resultSet.isBeforeFirst()) { // check xem email ton tai chua?
                this.userLoginSuccessFlag = true;

                String borrow_table_name = "Issued" + user_id;
                String favorite_table_name = "Favorite" + user_id;

                String queryInsert = "Insert into user (email, password, user_id, fullName, borrow_table_name, favorite_table_name) values (?, ?, ?, ?, ?, ?)";

                myUser = new User(email, password, user_id, fullName, 0, 100, 20, "active", borrow_table_name, favorite_table_name);

                try (PreparedStatement preparedStatementInsert = databaseDriver.getConn().prepareStatement(queryInsert)) {
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
            } else {
                System.out.println("User already exist!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You can't use this email!");
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
