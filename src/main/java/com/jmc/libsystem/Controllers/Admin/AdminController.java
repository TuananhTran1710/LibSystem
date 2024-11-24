package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane admin_parent;

    private static AdminController instance;

    public AdminController() {
        instance = this;
    }

    public static AdminController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminDashboardView());
        admin_parent.setLeft(Model.getInstance().getViewFactory().getAdminMenu());
        handleChangeMenu();
    }

    private void handleChangeMenu() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem()
                .addListener((observableValue, oldVal, newVal) -> {
                    switch (newVal) {
                        case LOGOUT -> {
                            Model.getInstance().getViewFactory().showLoginWindow();
                            Stage stage = (Stage) admin_parent.getScene().getWindow();
                            Model.getInstance().getViewFactory().closeStage(stage);
                        }
                        case USER -> {
                            admin_parent.setCenter(Model.getInstance().getViewFactory().getManageUserView());
                            //ManageUserController.getInstance().refreshData();
                        }
                        case BOOK -> {
                            admin_parent.setCenter(Model.getInstance().getViewFactory().getManageBookView());
                            ManageBookController.getInstance().reset();
                        }
                        case PROFILE -> {
                            admin_parent.setCenter(Model.getInstance().getViewFactory().getProfileAdminView());
                            ProfileController.getInstance().showProfile();
                        }
                        case RESPONSE -> {
                            admin_parent.setCenter(Model.getInstance().getViewFactory().getResponseView());
                            ResponseController.getInstance().refreshData();
                        }
                        default -> {
                            admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminDashboardView());
                            AdminDashboardController.getInstance().refreshData();
                        }

                    }
                });
    }
}
