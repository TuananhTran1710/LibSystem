package com.jmc.libsystem.Models;

import com.jmc.libsystem.Views.ViewFactory;
import javafx.scene.control.Alert;

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

    public void evaluateClientCred(String username, String password) {
        ResultSet resultSet = databaseDriver.getClientData(username, password);
        try {
            if (resultSet.isBeforeFirst()) { // isBeforeFirst check xem co it nhat 1 dong la khach hang hay khong
                this.userLoginSuccessFlag = true;
            }
            else{
                System.out.println("User not found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
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
}
