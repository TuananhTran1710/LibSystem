package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.Controllers.User.DashboardController;
import com.jmc.libsystem.Models.Model;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController {
    public BorderPane admin_parent;

    public void initialize(URL arg0, ResourceBundle arg1) {
        admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminDashboardView());
        admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminMenu());
        handleChangeMenu();
    }

    private void handleChangeMenu() {
        Model.getInstance().getViewFactory().getUserSelectedMenuItem()
                .addListener((observableValue, oldVal, newVal) -> {
                    switch (newVal) {
                        case LOGOUT -> {
                            Model.getInstance().getViewFactory().showLoginWindow();
                            Stage stage = (Stage) admin_parent.getScene().getWindow();
                            Model.getInstance().getViewFactory().closeStage(stage);
                        }
                        default -> {
                            admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminDashboardView());
                        }
                    }
                });
    }
}
