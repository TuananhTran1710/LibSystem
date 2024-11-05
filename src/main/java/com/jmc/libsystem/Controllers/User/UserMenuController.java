package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.UserMenuOptions;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class UserMenuController implements Initializable {
    public Button dashboard_btn;
    public Button search_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button report_btn;
    public Button mybook_btn;
    public Button propose_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListensButton();
    }

    private void addListensButton() {
        dashboard_btn.setOnAction(event -> onDashBoard());
        logout_btn.setOnAction(event -> convertToLogin());
        mybook_btn.setOnAction(event -> onMyBook());
        search_btn.setOnAction(event -> onSearch());
        propose_btn.setOnAction(event -> onPropose());
        profile_btn.setOnAction(event -> onProfile());
    }

    private void onProfile() {
        Model.getInstance().getViewFactory().getUserSelectedMenuItem().set(UserMenuOptions.PROFILE);
    }

    private void onPropose() {
        Model.getInstance().getViewFactory().getUserSelectedMenuItem().set(UserMenuOptions.PROPOSE);
    }

    private void onSearch() {
        Model.getInstance().getViewFactory().getUserSelectedMenuItem().set(UserMenuOptions.SEARCH);
    }

    private void convertToLogin() {
        Model.getInstance().getViewFactory().getUserSelectedMenuItem().set(UserMenuOptions.LOGOUT);
        Model.getInstance().setLoginFlag(false);

        // tao mot doi tuong moi de tranh thao tac voi listener cu (trong user controller)
        Model.getInstance().getViewFactory().setUserSelectedMenuItem(new SimpleObjectProperty<>());
    }

    private void onDashBoard() {
        Model.getInstance().getViewFactory().getUserSelectedMenuItem().set(UserMenuOptions.DASHBOARD);
    }

    private void onMyBook() {
        Model.getInstance().getViewFactory().getUserSelectedMenuItem().set(UserMenuOptions.MYBOOK);
    }
}
