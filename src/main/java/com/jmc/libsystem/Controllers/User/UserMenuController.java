package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.UserMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class UserMenuController implements Initializable {
    public Button dashboard_btn;
    public Button search_btn;
    public Button favourite_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button report_btn;
    public Button my_book_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListensButton();
    }

    private void addListensButton() {
        dashboard_btn.setOnAction(event -> onDashBoard());
        logout_btn.setOnAction(event -> convertToLogin());
    }

    private void convertToLogin() {
        Model.getInstance().setUserLoginSuccessFlag(false);
        Model.getInstance().getViewFactory().getUserSelectedMenuItem().set(UserMenuOptions.LOGOUT);
    }

    private void onDashBoard() {
        Model.getInstance().getViewFactory().getUserSelectedMenuItem().set(UserMenuOptions.DASHBOARD);
    }
}
