package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.Controllers.User.DashboardController;
import com.jmc.libsystem.Models.Model;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController {
    public BorderPane admin_parent;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminDashboardView());
        admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminMenu());
        handleChangeMenu();
        System.out.println("admin can call");
    }

    private void handleChangeMenu() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem()
                .addListener((observableValue, oldVal, newVal) -> {
                    switch (newVal) {
                        default -> {
                            admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminDashboardView());
                        }
                    }
                });
    }
}
