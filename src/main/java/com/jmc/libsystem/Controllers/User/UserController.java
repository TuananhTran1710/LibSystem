package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    public BorderPane user_parent;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Model.getInstance().getViewFactory().getUserSelectedMenuItem()
                .addListener((observableValue, oldVal, newVal) -> {
                    switch (newVal) {
                        case LOGOUT -> {
                            Model.getInstance().getViewFactory().showLoginWindow();
                            Stage stage = (Stage) user_parent.getScene().getWindow();
                            Model.getInstance().getViewFactory().closeStage(stage);
                        }
                        default -> user_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView()
                        );
                    }
                });
    }
}
