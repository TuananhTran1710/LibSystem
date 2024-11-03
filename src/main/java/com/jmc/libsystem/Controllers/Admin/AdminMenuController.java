package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.AdminMenuOptions;
import com.jmc.libsystem.Views.UserMenuOptions;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController {
    public Button dashboard_btn;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListensButton();
    }

    private void addListensButton() {
        dashboard_btn.setOnAction(event -> onDashBoard());
    }

    private void onDashBoard() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DASHBOARD);
    }
}
