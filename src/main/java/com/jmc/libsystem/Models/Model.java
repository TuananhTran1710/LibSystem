package com.jmc.libsystem.Models;

import com.jmc.libsystem.Views.ViewFactory;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Model {
    private static Model model; // phai la static de dung chung khi goi o cac file
    private final ViewFactory viewFactory;

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

    public void setUserLoginSuccessFlag(boolean x) {
        userLoginSuccessFlag = x;
    }

    // su dung de xac nhan thong tin login co thanh cong ?
    public void evaluateUserCredToLogin(String username, String password) {
        ResultSet resultSet = databaseDriver.getUserDataForLogin(username, password);
        try {
            if (resultSet.isBeforeFirst()) { // isBeforeFirst check xem co it nhat 1 dong la khach hang hay khong
                this.userLoginSuccessFlag = true;
            }
            else{
                System.out.println("User isn't found in the database!");
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
    public void evaluateUserCredToSignup(String username, String password) {

        //String password duoc truyen vao nham muc dich co du lieu de Insert vao database khi thong tin sign-up hop le
        ResultSet resultSet = databaseDriver.getUserDataForSignUp(username);
        try {
            if (!resultSet.isBeforeFirst()) { // check xem username ton tai chua?
                this.userLoginSuccessFlag = true;

                String queryInsert = "Insert into users (username, password) values (?, ?)";
                try (PreparedStatement preparedStatementInsert = databaseDriver.getConn().prepareStatement(queryInsert)) {
                    preparedStatementInsert.setString(1, username);
                    preparedStatementInsert.setString(2, password);
                    preparedStatementInsert.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("User already exist!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You can't use this username!");
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
