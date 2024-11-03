package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.AdminMenuOptions;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button dashboard_btn;
    public Button user_btn;
    public Button book_btn;
    public Button response_btn;
    public Button profile_btn;
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListensButton();
    }

    private void addListensButton() {
        dashboard_btn.setOnAction(event -> onDashBoard());
        logout_btn.setOnAction(event -> convertToLogin());
    }

    private void convertToLogin() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.LOGOUT);
        Model.getInstance().setLoginFlag(false);

        // tao mot doi tuong moi de tranh thao tac voi listener cu (trong admin controller)
        Model.getInstance().getViewFactory().setAdminSelectedMenuItem(new SimpleObjectProperty<>());
    }

    private void onDashBoard() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DASHBOARD);
    }
}
