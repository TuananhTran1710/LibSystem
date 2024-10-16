package com.jmc.libsystem.Models;

import com.jmc.libsystem.Information.Admin;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Views.ViewFactory;

public class Model {
    private static Model model; // phai la static de dung chung khi goi o cac file
    private final ViewFactory viewFactory;
    private User myUser;
    private Admin myAdmin;
    private final DatabaseDriver databaseDriver;
    private boolean loginFlag;

    Model() {
        this.databaseDriver = new DatabaseDriver();
        this.viewFactory = new ViewFactory();
        this.loginFlag = false;
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

    public boolean getLoginFlag() {
        return this.loginFlag;
    }

    public void setLoginFlag(boolean x) {
        loginFlag = x;
    }

    public User getMyUser() {
        return myUser;
    }

    public void setMyUser(User user) {
        myUser = user;
    }

    public Admin getMyAdmin() {
        return myAdmin;
    }

    public void setMyAdmin(Admin myAdmin) {
        this.myAdmin = myAdmin;
    }
}
